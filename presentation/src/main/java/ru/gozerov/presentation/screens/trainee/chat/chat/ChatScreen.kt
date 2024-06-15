package ru.gozerov.presentation.screens.trainee.chat.chat

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.ChatMessage
import ru.gozerov.domain.models.TrainerService
import ru.gozerov.domain.utils.parseDateToHoursAndMinutes
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.chat.chat.models.ChatEffect
import ru.gozerov.presentation.screens.trainee.chat.chat.models.ChatIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.AttachServiceBottomSheet
import ru.gozerov.presentation.shared.views.MessageTextField
import ru.gozerov.presentation.shared.views.UserAvatar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel,
    trainer: ChatCard
) {

    val effect = viewModel.effect.collectAsState().value

    val messageState = remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val serviceMessageState = remember { mutableStateOf("") }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    val services = remember { mutableStateOf<List<TrainerService>>(emptyList()) }
    val messages = remember { mutableStateOf<LazyPagingItems<ChatMessage>?>(null) }

    LaunchedEffect(null) {
        viewModel.handleIntent(ChatIntent.GetTrainerServices(trainer.id))
    }

    when (effect) {
        is ChatEffect.None -> {}

        is ChatEffect.TrainerServices -> {
            services.value = effect.services
            viewModel.handleIntent(ChatIntent.GetMessages(trainer.id))
        }

        is ChatEffect.LoadedMessages -> {
            val data = effect.messages.collectAsLazyPagingItems()
            if (data.itemCount != 0) {
                val message = data.itemSnapshotList.items.first()
                viewModel.handleIntent(ChatIntent.UpdateIds(message.trainerId, message.userId))
                messages.value = data
            }
            val pagingData = PagingData.from(data.itemSnapshotList.items)
            viewModel.handleIntent(ChatIntent.SaveMessages(pagingData))
        }

        is ChatEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(ChatIntent.Reset)
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            AttachServiceBottomSheet(serviceMessageState, services.value) { id, message ->
                viewModel.handleIntent(ChatIntent.SendMessage(trainer.id, message, id))
                serviceMessageState.value = ""
                coroutineScope.launch {
                    scaffoldState.bottomSheetState.collapse()
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = FitLadyaTheme.colors.secondary,
            topBar = {
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
                                "trainerId",
                                trainer.id
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

            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
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


                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .background(color = FitLadyaTheme.colors.primaryBackground)
                        .animateContentSize(),
                    reverseLayout = true,
                ) {
                    messages.value?.itemCount?.let { itemCount ->
                        items(itemCount) { index ->
                            val message = messages.value!![index]
                            message?.let {
                                if (message.isToUser)
                                    UserMessageCard(
                                        message = message,
                                        services = services.value
                                    )
                                else
                                    MeMessageCard(message = message, services = services.value)
                            }
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
                        onSend = {
                            viewModel.handleIntent(
                                ChatIntent.SendMessage(
                                    trainer.id,
                                    messageState.value
                                )
                            )
                            messageState.value = ""
                        },
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
fun MeMessageCard(message: ChatMessage, services: List<TrainerService>) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Column(
            horizontalAlignment = Alignment.End,
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
                .padding(horizontal = 12.dp, vertical = 6.dp),
        ) {

            Column(
                modifier = Modifier
                    .widthIn(64.dp, 280.dp),
                horizontalAlignment = Alignment.Start
            ) {
                message.serviceId?.let { serviceId ->
                    val service = services.firstOrNull { s -> s.id == serviceId }
                    service?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                modifier = Modifier.padding(end = 8.dp),
                                painter = painterResource(id = R.drawable.ic_trainer_service),
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.accent
                            )
                            Column {
                                Text(
                                    text = service.name,
                                    color = FitLadyaTheme.colors.text,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(id = R.string.price_is, service.price),
                                    color = FitLadyaTheme.colors.text.copy(alpha = 0.48f),
                                    maxLines = 1,
                                    fontSize = 12.sp,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
                message.message?.let { text ->
                    Text(
                        text = text,
                        color = FitLadyaTheme.colors.text,
                        fontSize = 16.sp
                    )
                }
            }
            Text(
                textAlign = TextAlign.End,
                text = parseDateToHoursAndMinutes(message.time),
                color = FitLadyaTheme.colors.text.copy(alpha = 0.36f)
            )
        }
    }
}

@Composable
fun UserMessageCard(message: ChatMessage, services: List<TrainerService>) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .widthIn(120.dp, 280.dp)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .background(
                    color = FitLadyaTheme.colors.secondary,
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = 12.dp
                    )
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
        ) {
            Column(
                modifier = Modifier.widthIn(64.dp, 280.dp),
                horizontalAlignment = Alignment.Start
            ) {
                message.serviceId?.let { serviceId ->
                    val service = services.firstOrNull { s -> s.id == serviceId }
                    service?.let {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                modifier = Modifier.padding(end = 8.dp),
                                painter = painterResource(id = R.drawable.ic_trainer_service),
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.accent
                            )
                            Column {
                                Text(
                                    text = service.name,
                                    color = FitLadyaTheme.colors.text,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(id = R.string.price_is, service.price),
                                    color = FitLadyaTheme.colors.text.copy(alpha = 0.48f),
                                    maxLines = 1,
                                    fontSize = 12.sp,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
                message.message?.let { text ->

                    Text(
                        modifier = Modifier.widthIn(64.dp, 280.dp),
                        text = text,
                        color = FitLadyaTheme.colors.text,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
            Text(
                text = parseDateToHoursAndMinutes(message.time),
                textAlign = TextAlign.End,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.36f)
            )
        }
    }
}