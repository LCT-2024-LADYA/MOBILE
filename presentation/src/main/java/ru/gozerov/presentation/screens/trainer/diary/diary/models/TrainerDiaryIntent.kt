package ru.gozerov.presentation.screens.trainer.diary.diary.models

sealed interface TrainerDiaryIntent {

    object Reset : TrainerDiaryIntent

    data class GetSchedule(
        val month: Int
    ) : TrainerDiaryIntent

    data class GetServicesAtDate(
        val ids: List<Int>
    ) : TrainerDiaryIntent

    data class RemoveScheduleService(
        val id: Int
    ) : TrainerDiaryIntent

    object LoadTrainings : TrainerDiaryIntent

}