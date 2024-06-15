package ru.gozerov.presentation.screens.trainer.diary.diary.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.CustomService
import ru.gozerov.domain.models.ScheduleService
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainerTrainingCard

sealed interface TrainerDiaryEffect {

    object None : TrainerDiaryEffect

    data class LoadedSchedule(
        val services: List<ScheduledTraining>
    ) : TrainerDiaryEffect

    data class LoadedServices(
        val services: List<ScheduleService>
    ) : TrainerDiaryEffect

    data class LoadedTrainings(
        val trainings: Flow<PagingData<TrainerTrainingCard>>
    ) : TrainerDiaryEffect

    data class Error(
        val message: String
    ) : TrainerDiaryEffect

    data class RemovedService(
        val id: Int
    ) : TrainerDiaryEffect

}