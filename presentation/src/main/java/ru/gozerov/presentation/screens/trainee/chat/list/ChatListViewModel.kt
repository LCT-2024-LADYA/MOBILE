package ru.gozerov.presentation.screens.trainee.chat.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.GetClientChatsUseCase
import ru.gozerov.domain.usecases.GetRolesUseCase
import ru.gozerov.domain.usecases.GetSpecializationsUseCase
import ru.gozerov.domain.usecases.GetTrainerCardsUseCase
import ru.gozerov.presentation.screens.trainee.chat.list.models.ChatListEffect
import ru.gozerov.presentation.screens.trainee.chat.list.models.ChatListIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getClientChatsUseCase: GetClientChatsUseCase,
    private val getTrainerCardsUseCase: GetTrainerCardsUseCase,
    private val getSpecializationsUseCase: GetSpecializationsUseCase,
    private val getRolesUseCase: GetRolesUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<ChatListEffect>(ChatListEffect.None)
    val effect: StateFlow<ChatListEffect>
        get() = _effect.asStateFlow()


    fun handleIntent(intent: ChatListIntent) {
        viewModelScope.launch {
            when (intent) {
                is ChatListIntent.LoadRolesAndSpecializations -> {
                    getSpecializationsUseCase.invoke()
                        .onSuccess { specializations ->
                            viewModelScope.launch {
                                getRolesUseCase.invoke()
                                    .onSuccess { roles ->
                                        _effect.emit(
                                            ChatListEffect.LoadedRolesAndSpecializations(
                                                roles,
                                                specializations
                                            )
                                        )
                                    }
                                    .onFailure { throwable ->
                                        _effect.emit(ChatListEffect.Error(throwable.message.toString()))
                                    }
                            }
                        }
                        .onFailure { throwable ->
                            _effect.emit(ChatListEffect.Error(throwable.message.toString()))
                        }
                }


                is ChatListIntent.Init -> {
                    runCatchingNonCancellation {
                        getClientChatsUseCase.invoke(intent.query)
                    }
                        .onSuccess { chats ->
                            runCatchingNonCancellation {
                                getTrainerCardsUseCase.invoke(
                                    intent.query,
                                    intent.roles,
                                    intent.specializations
                                )
                            }
                                .onSuccess { trainersFlow ->
                                    _effect.emit(
                                        ChatListEffect.LoadedChatsAndTrainers(chats, trainersFlow)
                                    )
                                }
                                .onFailure { throwable ->
                                    _effect.emit(ChatListEffect.Error(throwable.message.toString()))
                                }
                        }
                        .onFailure { throwable ->
                            _effect.emit(ChatListEffect.Error(throwable.message.toString()))
                        }
                }

                is ChatListIntent.Reset -> {
                    _effect.emit(ChatListEffect.None)
                }

                is ChatListIntent.LoadChats -> {
                    runCatchingNonCancellation {
                        getClientChatsUseCase.invoke(intent.query)
                    }
                        .onSuccess { chats ->
                            _effect.emit(ChatListEffect.LoadedChats(chats))
                        }
                        .onFailure { throwable ->
                            _effect.emit(ChatListEffect.Error(throwable.message.toString()))
                        }
                }

                is ChatListIntent.LoadTrainers -> {
                    runCatchingNonCancellation {
                        getTrainerCardsUseCase.invoke(
                            intent.query,
                            intent.roles,
                            intent.specializations
                        )
                    }
                        .onSuccess { trainersFlow ->
                            _effect.emit(ChatListEffect.LoadedTrainers(trainersFlow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(ChatListEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}