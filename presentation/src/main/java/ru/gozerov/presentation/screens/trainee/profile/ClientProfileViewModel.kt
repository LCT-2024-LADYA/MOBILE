package ru.gozerov.presentation.screens.trainee.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.GetClientInfoUseCase
import ru.gozerov.presentation.screens.trainee.profile.models.ClientProfileEffect
import ru.gozerov.presentation.screens.trainee.profile.models.ClientProfileIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class ClientProfileViewModel @Inject constructor(
    private val getClientInfoUseCase: GetClientInfoUseCase
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

                is ClientProfileIntent.FillProfile -> {}
                is ClientProfileIntent.UpdateInfo -> {}
            }
        }
    }

}