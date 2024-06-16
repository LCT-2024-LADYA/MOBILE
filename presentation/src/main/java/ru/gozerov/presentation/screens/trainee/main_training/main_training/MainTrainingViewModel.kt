package ru.gozerov.presentation.screens.trainee.main_training.main_training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.ClearAddedTrainingUseCase
import ru.gozerov.domain.usecases.GetNextTrainingUseCase
import ru.gozerov.presentation.screens.trainee.main_training.main_training.models.MainTrainingEffect
import ru.gozerov.presentation.screens.trainee.main_training.main_training.models.MainTrainingIntent
import ru.gozerov.presentation.screens.trainee.main_training.main_training.models.MainTrainingViewState
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class MainTrainingViewModel @Inject constructor(
    private val getNextTrainingUseCase: GetNextTrainingUseCase,
    private val clearAddedTrainingUseCase: ClearAddedTrainingUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<MainTrainingViewState>(MainTrainingViewState.None)
    val viewState: StateFlow<MainTrainingViewState>
        get() = _viewState.asStateFlow()

    private val _effect = MutableStateFlow<MainTrainingEffect>(MainTrainingEffect.None)
    val effect: StateFlow<MainTrainingEffect>
        get() = _effect.asStateFlow()

    init {
        viewModelScope.launch {
            clearAddedTrainingUseCase.invoke()
        }
    }

    fun handleIntent(intent: MainTrainingIntent) {
        viewModelScope.launch {
            when (intent) {
                is MainTrainingIntent.Reset -> {
                    _viewState.emit(MainTrainingViewState.None)
                    _effect.emit(MainTrainingEffect.None)
                }

                is MainTrainingIntent.LoadNextTraining -> {
                    runCatchingNonCancellation {
                        getNextTrainingUseCase.invoke()
                    }
                        .onSuccess { training ->
                            training?.let { _ ->
                                _viewState.emit(MainTrainingViewState.LoadedTraining(training))
                            } ?: _viewState.emit(MainTrainingViewState.Empty)
                        }
                        .onFailure { throwable ->
                            _effect.emit(MainTrainingEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}