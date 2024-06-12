package ru.gozerov.presentation.screens.trainee.chat.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.Achievement
import ru.gozerov.domain.models.Role
import ru.gozerov.domain.models.Specialization
import ru.gozerov.domain.models.TrainerCard
import ru.gozerov.domain.models.TrainerService
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.chat.chat.views.AttachServiceBottomSheet
import ru.gozerov.presentation.shared.views.MessageTextField
import ru.gozerov.presentation.shared.views.UserAvatar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun ChatScreen(navController: NavController) {
    val trainer = TrainerCard(
        1,
        null,
        "Евгений",
        "Геркулесов",
        listOf(Role(1, "Персональный тренер")),
        age = 100,
        experience = 17,
        sex = 1,
        quote = "Если по расписанию день ног - значит сегодня выходной",
        specializations = listOf(
            Specialization(0, "Набор мышечной массы"),
            Specialization(1, "Составление программ тренировок")
        ),
        services = listOf(
            TrainerService(0, "Тренироrfefewrwrwerwerewrewrefsfsfsdfdsfsfswewrwerвка", 1000),
            TrainerService(1, "План тренировок на неделю", 2000)
        ),
        achievements = listOf(
            Achievement(0, "Набор мышечной массы", true),
            Achievement(1, "Составление программ тренировок", true)
        ),
        workingDays = "пн - пт",
        workingTime = "8:00 - 19:00"
    )

    val messageState = remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val messages = (0..10).map { "Я уже месяц поддерживаю свой режим питания" }
    val serviceMessageState = remember { mutableStateOf("") }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            AttachServiceBottomSheet(serviceMessageState)
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = FitLadyaTheme.colors.secondary
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
                    }
            ) {

                Row(
                    modifier = Modifier.height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(48.dp),
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = FitLadyaTheme.colors.text
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            }
                        ) {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "trainer",
                                trainer
                            )
                            navController.navigate(Screen.TrainerCard.route)
                        }
                    ) {
                        UserAvatar(
                            size = 40.dp,
                            photo = trainer.photoUrl,
                            padding = 4.dp,
                            background = FitLadyaTheme.colors.primaryBorder
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.padding(end = 16.dp)) {
                            Text(
                                text = "${trainer.firstName} ${trainer.lastName}",
                                color = FitLadyaTheme.colors.text,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium

                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(id = R.string.online),
                                fontWeight = FontWeight.Medium,
                                color = FitLadyaTheme.colors.fieldPrimaryText,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(color = FitLadyaTheme.colors.primaryBackground)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        reverseLayout = true,

                        ) {
                        items(messages.size) { index ->
                            if (index % 2 == 0)
                                MeMessageCard(text = messages[index])
                            else
                                UserMessageCard(text = messages[index])
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .background(color = FitLadyaTheme.colors.secondary)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MessageTextField(
                        textState = messageState,
                        placeholderText = stringResource(id = R.string.message),
                        onSend = {},
                        onAttach = {
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        }
                    )
                }

            }
        }
    }
}

@Composable
fun MeMessageCard(text: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Column(
            modifier = Modifier
                .widthIn(120.dp, 280.dp)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .background(
                    color = FitLadyaTheme.colors.primary,
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 12.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = text,
                color = FitLadyaTheme.colors.text,
                fontSize = 16.sp
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = "14:48",
                    color = FitLadyaTheme.colors.text.copy(alpha = 0.36f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_read_message),
                    contentDescription = null,
                    tint = FitLadyaTheme.colors.accent
                )
            }
        }
    }
}

@Composable
fun UserMessageCard(text: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        Column(
            modifier = Modifier
                .widthIn(120.dp, 280.dp)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .background(
                    color = FitLadyaTheme.colors.secondary,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomEnd = 12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = text,
                color = FitLadyaTheme.colors.text,
                fontSize = 16.sp
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = "14:48",
                    color = FitLadyaTheme.colors.text.copy(alpha = 0.36f)
                )
            }
        }
    }
}