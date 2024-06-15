package ru.gozerov.presentation.screens.trainer.diary.paste_training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.ScheduleTrainingUseCase
import ru.gozerov.presentation.screens.trainer.diary.paste_training.models.PasteTrainingEffect
import ru.gozerov.presentation.screens.trainer.diary.paste_training.models.PasteTrainingIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class PasteTrainingViewModel @Inject constructor(
    private val scheduleTrainingUseCase: ScheduleTrainingUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<PasteTrainingEffect>(PasteTrainingEffect.None)
    val effect: StateFlow<PasteTrainingEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: PasteTrainingIntent) {
        viewModelScope.launch {
            when (intent) {
                is PasteTrainingIntent.Reset -> {
                    _effect.emit(PasteTrainingEffect.None)
                }

                is PasteTrainingIntent.ScheduleTraining -> {
                    runCatchingNonCancellation {
                        scheduleTrainingUseCase.invoke(
                            intent.id,
                            intent.date,
                            intent.timeStart,
                            intent.timeEnd,
                            intent.exercises
                        )
                    }
                        .onSuccess {
                            _effect.emit(PasteTrainingEffect.Pasted)
                        }
                        .onFailure { throwable ->
                            _effect.emit(PasteTrainingEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }


}