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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.UserCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.chat.list.views.SimpleChatCard
import ru.gozerov.presentation.screens.trainee.chat.list.views.SimpleChatUserCard
import ru.gozerov.presentation.shared.views.SearchTextField
import ru.gozerov.presentation.ui.theme.FitLadyaTheme
import kotlin.random.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun TrainerChatListScreen(
    contentPaddingValues: PaddingValues,
    parentNavController: NavController
) {

    val searchState = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    val tabs = listOf(stringResource(R.string.chats), stringResource(R.string.users))
    val selectedTab = remember { mutableIntStateOf(0) }

    val chatList = (0..15).map {
        ChatCard(
            it,
            null,
            "Евгений Геркулесов",
            "Я уже месяц поддерживаю свою хуй",
            "14:48",
            1
        )
    }

    val userList = (0..15).map {
        UserCard(
            it, null,
            "Евгений",
            "Геркулесов",
            1,
            Random.nextInt(14, 100)
        )
    }

    val coroutineScope = rememberCoroutineScope()
    val errorMessage = stringResource(id = R.string.error)

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

                TabRow(selectedTabIndex = selectedTab.intValue, indicator = { tabPositions ->
                    val currentTabPosition = tabPositions[selectedTab.intValue]
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
                            selected = tabs.indexOf(tab) == selectedTab.intValue,
                            onClick = {
                                selectedTab.intValue = tabs.indexOf(tab)
                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 16.dp),
                                text = tab,
                                fontWeight = FontWeight.Medium,
                                color = if (tabs[selectedTab.intValue] == tab) FitLadyaTheme.colors.fieldPrimaryText else FitLadyaTheme.colors.text.copy(
                                    alpha = 0.48f
                                )
                            )
                        }
                    }
                }
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(0.dp))
                }
                if (selectedTab.intValue == 0) {
                    items(chatList.size) { index ->
                        SimpleChatCard(chatList[index]) {
                            parentNavController.navigate(Screen.TrainerChat.route)
                        }
                    }
                } else {
                    items(userList.size) { index ->
                        SimpleChatUserCard(user = userList[index])
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }

    }

}