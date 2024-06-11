package ru.gozerov.presentation.screens.trainee.main_training.process

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.CompleteExerciseUseCase
import ru.gozerov.domain.usecases.SetCompletedTrainingIdUseCase
import ru.gozerov.presentation.screens.trainee.main_training.process.models.TrainingProcessEffect
import ru.gozerov.presentation.screens.trainee.main_training.process.models.TrainingProcessIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class TrainingProcessViewModel @Inject constructor(
    private val completeExerciseUseCase: CompleteExerciseUseCase,
    private val setCompletedTrainingIdUseCase: SetCompletedTrainingIdUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<TrainingProcessEffect>(TrainingProcessEffect.None)
    val effect: StateFlow<TrainingProcessEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: TrainingProcessIntent) {
        viewModelScope.launch {
            when (intent) {
                is TrainingProcessIntent.Reset -> {
                    _effect.emit(TrainingProcessEffect.None)
                }

                is TrainingProcessIntent.CompleteExercise -> {
                    runCatchingNonCancellation {
                        completeExerciseUseCase.invoke(intent.trainingId, intent.exerciseId)
                    }
                        .onFailure { throwable ->
                            _effect.emit(TrainingProcessEffect.Error(throwable.message.toString()))
                        }
                }

                is TrainingProcessIntent.EndTraining -> {
                    runCatchingNonCancellation {
                        setCompletedTrainingIdUseCase.invoke(intent.trainingId)
                    }
                        .onFailure { throwable ->
                            _effect.emit(TrainingProcessEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}
