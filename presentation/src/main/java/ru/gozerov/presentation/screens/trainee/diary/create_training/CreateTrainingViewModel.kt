package ru.gozerov.presentation.screens.trainee.diary.create_training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.repositories.TrainingRepository
import ru.gozerov.domain.usecases.AddExercisesToCreatingUseCase
import ru.gozerov.domain.usecases.ClearAddedTrainingUseCase
import ru.gozerov.domain.usecases.CreateTrainerTrainingUseCase
import ru.gozerov.domain.usecases.CreateTrainingUseCase
import ru.gozerov.domain.usecases.GetAddedExercisesUseCase
import ru.gozerov.domain.usecases.GetTrainingByIdUseCase
import ru.gozerov.domain.usecases.RemoveExerciseUseCase
import ru.gozerov.presentation.screens.trainee.diary.create_training.models.CreateTrainingEffect
import ru.gozerov.presentation.screens.trainee.diary.create_training.models.CreateTrainingIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class CreateTrainingViewModel @Inject constructor(
    private val createTrainingUseCase: CreateTrainingUseCase,
    private val getAddedExercises: GetAddedExercisesUseCase,
    private val getTrainingByIdUseCase: GetTrainingByIdUseCase,
    private val addExercisesToCreatingUseCase: AddExercisesToCreatingUseCase,
    private val clearAddedTrainingUseCase: ClearAddedTrainingUseCase,
    private val removeExerciseUseCase: RemoveExerciseUseCase,
    private val createTrainerTrainingUseCase: CreateTrainerTrainingUseCase,
    private val trainingRepository: TrainingRepository
) : ViewModel() {

    init {
        handleIntent(CreateTrainingIntent.Clear)
    }

    private val _effect = MutableStateFlow<CreateTrainingEffect>(CreateTrainingEffect.None)
    val effect: StateFlow<CreateTrainingEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: CreateTrainingIntent) {
        viewModelScope.launch {
            when (intent) {
                is CreateTrainingIntent.Reset -> {
                    _effect.emit(CreateTrainingEffect.None)
                }

                is CreateTrainingIntent.RemoveExercise -> {
                    runCatchingNonCancellation {
                        removeExerciseUseCase.invoke(intent.id)
                    }
                        .onSuccess {
                            _effect.emit(CreateTrainingEffect.RemovedExercise(id = intent.id))
                        }
                        .onFailure { throwable ->
                            _effect.emit(CreateTrainingEffect.Error(throwable.message.toString()))
                        }
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

                is CreateTrainingIntent.GetTraining -> {
                    runCatchingNonCancellation {
                        getTrainingByIdUseCase.invoke(intent.id)
                    }
                        .map { result ->
                            result
                                .onSuccess { training ->
                                    addExercisesToCreatingUseCase.invoke(training.exercises)
                                    _effect.emit(CreateTrainingEffect.LoadedTraining(training))
                                }
                                .onFailure { throwable ->
                                    _effect.emit(CreateTrainingEffect.Error(throwable.message.toString()))
                                }
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

                is CreateTrainingIntent.Clear -> {
                    runCatchingNonCancellation {
                        clearAddedTrainingUseCase.invoke()
                    }
                        .onFailure { throwable ->
                            _effect.emit(CreateTrainingEffect.Error(throwable.message.toString()))
                        }
                }

                is CreateTrainingIntent.NextTraining -> {
                    trainingRepository.setNextTraining(intent.id)
                }

                is CreateTrainingIntent.CreateTrainerTraining -> {
                    runCatchingNonCancellation {
                        createTrainerTrainingUseCase.invoke(
                            intent.createTrainingModel,
                            intent.wantsPublic
                        )
                    }
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

    override fun onCleared() {
        viewModelScope.launch {
            runCatchingNonCancellation {
                clearAddedTrainingUseCase.invoke()
            }
                .onFailure { throwable ->
                    _effect.emit(CreateTrainingEffect.Error(throwable.message.toString()))
                }
        }
        super.onCleared()
    }
}