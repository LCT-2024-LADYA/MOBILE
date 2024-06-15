package ru.gozerov.presentation.screens.trainer.diary.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.repositories.ServiceRepository
import ru.gozerov.domain.repositories.TrainingRepository
import ru.gozerov.presentation.screens.trainer.diary.diary.models.TrainerDiaryEffect
import ru.gozerov.presentation.screens.trainer.diary.diary.models.TrainerDiaryIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class TrainerDiaryViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val trainingRepository: TrainingRepository
) : ViewModel() {

    private val _effect = MutableStateFlow<TrainerDiaryEffect>(TrainerDiaryEffect.None)
    val effect: StateFlow<TrainerDiaryEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: TrainerDiaryIntent) {
        viewModelScope.launch {
            when (intent) {
                is TrainerDiaryIntent.Reset -> {
                    _effect.emit(TrainerDiaryEffect.None)
                }

                is TrainerDiaryIntent.LoadTrainings -> {
                    runCatchingNonCancellation {
                        trainingRepository.getTrainerTrainings("")
                    }
                        .onSuccess { flow ->
                            _effect.emit(TrainerDiaryEffect.LoadedTrainings(flow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerDiaryEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainerDiaryIntent.GetSchedule -> {
                    runCatchingNonCancellation {
                        serviceRepository.getSchedule(intent.month)
                    }
                        .onSuccess { trainings ->
                            trainingRepository.clearAddedTrainings()
                            _effect.emit(TrainerDiaryEffect.LoadedSchedule(trainings))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerDiaryEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainerDiaryIntent.GetServicesAtDate -> {
                    runCatchingNonCancellation {
                        serviceRepository.getScheduledServices(intent.ids)
                    }
                        .onSuccess { services ->
                            _effect.emit(TrainerDiaryEffect.LoadedServices(services))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerDiaryEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainerDiaryIntent.RemoveScheduleService -> {
                    runCatchingNonCancellation {
                        serviceRepository.deleteScheduledService(id = intent.id)
                    }
                        .onSuccess {
                            _effect.emit(TrainerDiaryEffect.RemovedService(intent.id))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerDiaryEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }
}