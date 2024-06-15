package ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.models

sealed interface TrainingToScheduleEffect {

    object None : TrainingToScheduleEffect

    data class LastAddedTraining(
        val id: Int
    ) : TrainingToScheduleEffect

    class Error(
        val message: String
    ) : TrainingToScheduleEffect

}