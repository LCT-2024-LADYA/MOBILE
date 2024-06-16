package ru.gozerov.presentation.screens.trainee.diary.diary.models

sealed interface DiaryIntent {

    object Reset : DiaryIntent

    data class GetSchedule(
        val month: Int
    ) : DiaryIntent

    data class GetTrainingsAtDate(
        val ids: List<Int>
    ) : DiaryIntent

    data class DeleteScheduledTraining(
        val trainingId: Int
    ) : DiaryIntent

    object GetPlans : DiaryIntent

}