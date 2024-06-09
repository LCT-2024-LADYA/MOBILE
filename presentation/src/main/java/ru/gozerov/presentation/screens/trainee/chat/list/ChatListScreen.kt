package ru.gozerov.presentation.screens.trainee.chat.list

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.screens.trainee.chat.list.views.SimpleChatCard
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun ChatListScreen() {

    val searchState = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    val tabs = listOf(stringResource(R.string.chats), stringResource(R.string.trainers))
    val selectedTab = remember { mutableIntStateOf(0) }

    val chatList = listOf(
        ChatCard(
            0,
            null,
            "Евгений Геркулесов",
            "Я уже месяц поддерживаю свою хуй",
            "14:48",
            1
        ),
        ChatCard(
            1,
            null,
            "Евгений Геркулесов",
            "Я уже месяц поддерживаю свою хуй",
            "14:48",
            1
        ),
        ChatCard(
            2,
            null,
            "Евгений Геркулесов",
            "Я уже месяц поддерживаю свою хуй",
            "14:48",
            1
        ),
        ChatCard(
            3,
            null,
            "Евгений Геркулесов",
            "Я уже месяц поддерживаю свою хуй",
            "14:48",
            1
        )
    )

    val coroutineScope = rememberCoroutineScope()
    val errorMessage = stringResource(id = R.string.error)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.secondary
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CustomTextField(
                labelText = stringResource(id = R.string.age),
                textState = searchState,
                modifier = Modifier.width(260.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        TabRow(selectedTabIndex = selectedTab.intValue) {
            Tab(selected = tabs.indexOf(tabs[0]) == selectedTab.intValue, onClick = { }) {

            }
            Tab(selected = tabs.indexOf(tabs[1]) == selectedTab.intValue, onClick = { }) {

            }
        }
        LazyColumn(
            modifier = Modifier.background(color = FitLadyaTheme.colors.primaryBackground),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(chatList.size) { index ->
                SimpleChatCard(chatList[index])
            }
        }
    }

}