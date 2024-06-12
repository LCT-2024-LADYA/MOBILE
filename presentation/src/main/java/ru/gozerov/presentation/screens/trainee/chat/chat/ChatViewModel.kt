package ru.gozerov.presentation.screens.trainee.chat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.insertHeaderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.ChatMessage
import ru.gozerov.domain.usecases.CheckNewMessagesUseCase
import ru.gozerov.domain.usecases.GetUserChatMessagesUseCase
import ru.gozerov.domain.usecases.RunWebSocketUseCase
import ru.gozerov.domain.usecases.SendMessageUseCase
import ru.gozerov.domain.utils.getCurrentUtcTime
import ru.gozerov.presentation.screens.trainee.chat.chat.models.ChatEffect
import ru.gozerov.presentation.screens.trainee.chat.chat.models.ChatIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val runWebSocketUseCase: RunWebSocketUseCase,
    private val getUserChatMessagesUseCase: GetUserChatMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val checkNewMessagesUseCase: CheckNewMessagesUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<ChatEffect>(ChatEffect.None)
    val effect: StateFlow<ChatEffect>
        get() = _effect.asStateFlow()

    private var trainerId = 0
    private var clientId = 0

    init {
        viewModelScope.launch {
            runCatchingNonCancellation {
                runWebSocketUseCase.invoke()
            }
                .onSuccess {
                    handleIntent(ChatIntent.CheckNewMessages)
                }
                .onFailure { throwable ->
                    _effect.emit(ChatEffect.Error("Can`t load chat"))
                }
        }
    }

    private var messages: PagingData<ChatMessage>? = null

    fun handleIntent(intent: ChatIntent) {
        viewModelScope.launch {
            when (intent) {
                is ChatIntent.UpdateIds -> {
                    trainerId = intent.trainerId
                    clientId = intent.clientId
                }

                is ChatIntent.Reset -> {
                    _effect.emit(ChatEffect.None)
                }

                is ChatIntent.SaveMessages -> {
                    messages = intent.data
                }

                is ChatIntent.GetMessages -> {
                    runCatchingNonCancellation {
                        getUserChatMessagesUseCase.invoke(intent.trainerId)
                    }
                        .onSuccess { messages ->
                            _effect.emit(ChatEffect.LoadedMessages(messages))
                        }
                        .onFailure { throwable ->
                            _effect.emit(ChatEffect.Error(throwable.message.toString()))
                        }
                }

                is ChatIntent.SendMessage -> {
                    runCatchingNonCancellation {
                        sendMessageUseCase.invoke(intent.to, intent.message)
                    }
                        .onSuccess {
                            val time = getCurrentUtcTime()
                            messages = messages?.insertHeaderItem(
                                item = ChatMessage(
                                    Random.nextInt(),
                                    false,
                                    intent.message,
                                    0,
                                    time,
                                    intent.to,
                                    0
                                )
                            )
                            messages?.let { data ->
                                _effect.emit(ChatEffect.LoadedMessages(flowOf(data)))
                            }
                        }
                        .onFailure { throwable ->
                            _effect.emit(ChatEffect.Error(throwable.message.toString()))
                        }
                }

                is ChatIntent.CheckNewMessages -> {
                    runCatchingNonCancellation {
                        checkNewMessagesUseCase.invoke()
                    }
                        .onSuccess { flow ->
                            flow.collect { newMessage ->
                                if (newMessage.trainerId == trainerId && newMessage.userId == clientId) {
                                    messages = messages?.insertHeaderItem(
                                        item = ChatMessage(
                                            newMessage.id,
                                            newMessage.isToUser,
                                            newMessage.message,
                                            newMessage.serviceId,
                                            newMessage.time,
                                            newMessage.trainerId,
                                            newMessage.userId
                                        )
                                    )
                                    messages?.let { data ->
                                        _effect.emit(ChatEffect.LoadedMessages(flowOf(data)))
                                    }
                                }
                            }
                        }
                        .onFailure { throwable ->
                            _effect.emit(ChatEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}