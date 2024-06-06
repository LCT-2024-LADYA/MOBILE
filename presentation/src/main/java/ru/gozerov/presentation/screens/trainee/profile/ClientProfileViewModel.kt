package ru.gozerov.presentation.screens.trainee.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.GetClientInfoUseCase
import ru.gozerov.domain.usecases.LogoutAsClientUseCase
import ru.gozerov.domain.usecases.UpdateClientInfoUseCase
import ru.gozerov.domain.usecases.UpdateClientPhotoUseCase
import ru.gozerov.presentation.screens.trainee.profile.models.ClientProfileEffect
import ru.gozerov.presentation.screens.trainee.profile.models.ClientProfileIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class ClientProfileViewModel @Inject constructor(
    private val getClientInfoUseCase: GetClientInfoUseCase,
    private val updateClientInfoUseCase: UpdateClientInfoUseCase,
    private val updateClientPhotoUseCase: UpdateClientPhotoUseCase,
    private val logoutAsClientUseCase: LogoutAsClientUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<ClientProfileEffect>(ClientProfileEffect.None)
    val effect: StateFlow<ClientProfileEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: ClientProfileIntent) {
        viewModelScope.launch {
            when (intent) {
                is ClientProfileIntent.Reset -> {
                    _effect.emit(ClientProfileEffect.None)
                }

                is ClientProfileIntent.GetInfo -> {
                    runCatchingNonCancellation {
                        getClientInfoUseCase.invoke()
                    }
                        .map { result ->
                            result
                                .onSuccess { info ->
                                    _effect.emit(ClientProfileEffect.LoadedProfile(info))
                                }
                                .onFailure { throwable ->

                                    _effect.emit(ClientProfileEffect.Error(throwable.message.toString()))
                                }
                        }

                }

                is ClientProfileIntent.UpdateInfo -> {
                    runCatchingNonCancellation {
                        updateClientInfoUseCase.invoke(
                            intent.age,
                            intent.email,
                            intent.firstName,
                            intent.lastName,
                            intent.sex
                        )
                    }
                        .map { result ->
                            result
                                .onSuccess {
                                    _effect.emit(ClientProfileEffect.SuccessfulInfoUpdate)
                                }
                                .onFailure { throwable ->
                                    _effect.emit(ClientProfileEffect.Error(throwable.message.toString()))
                                }
                        }
                }

                is ClientProfileIntent.UpdatePhoto -> {
                    runCatchingNonCancellation {
                        updateClientPhotoUseCase.invoke(intent.photoUri)
                    }
                        .map { result ->
                            result
                                .onSuccess {
                                    _effect.emit(ClientProfileEffect.SuccessfulPhotoUpdate)
                                }
                                .onFailure { throwable ->
                                    _effect.emit(ClientProfileEffect.Error(throwable.message.toString()))
                                }
                        }
                }

                ClientProfileIntent.Logout -> {
                    runCatchingNonCancellation {
                        logoutAsClientUseCase.invoke()
                    }
                        .onSuccess {
                            _effect.emit(ClientProfileEffect.Logout)
                        }
                        .onFailure {
                            _effect.emit(ClientProfileEffect.Error("Unknown error"))
                        }
                }
            }
        }
    }

}