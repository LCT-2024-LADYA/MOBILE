package ru.gozerov.presentation.screens.trainee.chat.list

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
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
import androidx.compose.runtime.mutableIntStateOf
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
import ru.gozerov.domain.models.Role
import ru.gozerov.domain.models.Specialization
import ru.gozerov.domain.models.TrainerCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.chat.list.models.ChatListEffect
import ru.gozerov.presentation.screens.trainee.chat.list.models.ChatListIntent
import ru.gozerov.presentation.screens.trainee.chat.list.views.TrainerFilterView
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.utils.toChatCard
import ru.gozerov.presentation.shared.views.SearchTextField
import ru.gozerov.presentation.shared.views.SimpleChatCard
import ru.gozerov.presentation.shared.views.SimpleChatTrainerCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun ChatListScreen(
    contentPaddingValues: PaddingValues,
    parentNavController: NavController,
    viewModel: ChatListViewModel
) {
    val effect = viewModel.effect.collectAsState().value

    val searchState = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    val tabs = listOf(stringResource(R.string.chats), stringResource(R.string.trainers))
    val availableSex =
        listOf(stringResource(id = R.string.sex_man_d), stringResource(id = R.string.sex_woman_d))

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    val chats = remember { mutableStateOf<List<ChatCard>>(emptyList()) }
    val trainers = remember { mutableStateOf<LazyPagingItems<TrainerCard>?>(null) }

    val selectedRole = remember { mutableIntStateOf(-1) }
    val selectedSpecializations = remember { mutableStateOf<List<Specialization>>(emptyList()) }

    LaunchedEffect(null) {
        viewModel.handleIntent(ChatListIntent.Init(searchState.value, listOf(), listOf()))
    }

    val specializations = remember { mutableStateOf<List<Specialization>>(listOf()) }
    val roles = remember { mutableStateOf<List<Role>>(listOf()) }

    when (effect) {
        is ChatListEffect.None -> {}

        is ChatListEffect.LoadedRolesAndSpecializations -> {
            roles.value = effect.roles
            specializations.value = effect.specializations
        }

        is ChatListEffect.LoadedChatsAndTrainers -> {
            chats.value = effect.chats
            trainers.value = effect.trainersFlow.collectAsLazyPagingItems()
            if (roles.value.isEmpty())
                viewModel.handleIntent(ChatListIntent.LoadRolesAndSpecializations)
        }

        is ChatListEffect.LoadedChats -> {
            chats.value = effect.chats
        }

        is ChatListEffect.LoadedTrainers -> {
            trainers.value = effect.trainersFlow.collectAsLazyPagingItems()
        }

        is ChatListEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
        }
    }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            TrainerFilterView(
                selectedRole = selectedRole,
                selectedSpecializations = selectedSpecializations,
                roles = roles.value,
                specializations = specializations
            ) {
                coroutineScope.launch {
                    scaffoldState.bottomSheetState.collapse()
                }
                viewModel.handleIntent(
                    ChatListIntent.LoadTrainers(
                        query = searchState.value,
                        roles = if (selectedRole.intValue in roles.value.indices) listOf(roles.value[selectedRole.intValue].id) else listOf(),
                        selectedSpecializations.value.map { specialization ->
                            specialization.id
                        }
                    )
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = FitLadyaTheme.colors.primaryBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(indication = null, interactionSource = remember {
                        MutableInteractionSource()
                    }) {
                        if (scaffoldState.bottomSheetState.isExpanded) {
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }) {
                Column(
                    modifier = Modifier.background(color = FitLadyaTheme.colors.secondary)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    if (pagerState.currentPage == 0) {
                        SearchTextField(
                            textState = searchState,
                            onValueChange = { text ->
                                searchState.value = text
                                viewModel.handleIntent(ChatListIntent.LoadChats(searchState.value))
                            },
                            placeholderText = stringResource(id = R.string.search_in_chats),
                            containerColor = FitLadyaTheme.colors.primaryBackground
                        )
                    } else {
                        SearchTextField(
                            textState = searchState,
                            onValueChange = { text ->
                                searchState.value = text
                                viewModel.handleIntent(
                                    ChatListIntent.LoadTrainers(
                                        query = searchState.value,
                                        roles = if (selectedRole.intValue in roles.value.indices) listOf(
                                            roles.value[selectedRole.intValue].id
                                        ) else listOf(),
                                        selectedSpecializations.value.map { specialization ->
                                            specialization.id
                                        }
                                    )
                                )
                            },
                            placeholderText = stringResource(id = R.string.find_trainer),
                            containerColor = FitLadyaTheme.colors.primaryBackground,
                            onFilter = {
                                coroutineScope.launch {
                                    scaffoldState.bottomSheetState.expand()
                                }
                            }
                        )
                    }

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
                HorizontalPager(
                    modifier = Modifier.weight(1f),
                    state = pagerState,
                    verticalAlignment = Alignment.Top
                ) { page ->
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(0.dp))
                        }
                        if (page == 0) {
                            items(chats.value.size) { index ->
                                SimpleChatCard(chats.value[index]) {
                                    parentNavController.currentBackStackEntry?.savedStateHandle?.set(
                                        "trainer",
                                        chats.value[index]
                                    )
                                    parentNavController.navigate(Screen.ClientChat.route)
                                }
                            }
                        } else {
                            trainers.value?.let { data ->
                                items(data.itemCount) { index ->
                                    val trainer = data[index]
                                    trainer?.let {
                                        SimpleChatTrainerCard(
                                            trainer = trainer,
                                            sex = availableSex[trainer.sex - 1],
                                            onProfileClick = {
                                                parentNavController.currentBackStackEntry?.savedStateHandle?.set(
                                                    "trainerId",
                                                    trainer.id
                                                )
                                                parentNavController.navigate(Screen.TrainerCard.route)
                                            },
                                            onTextClick = {
                                                parentNavController.currentBackStackEntry?.savedStateHandle?.set(
                                                    "trainer",
                                                    trainer.toChatCard()
                                                )
                                                parentNavController.navigate(Screen.ClientChat.route)
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
}