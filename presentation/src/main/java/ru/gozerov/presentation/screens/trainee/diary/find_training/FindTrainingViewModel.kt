package ru.gozerov.presentation.screens.trainee.diary.find_training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.GetCustomTrainingsUseCase
import ru.gozerov.domain.usecases.GetTrainingsUseCase
import ru.gozerov.presentation.screens.trainee.diary.find_training.models.FindTrainingEffect
import ru.gozerov.presentation.screens.trainee.diary.find_training.models.FindTrainingIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class FindTrainingViewModel @Inject constructor(
    private val getTrainingsUseCase: GetTrainingsUseCase,
    private val getCustomTrainingsUseCase: GetCustomTrainingsUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<FindTrainingEffect>(FindTrainingEffect.None)
    val effect: StateFlow<FindTrainingEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: FindTrainingIntent) {
        viewModelScope.launch {
            when (intent) {
                is FindTrainingIntent.Reset -> {
                    _effect.emit(FindTrainingEffect.None)
                }

                is FindTrainingIntent.FindInAllTrainings -> {
                    runCatchingNonCancellation {
                        getTrainingsUseCase.invoke(intent.query)
                    }
                        .onSuccess { flow ->
                            _effect.emit(FindTrainingEffect.LoadedAllTrainings(flow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(FindTrainingEffect.Error(throwable.message.toString()))
                        }
                }

                is FindTrainingIntent.GetTrainings -> {
                    runCatchingNonCancellation {
                        getTrainingsUseCase.invoke("")
                    }
                        .onSuccess { allFlow ->
                            runCatchingNonCancellation {
                                getCustomTrainingsUseCase.invoke("")
                            }
                                .onSuccess { userFlow ->
                                    _effect.emit(
                                        FindTrainingEffect.AllFoundTrainings(
                                            allFlow,
                                            userFlow
                                        )
                                    )
                                }
                                .onFailure { throwable ->
                                    _effect.emit(FindTrainingEffect.Error(throwable.message.toString()))
                                }
                        }
                        .onFailure { throwable ->
                            _effect.emit(FindTrainingEffect.Error(throwable.message.toString()))
                        }
                }

                is FindTrainingIntent.FindInUserTrainings -> {
                    runCatchingNonCancellation {
                        getCustomTrainingsUseCase.invoke(intent.query)
                    }
                        .onSuccess { flow ->
                            _effect.emit(FindTrainingEffect.LoadedUserTrainings(flow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(FindTrainingEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}