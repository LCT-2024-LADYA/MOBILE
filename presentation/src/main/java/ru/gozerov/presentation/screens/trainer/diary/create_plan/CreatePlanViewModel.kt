package ru.gozerov.presentation.screens.trainer.diary.create_plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.repositories.TrainingRepository
import ru.gozerov.presentation.screens.trainer.diary.create_plan.models.CreatePlanEffect
import ru.gozerov.presentation.screens.trainer.diary.create_plan.models.CreatePlanIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class CreatePlanViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository
) : ViewModel() {

    private val _effect = MutableStateFlow<CreatePlanEffect>(CreatePlanEffect.None)
    val effect: StateFlow<CreatePlanEffect>
        get() = _effect.asStateFlow()

    var name: String = ""
    var description: String = ""

    fun handleIntent(intent: CreatePlanIntent) {
        viewModelScope.launch {
            when (intent) {
                is CreatePlanIntent.Reset -> {
                    _effect.emit(CreatePlanEffect.None)
                }

                is CreatePlanIntent.GetAddedTrainings -> {
                    _effect.emit(CreatePlanEffect.AddedTrainings(trainingRepository.getAddedTrainings()))
                }

                is CreatePlanIntent.RemoveTraining -> {
                    trainingRepository.removeTrainingFromCreating(intent.id)
                    _effect.emit(CreatePlanEffect.RemovedTraining(intent.id))
                }

                is CreatePlanIntent.CreatePlan -> {
                    runCatchingNonCancellation {
                        trainingRepository.createPlan(
                            intent.name,
                            intent.description,
                            intent.trainings,
                            intent.userId
                        )
                    }
                        .onSuccess {
                            _effect.emit(CreatePlanEffect.CreatedPlan)
                        }
                        .onFailure { throwable ->
                            _effect.emit(CreatePlanEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}