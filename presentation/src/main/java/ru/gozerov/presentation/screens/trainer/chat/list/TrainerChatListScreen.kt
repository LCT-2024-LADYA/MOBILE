package ru.gozerov.presentation.screens.trainer.chat.list

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.UserCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainer.chat.list.models.TrainerChatListEffect
import ru.gozerov.presentation.screens.trainer.chat.list.models.TrainerChatListIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.SearchTextField
import ru.gozerov.presentation.shared.views.SimpleChatCard
import ru.gozerov.presentation.shared.views.SimpleChatUserCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun TrainerChatListScreen(
    contentPaddingValues: PaddingValues,
    parentNavController: NavController,
    viewModel: TrainerChatListViewModel
) {
    val effect = viewModel.effect.collectAsState().value

    val searchState = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    val tabs = listOf(stringResource(R.string.chats), stringResource(R.string.users))

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    val chats = remember { mutableStateOf<List<ChatCard>>(emptyList()) }
    val clients = remember { mutableStateOf<LazyPagingItems<UserCard>?>(null) }

    LaunchedEffect(null) {
        viewModel.handleIntent(TrainerChatListIntent.Init("", searchState.value))
    }
    when (effect) {
        is TrainerChatListEffect.None -> {}
        is TrainerChatListEffect.LoadedChatsAndClients -> {
            chats.value = effect.chats
            clients.value = effect.clientsFlow.collectAsLazyPagingItems()
        }

        is TrainerChatListEffect.LoadedChats -> {
            chats.value = effect.chats
        }

        is TrainerChatListEffect.LoadedClients -> {
            clients.value = effect.clientsFlow.collectAsLazyPagingItems()
        }

        is TrainerChatListEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
        }
    }

    Scaffold(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.background(color = FitLadyaTheme.colors.secondary)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                SearchTextField(
                    textState = searchState,
                    placeholderText = stringResource(id = R.string.search_in_chats),
                    containerColor = FitLadyaTheme.colors.primaryBackground
                )

                TabRow(selectedTabIndex = pagerState.currentPage, indicator = { tabPositions ->
                    val currentTabPosition = tabPositions[pagerState.currentPage]
                    Box(
                        Modifier
                            .tabIndicatorOffset(currentTabPosition)
                            .height(3.dp)
                            .padding(horizontal = 64.dp)
                            .background(
                                color = FitLadyaTheme.colors.primary,
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                            )
                    )
                }, divider = {}) {
                    tabs.forEach { tab ->
                        Tab(
                            modifier = Modifier.background(FitLadyaTheme.colors.secondary),
                            selected = tabs.indexOf(tab) == pagerState.currentPage,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(tabs.indexOf(tab))
                                }
                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 16.dp),
                                text = tab,
                                fontWeight = FontWeight.Medium,
                                color = if (tabs[pagerState.currentPage] == tab) FitLadyaTheme.colors.fieldPrimaryText else FitLadyaTheme.colors.text.copy(
                                    alpha = 0.48f
                                )
                            )
                        }
                    }
                }
            }
            HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) { page ->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(0.dp))
                    }
                    if (pagerState.currentPage == 0) {
                        items(chats.value.size) { index ->
                            SimpleChatCard(chats.value[index]) {
                                parentNavController.currentBackStackEntry?.savedStateHandle?.set(
                                    "client",
                                    chats.value[index]
                                )
                                parentNavController.navigate(Screen.TrainerChat.route) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    } else {
                        clients.value?.let { data ->
                            items(data.itemCount) { index ->
                                val client = data[index]
                                client?.let {
                                    SimpleChatUserCard(
                                        user = client,
                                        onProfileClick = {
                                            parentNavController.currentBackStackEntry?.savedStateHandle?.set(
                                                "clientId",
                                                client.id
                                            )
                                            parentNavController.navigate(Screen.ClientCard.route)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

        }

    }

}