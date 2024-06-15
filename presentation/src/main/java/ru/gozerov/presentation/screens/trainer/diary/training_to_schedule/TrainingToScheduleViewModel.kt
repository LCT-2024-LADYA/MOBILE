package ru.gozerov.presentation.screens.trainer.diary.training_to_schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.GetLastAddedTrainingUseCase
import ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.models.TrainingToScheduleEffect
import ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.models.TrainingToScheduleIntent
import javax.inject.Inject

@HiltViewModel
class TrainingToScheduleViewModel @Inject constructor(
    private val getLastAddedTrainingUseCase: GetLastAddedTrainingUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<TrainingToScheduleEffect>(TrainingToScheduleEffect.None)
    val effect: StateFlow<TrainingToScheduleEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: TrainingToScheduleIntent) {
        viewModelScope.launch {
            when (intent) {
                is TrainingToScheduleIntent.Reset -> {
                    _effect.emit(TrainingToScheduleEffect.None)
                }

                is TrainingToScheduleIntent.GetLastAddedTraining -> {
                    val id = getLastAddedTrainingUseCase.invoke()
                    _effect.emit(TrainingToScheduleEffect.LastAddedTraining(id))
                }
            }
        }
    }

}