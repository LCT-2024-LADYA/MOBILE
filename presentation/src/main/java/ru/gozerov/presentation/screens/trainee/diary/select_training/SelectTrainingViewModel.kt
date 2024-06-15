package ru.gozerov.presentation.screens.trainee.diary.select_training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.toTrainingCard
import ru.gozerov.domain.repositories.TrainingRepository
import ru.gozerov.domain.usecases.GetTrainingsUseCase
import ru.gozerov.presentation.screens.trainee.diary.select_training.models.SelectTrainingEffect
import ru.gozerov.presentation.screens.trainee.diary.select_training.models.SelectTrainingIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class SelectTrainingViewModel @Inject constructor(
    private val getTrainingsUseCase: GetTrainingsUseCase,
    private val trainingRepository: TrainingRepository
) : ViewModel() {

    private val _effect = MutableStateFlow<SelectTrainingEffect>(SelectTrainingEffect.None)
    val effect: StateFlow<SelectTrainingEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: SelectTrainingIntent) {
        viewModelScope.launch {
            when (intent) {
                is SelectTrainingIntent.Reset -> {
                    _effect.emit(SelectTrainingEffect.None)
                }

                is SelectTrainingIntent.FindInAllTrainings -> {
                    runCatchingNonCancellation {
                        getTrainingsUseCase.invoke(intent.query)
                    }
                        .onSuccess { flow ->
                            _effect.emit(SelectTrainingEffect.LoadedAllTrainings(flow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(SelectTrainingEffect.Error(throwable.message.toString()))
                        }
                }

                is SelectTrainingIntent.GetTrainings -> {
                    runCatchingNonCancellation {
                        getTrainingsUseCase.invoke("")
                    }
                        .onSuccess { allFlow ->
                            runCatchingNonCancellation {
                                trainingRepository.getTrainerTrainings("")
                            }
                                .onSuccess { trainerFlow ->
                                    _effect.emit(
                                        SelectTrainingEffect.AllFoundTrainings(
                                            allFlow,
                                            trainerFlow.map { data ->
                                                data.map { card ->
                                                    card.toTrainingCard()
                                                }
                                            }
                                        )
                                    )
                                }
                                .onFailure { throwable ->
                                    _effect.emit(SelectTrainingEffect.Error(throwable.message.toString()))
                                }
                        }
                        .onFailure { throwable ->
                            _effect.emit(SelectTrainingEffect.Error(throwable.message.toString()))
                        }
                }

                is SelectTrainingIntent.AddTraining -> {
                    trainingRepository.addTrainingToCreating(intent.training)
                }

                is SelectTrainingIntent.FindInTrainerTrainings -> {
                    runCatchingNonCancellation {
                        trainingRepository.getTrainerTrainings("")
                    }
                        .onSuccess { trainerFlow ->
                            _effect.emit(
                                SelectTrainingEffect.LoadedTrainerTrainings(
                                    trainerFlow.map { data ->
                                        data.map { card ->
                                            card.toTrainingCard()
                                        }
                                    }
                                )
                            )
                        }
                        .onFailure { throwable ->
                            _effect.emit(SelectTrainingEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}