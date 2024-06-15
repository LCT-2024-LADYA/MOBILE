package ru.gozerov.presentation.screens.trainer.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.repositories.ServiceRepository
import ru.gozerov.presentation.screens.trainer.service.models.TrainerServicesEffect
import ru.gozerov.presentation.screens.trainer.service.models.TrainerServicesIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class TrainerServicesViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    private val _effect = MutableStateFlow<TrainerServicesEffect>(TrainerServicesEffect.None)
    val effect: StateFlow<TrainerServicesEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: TrainerServicesIntent) {
        viewModelScope.launch {
            when (intent) {
                is TrainerServicesIntent.Reset -> {
                    _effect.emit(TrainerServicesEffect.None)
                }

                is TrainerServicesIntent.LoadServices -> {
                    runCatchingNonCancellation {
                        serviceRepository.getTrainerServices()
                    }
                        .onSuccess { flow ->
                            _effect.emit(TrainerServicesEffect.LoadedServices(flow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerServicesEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainerServicesIntent.SetStatus -> {
                    runCatchingNonCancellation {
                        serviceRepository.updateServiceStatus(
                            intent.serviceId,
                            intent.status,
                            intent.type
                        )
                    }
                        .onFailure { throwable ->
                            _effect.emit(TrainerServicesEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainerServicesIntent.DeleteService -> {
                    runCatchingNonCancellation {
                        serviceRepository.deleteService(intent.id)
                    }
                        .onSuccess {
                            _effect.emit(TrainerServicesEffect.DeletedService(intent.id))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerServicesEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}