package ru.gozerov.presentation.screens.trainee.diary.diary.models

import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.models.ScheduledTraining

sealed interface DiaryEffect {

    object None : DiaryEffect

    data class LoadedSchedule(
        val trainings: List<ScheduledTraining>
    ) : DiaryEffect

    data class LoadedTrainings(
        val trainings: List<CustomTraining>
    ) : DiaryEffect

    data class Error(
        val message: String
    ) : DiaryEffect

}