package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(month: Int): List<ScheduledTraining> = withContext(Dispatchers.IO) {
        return@withContext trainingRepository.getSchedule(month)
    }

}