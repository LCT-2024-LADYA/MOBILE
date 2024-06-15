package ru.gozerov.presentation.screens.trainer.diary.diary_plan.models

sealed interface SchedulePlanIntent {

    object ClearLastAddedTraining : SchedulePlanIntent

}