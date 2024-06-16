package ru.gozerov.presentation.screens.trainer.chat.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.ChatItem
import ru.gozerov.domain.models.TrainerService
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.chat.chat.DateCard
import ru.gozerov.presentation.screens.trainee.chat.chat.MeMessageCard
import ru.gozerov.presentation.screens.trainee.chat.chat.UserMessageCard
import ru.gozerov.presentation.screens.trainer.chat.chat.models.TrainerChatEffect
import ru.gozerov.presentation.screens.trainer.chat.chat.models.TrainerChatIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.AttachServiceBottomSheet
import ru.gozerov.presentation.shared.views.MessageTextField
import ru.gozerov.presentation.shared.views.UserAvatar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun TrainerChatScreen(
    navController: NavController,
    viewModel: TrainerChatViewModel,
    user: ChatCard
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
    val messages = remember { mutableStateOf<LazyPagingItems<ChatItem>?>(null) }

    LaunchedEffect(null) {
        viewModel.handleIntent(TrainerChatIntent.GetServices)
    }

    when (effect) {
        is TrainerChatEffect.None -> {}

        is TrainerChatEffect.TrainerServices -> {
            services.value = effect.services
            viewModel.handleIntent(TrainerChatIntent.GetMessages(user.id))
        }

        is TrainerChatEffect.LoadedMessages -> {
            val data = effect.messages.collectAsLazyPagingItems()
            if (data.itemCount != 0) {
                val message =
                    data.itemSnapshotList.items.firstOrNull { it is ChatItem.ChatMessage } as? ChatItem.ChatMessage
                message?.let {
                    viewModel.handleIntent(
                        TrainerChatIntent.UpdateIds(message.trainerId, message.userId)
                    )
                    messages.value = data
                }
            }
            val pagingData = PagingData.from(data.itemSnapshotList.items)
            viewModel.handleIntent(TrainerChatIntent.SaveMessages(pagingData))
        }

        is TrainerChatEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(TrainerChatIntent.Reset)
        }
    }


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            AttachServiceBottomSheet(serviceMessageState, services.value) { serviceId, message ->
                viewModel.handleIntent(TrainerChatIntent.SendMessage(user.id, message, serviceId))
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
                                "clientId",
                                user.id
                            )
                            navController.navigate(Screen.ClientCard.route)
                        }
                    ) {
                        UserAvatar(
                            size = 40.dp,
                            photo = user.photoUrl,
                            padding = 4.dp,
                            background = FitLadyaTheme.colors.primaryBorder
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.padding(end = 16.dp)) {
                            Text(
                                text = "${user.firstName} ${user.lastName}",
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


                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .background(color = FitLadyaTheme.colors.primaryBackground)
                        .animateContentSize(),
                    reverseLayout = true,

                    ) {
                    messages.value?.itemCount?.let { itemCount ->
                        items(itemCount) { index ->
                            val message = messages.value!![index]
                            message?.let {
                                when (message) {
                                    is ChatItem.ChatMessage -> message.let {
                                        if (!message.isToUser)
                                            UserMessageCard(
                                                message = message,
                                                services = services.value
                                            )
                                        else
                                            MeMessageCard(
                                                message = message,
                                                services = services.value
                                            )
                                    }

                                    is ChatItem.DateMessage -> {
                                        Log.e("AAA", message.message)
                                        DateCard(date = message.message)
                                    }
                                }
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
                                TrainerChatIntent.SendMessage(
                                    user.id,
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