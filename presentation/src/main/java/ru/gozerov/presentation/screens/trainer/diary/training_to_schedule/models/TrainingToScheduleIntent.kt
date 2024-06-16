package ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.models

sealed interface TrainingToScheduleIntent {

    object Reset : TrainingToScheduleIntent

    data class GetPlanById(val id: Int) : TrainingToScheduleIntent

    data class GetSchedule(
        val month: Int
    ) : TrainingToScheduleIntent

    object GetLastAddedTraining : TrainingToScheduleIntent

}