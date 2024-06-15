package ru.gozerov.presentation.screens.trainer.diary.diary_plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.GetLastAddedTrainingUseCase
import ru.gozerov.presentation.screens.trainer.diary.diary_plan.models.SchedulePlanIntent
import javax.inject.Inject

@HiltViewModel
class SchedulePlanViewModel @Inject constructor(
    private val clearLastAddedTrainingUseCase: GetLastAddedTrainingUseCase
): ViewModel() {

    fun handleIntent(intent: SchedulePlanIntent) {
        viewModelScope.launch {
            when(intent) {
                is SchedulePlanIntent.ClearLastAddedTraining -> {
                    clearLastAddedTrainingUseCase.invoke()
                }
            }
        }
    }

}