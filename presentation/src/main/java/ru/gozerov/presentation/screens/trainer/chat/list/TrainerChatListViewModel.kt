package ru.gozerov.presentation.screens.trainer.chat.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.GetClientCardsUseCase
import ru.gozerov.domain.usecases.GetTrainerChatsUseCase
import ru.gozerov.presentation.screens.trainer.chat.list.models.TrainerChatListEffect
import ru.gozerov.presentation.screens.trainer.chat.list.models.TrainerChatListIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class TrainerChatListViewModel @Inject constructor(
    private val getTrainerChatsUseCase: GetTrainerChatsUseCase,
    private val getClientCardsUseCase: GetClientCardsUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<TrainerChatListEffect>(TrainerChatListEffect.None)
    val effect: StateFlow<TrainerChatListEffect>
        get() = _effect.asStateFlow()


    fun handleIntent(intent: TrainerChatListIntent) {
        viewModelScope.launch {
            when (intent) {
                is TrainerChatListIntent.Init -> {
                    runCatchingNonCancellation {
                        getTrainerChatsUseCase.invoke()
                    }
                        .onSuccess { chats ->
                            runCatchingNonCancellation {
                                getClientCardsUseCase.invoke(intent.queryClients)
                            }
                                .onSuccess { clientsFlow ->
                                    _effect.emit(
                                        TrainerChatListEffect.LoadedChatsAndClients(
                                            chats,
                                            clientsFlow
                                        )
                                    )
                                }
                                .onFailure { throwable ->
                                    _effect.emit(TrainerChatListEffect.Error(throwable.message.toString()))
                                }
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerChatListEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainerChatListIntent.Reset -> {
                    _effect.emit(TrainerChatListEffect.None)
                }

                is TrainerChatListIntent.LoadChats -> {
                    runCatchingNonCancellation {
                        getTrainerChatsUseCase.invoke()
                    }
                        .onSuccess { chats ->
                            _effect.emit(TrainerChatListEffect.LoadedChats(chats))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerChatListEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainerChatListIntent.LoadClients -> {
                    runCatchingNonCancellation {
                        getClientCardsUseCase.invoke(intent.query)
                    }
                        .onSuccess { clientsFlow ->
                            _effect.emit(TrainerChatListEffect.LoadedClients(clientsFlow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerChatListEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}