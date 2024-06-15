package ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.models

sealed interface TrainingToScheduleIntent {

    object Reset : TrainingToScheduleIntent

    object GetLastAddedTraining : TrainingToScheduleIntent

}