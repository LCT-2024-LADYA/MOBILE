package ru.gozerov.presentation.screens.trainer.diary.create_plan.models

sealed interface CreatePlanIntent {

    object Reset : CreatePlanIntent

    object GetAddedTrainings : CreatePlanIntent

    data class RemoveTraining(
        val id: Int
    ) : CreatePlanIntent

    data class CreatePlan(
        val name: String,
        val description: String,
        val trainings: List<Int>,
        val userId: Int
    ) : CreatePlanIntent

}