package ru.gozerov.presentation.screens.trainee.diary.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.DeleteScheduledTrainingUseCase
import ru.gozerov.domain.usecases.GetScheduleUseCase
import ru.gozerov.domain.usecases.GetTrainingsAtDateUseCase
import ru.gozerov.domain.usecases.GetUserPlanCardsUseCase
import ru.gozerov.presentation.screens.trainee.diary.diary.models.DiaryEffect
import ru.gozerov.presentation.screens.trainee.diary.diary.models.DiaryIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getTrainingsAtDateUseCase: GetTrainingsAtDateUseCase,
    private val deleteScheduledTrainingUseCase: DeleteScheduledTrainingUseCase,
    private val getUserPlanCardsUseCase: GetUserPlanCardsUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<DiaryEffect>(DiaryEffect.None)
    val effect: StateFlow<DiaryEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: DiaryIntent) {
        viewModelScope.launch {
            when (intent) {
                is DiaryIntent.Reset -> {
                    _effect.emit(DiaryEffect.None)
                }

                is DiaryIntent.GetSchedule -> {
                    runCatchingNonCancellation {
                        getScheduleUseCase.invoke(intent.month)
                    }
                        .onSuccess { trainings ->
                            _effect.emit(DiaryEffect.LoadedSchedule(trainings))
                        }
                        .onFailure { throwable ->
                            _effect.emit(DiaryEffect.Error(throwable.message.toString()))
                        }
                }

                is DiaryIntent.GetTrainingsAtDate -> {
                    runCatchingNonCancellation {
                        getTrainingsAtDateUseCase.invoke(intent.ids)
                    }
                        .onSuccess { trainings ->
                            _effect.emit(DiaryEffect.LoadedTrainings(trainings))
                        }
                        .onFailure { throwable ->
                            _effect.emit(DiaryEffect.Error(throwable.message.toString()))
                        }
                }

                is DiaryIntent.DeleteScheduledTraining -> {
                    runCatchingNonCancellation {
                        deleteScheduledTrainingUseCase.invoke(intent.trainingId)
                    }
                        .onSuccess {
                            _effect.emit(DiaryEffect.RemoveTraining(intent.trainingId))
                        }
                        .onFailure { throwable ->
                            _effect.emit(DiaryEffect.Error(throwable.message.toString()))
                        }
                }

                is DiaryIntent.GetPlans -> {
                    runCatchingNonCancellation {
                        getUserPlanCardsUseCase.invoke()
                    }
                        .onSuccess { cards ->
                            _effect.emit(DiaryEffect.LoadedPlans(cards))
                        }
                        .onFailure { throwable ->
                            _effect.emit(DiaryEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}