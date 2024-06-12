package ru.gozerov.presentation.shared.screens.client_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.GetClientProfileUseCase
import ru.gozerov.presentation.shared.screens.client_card.models.ClientCardEffect
import ru.gozerov.presentation.shared.screens.client_card.models.ClientCardIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class ClientCardViewModel @Inject constructor(
    private val getClientProfileUseCase: GetClientProfileUseCase
) : ViewModel() {


    private val _effect = MutableStateFlow<ClientCardEffect>(ClientCardEffect.None)
    val effect: StateFlow<ClientCardEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: ClientCardIntent) {
        viewModelScope.launch {
            when (intent) {
                is ClientCardIntent.Reset -> {
                    _effect.emit(ClientCardEffect.None)
                }

                is ClientCardIntent.LoadProfile -> {
                    runCatchingNonCancellation {
                        getClientProfileUseCase.invoke(intent.clientId)
                    }
                        .onSuccess { client ->
                            _effect.emit(ClientCardEffect.LoadedProfile(client))
                        }
                        .onFailure { throwable ->
                            _effect.emit(ClientCardEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}