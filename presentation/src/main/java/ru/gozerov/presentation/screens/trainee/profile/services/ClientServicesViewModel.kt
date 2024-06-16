package ru.gozerov.presentation.screens.trainee.profile.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.repositories.ServiceRepository
import ru.gozerov.presentation.screens.trainee.profile.services.models.ClientServicesEffect
import ru.gozerov.presentation.screens.trainee.profile.services.models.ClientServicesIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class ClientServicesViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    private val _effect = MutableStateFlow<ClientServicesEffect>(ClientServicesEffect.None)
    val effect: StateFlow<ClientServicesEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: ClientServicesIntent) {
        viewModelScope.launch {
            when (intent) {
                is ClientServicesIntent.Reset -> {
                    _effect.emit(ClientServicesEffect.None)
                }

                is ClientServicesIntent.LoadServices -> {
                    runCatchingNonCancellation {
                        serviceRepository.getUserServices()
                    }
                        .onSuccess { flow ->
                            _effect.emit(ClientServicesEffect.LoadedServices(flow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(ClientServicesEffect.Error(throwable.message.toString()))
                        }
                }

                is ClientServicesIntent.SetStatus -> {
                    runCatchingNonCancellation {
                        serviceRepository.updateServiceStatus(
                            intent.serviceId,
                            intent.status,
                            intent.type
                        )
                    }
                        .onFailure { throwable ->
                            _effect.emit(ClientServicesEffect.Error(throwable.message.toString()))
                        }
                }

                is ClientServicesIntent.DeleteService -> {
                    runCatchingNonCancellation {
                        serviceRepository.deleteService(intent.id)
                    }
                        .onSuccess {
                            _effect.emit(ClientServicesEffect.DeletedService(intent.id))
                        }
                        .onFailure { throwable ->
                            _effect.emit(ClientServicesEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}