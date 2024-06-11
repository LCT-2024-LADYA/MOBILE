package ru.gozerov.presentation.screens.trainee.diary.find_exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.AddExerciseToCreatingUseCase
import ru.gozerov.domain.usecases.GetCustomExercisesUseCase
import ru.gozerov.presentation.screens.trainee.diary.find_exercise.models.FindExerciseEffect
import ru.gozerov.presentation.screens.trainee.diary.find_exercise.models.FindExerciseIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class FindExerciseViewModel @Inject constructor(
    private val getExercisesUseCase: GetCustomExercisesUseCase,
    private val addExerciseToCreatingUseCase: AddExerciseToCreatingUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<FindExerciseEffect>(FindExerciseEffect.None)
    val effect: StateFlow<FindExerciseEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: FindExerciseIntent) {
        viewModelScope.launch {
            when (intent) {
                is FindExerciseIntent.Reset -> {
                    _effect.emit(FindExerciseEffect.None)
                }

                is FindExerciseIntent.SearchExercise -> {
                    runCatchingNonCancellation {
                        getExercisesUseCase.invoke(intent.query)
                    }
                        .onSuccess { flow ->
                            _effect.emit(FindExerciseEffect.LoadedExercises(flow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(FindExerciseEffect.Error(throwable.message.toString()))
                        }
                }

                is FindExerciseIntent.AddExercise -> {
                    runCatchingNonCancellation {
                        addExerciseToCreatingUseCase.invoke(intent.exercise)
                    }
                        .onSuccess {
                            _effect.emit(FindExerciseEffect.Exit)
                        }
                        .onFailure { throwable ->
                            _effect.emit(FindExerciseEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}