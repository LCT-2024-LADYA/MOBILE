package ru.gozerov.presentation.screens.trainee.diary.create_training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.CreateTrainingUseCase
import ru.gozerov.domain.usecases.GetAddedExercisesUseCase
import ru.gozerov.presentation.screens.trainee.diary.create_training.models.CreateTrainingEffect
import ru.gozerov.presentation.screens.trainee.diary.create_training.models.CreateTrainingIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class CreateTrainingViewModel @Inject constructor(
    private val createTrainingUseCase: CreateTrainingUseCase,
    private val getAddedExercises: GetAddedExercisesUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<CreateTrainingEffect>(CreateTrainingEffect.None)
    val effect: StateFlow<CreateTrainingEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: CreateTrainingIntent) {
        viewModelScope.launch {
            when (intent) {
                is CreateTrainingIntent.Reset -> {
                    _effect.emit(CreateTrainingEffect.None)
                }

                is CreateTrainingIntent.GetAddedExercises -> {
                    runCatchingNonCancellation {
                        getAddedExercises.invoke()
                    }
                        .onSuccess { exercises ->
                            _effect.emit(CreateTrainingEffect.AddedExercises(exercises))
                        }
                        .onFailure { throwable ->
                            _effect.emit(CreateTrainingEffect.Error(throwable.message.toString()))
                        }
                }

                is CreateTrainingIntent.CreateTraining -> {
                    runCatchingNonCancellation {
                        createTrainingUseCase.invoke(
                            intent.createTrainingModel,
                            intent.date,
                            intent.timeStart,
                            intent.timeEnd
                        )
                    }
                        .map { result ->
                            result
                                .onSuccess {
                                    _effect.emit(CreateTrainingEffect.CreatedTraining)
                                }
                                .onFailure { throwable ->
                                    _effect.emit(CreateTrainingEffect.Error(throwable.message.toString()))
                                }
                        }
                }
            }
        }
    }

}