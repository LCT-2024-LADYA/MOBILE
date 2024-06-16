package ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.models

import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainingPlan

sealed interface TrainingToScheduleEffect {

    object None : TrainingToScheduleEffect

    data class LastAddedTraining(
        val id: Int
    ) : TrainingToScheduleEffect

    data class LoadedSchedule(
        val trainings: List<ScheduledTraining>
    ) : TrainingToScheduleEffect

    data class LoadedPlan(
        val plan: TrainingPlan
    ) : TrainingToScheduleEffect

    class Error(
        val message: String
    ) : TrainingToScheduleEffect

}