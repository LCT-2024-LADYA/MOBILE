package ru.gozerov.presentation.screens.trainer.diary.training_to_schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.GetLastAddedTrainingUseCase
import ru.gozerov.domain.usecases.GetPlanByIdUseCase
import ru.gozerov.domain.usecases.GetScheduleUseCase
import ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.models.TrainingToScheduleEffect
import ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.models.TrainingToScheduleIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class TrainingToScheduleViewModel @Inject constructor(
    private val getLastAddedTrainingUseCase: GetLastAddedTrainingUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getPlanByIdUseCase: GetPlanByIdUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<TrainingToScheduleEffect>(TrainingToScheduleEffect.None)
    val effect: StateFlow<TrainingToScheduleEffect>
        get() = _effect.asStateFlow()

    var currentPosition = 0

    fun handleIntent(intent: TrainingToScheduleIntent) {
        viewModelScope.launch {
            when (intent) {
                is TrainingToScheduleIntent.Reset -> {
                    _effect.emit(TrainingToScheduleEffect.None)
                }

                is TrainingToScheduleIntent.GetPlanById -> {
                    runCatchingNonCancellation {
                        getPlanByIdUseCase.invoke(intent.id)
                    }
                        .onSuccess { plan ->
                            _effect.emit(TrainingToScheduleEffect.LoadedPlan(plan))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainingToScheduleEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainingToScheduleIntent.GetSchedule -> {
                    runCatchingNonCancellation {
                        getScheduleUseCase.invoke(intent.month)
                    }
                        .onSuccess { trainings ->
                            _effect.emit(TrainingToScheduleEffect.LoadedSchedule(trainings))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainingToScheduleEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainingToScheduleIntent.GetLastAddedTraining -> {
                    val id = getLastAddedTrainingUseCase.invoke()
                    _effect.emit(TrainingToScheduleEffect.LastAddedTraining(id))
                }
            }
        }
    }

}