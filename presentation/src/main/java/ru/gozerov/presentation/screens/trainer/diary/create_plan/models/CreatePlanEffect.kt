package ru.gozerov.presentation.screens.trainer.diary.create_plan.models

import ru.gozerov.domain.models.TrainingCard

sealed interface CreatePlanEffect {

    object None : CreatePlanEffect

    data class AddedTrainings(
        val trainings: List<TrainingCard>
    ) : CreatePlanEffect

    data class RemovedTraining(
        val id: Int
    ) : CreatePlanEffect

    data class Error(
        val message: String
    ) : CreatePlanEffect

    object CreatedPlan: CreatePlanEffect

}