package ru.gozerov.presentation.screens.trainer.chat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.insertHeaderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.ChatItem
import ru.gozerov.domain.models.TrainerMessagePage
import ru.gozerov.domain.usecases.CheckNewMessagesUseCase
import ru.gozerov.domain.usecases.GetTrainerChatMessagesUseCase
import ru.gozerov.domain.usecases.GetTrainerInfoUseCase
import ru.gozerov.domain.usecases.RunWebSocketUseCase
import ru.gozerov.domain.usecases.SendMessageUseCase
import ru.gozerov.domain.utils.getCurrentUtcTime
import ru.gozerov.domain.utils.parseDateToDDMM
import ru.gozerov.presentation.screens.trainer.chat.chat.models.TrainerChatEffect
import ru.gozerov.presentation.screens.trainer.chat.chat.models.TrainerChatIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TrainerChatViewModel @Inject constructor(
    private val runWebSocketUseCase: RunWebSocketUseCase,
    private val getTrainerChatMessagesUseCase: GetTrainerChatMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val checkNewMessagesUseCase: CheckNewMessagesUseCase,
    private val getTrainerInfoUseCase: GetTrainerInfoUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<TrainerChatEffect>(TrainerChatEffect.None)
    val effect: StateFlow<TrainerChatEffect>
        get() = _effect.asStateFlow()

    private var trainerId = 0
        set(value) {
            if (value != 0)
                field = value
        }
    private var clientId = 0
        set(value) {
            if (value != 0)
                field = value
        }

    init {
        viewModelScope.launch {
            runCatchingNonCancellation {
                runWebSocketUseCase.invoke()
            }
                .onFailure { _ ->
                    _effect.emit(TrainerChatEffect.Error("Can`t load chat"))
                }
        }
    }

    init {
        viewModelScope.launch {
            checkNewMessagesUseCase.invoke().collectLatest { newMessage ->
                if (newMessage.userId == clientId) {
                    val date = parseDateToDDMM(newMessage.time)
                    if (date != TrainerMessagePage.lastDate) {
                        messages = messages?.insertHeaderItem(
                            item = ChatItem.DateMessage(date)
                        )
                        TrainerMessagePage.lastDate = date
                    }
                    messages = messages?.insertHeaderItem(
                        item = ChatItem.ChatMessage(
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
                        _effect.emit(TrainerChatEffect.LoadedMessages(flowOf(data)))
                    }
                }
            }
        }
    }

    private var messages: PagingData<ChatItem>? = null

    fun handleIntent(intent: TrainerChatIntent) {
        viewModelScope.launch {
            when (intent) {

                is TrainerChatIntent.GetServices -> {
                    runCatchingNonCancellation {
                        getTrainerInfoUseCase.invoke()
                    }
                        .map { result ->
                            result
                                .onSuccess { info ->
                                    _effect.emit(TrainerChatEffect.TrainerServices(info.services))
                                }
                                .onFailure { throwable ->
                                    _effect.emit(TrainerChatEffect.Error(throwable.message.toString()))
                                }
                        }
                }

                is TrainerChatIntent.UpdateIds -> {
                    trainerId = intent.trainerId
                    clientId = intent.clientId
                }

                is TrainerChatIntent.Reset -> {
                    _effect.emit(TrainerChatEffect.None)
                }

                is TrainerChatIntent.SaveMessages -> {
                    messages = intent.data
                }

                is TrainerChatIntent.GetMessages -> {
                    runCatchingNonCancellation {
                        getTrainerChatMessagesUseCase.invoke(intent.userId)
                    }
                        .onSuccess { messages ->
                            _effect.emit(TrainerChatEffect.LoadedMessages(messages))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerChatEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainerChatIntent.SendMessage -> {
                    runCatchingNonCancellation {
                        sendMessageUseCase.invoke(intent.to, intent.message, intent.serviceId)
                    }
                        .onSuccess {
                            val time = getCurrentUtcTime()
                            val date = parseDateToDDMM(time)
                            if (date != TrainerMessagePage.lastDate) {
                                messages = messages?.insertHeaderItem(
                                    item = ChatItem.DateMessage(date)
                                )
                                TrainerMessagePage.lastDate = date
                            }
                            messages = messages?.insertHeaderItem(
                                item = ChatItem.ChatMessage(
                                    Random.nextInt(),
                                    true,
                                    intent.message,
                                    intent.serviceId,
                                    time,
                                    intent.to,
                                    0
                                )
                            )
                            messages?.let { data ->
                                _effect.emit(TrainerChatEffect.LoadedMessages(flowOf(data)))
                            }
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerChatEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainerChatIntent.CheckNewMessages -> {
                    runCatchingNonCancellation {
                        checkNewMessagesUseCase.invoke()
                    }
                        .onSuccess { flow ->
                            flow.collect { newMessage ->
                                if (newMessage.trainerId == trainerId && newMessage.userId == clientId) {
                                    val date = parseDateToDDMM(newMessage.time)
                                    if (date != TrainerMessagePage.lastDate) {
                                        messages = messages?.insertHeaderItem(
                                            item = ChatItem.DateMessage(date)
                                        )
                                        TrainerMessagePage.lastDate = date
                                    }
                                    messages = messages?.insertHeaderItem(
                                        item = ChatItem.ChatMessage(
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
                                        _effect.emit(TrainerChatEffect.LoadedMessages(flowOf(data)))
                                    }
                                }
                            }
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerChatEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}