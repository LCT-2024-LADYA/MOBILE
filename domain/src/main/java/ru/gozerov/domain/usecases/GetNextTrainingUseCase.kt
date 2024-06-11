package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.repositories.TrainingRepository
import ru.gozerov.domain.utils.addTimeToDate
import ru.gozerov.domain.utils.compareDates
import ru.gozerov.domain.utils.getCurrentUtcTime
import ru.gozerov.domain.utils.resetTimeToMidnight
import java.time.LocalDate
import javax.inject.Inject

class GetNextTrainingUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(): CustomTraining? = withContext(Dispatchers.IO) {
        val currentTime = getCurrentUtcTime()
        var nextTrainingIds = listOf<Int>()
        var postIds = listOf<Int>()
        val trainings = trainingRepository.getSchedule(LocalDate.now().monthValue)
        for (training in trainings) {
            if (nextTrainingIds.isEmpty() && compareDates(
                    resetTimeToMidnight(currentTime),
                    training.date
                ) <= 0
            ) {
                nextTrainingIds = training.ids
            } else if (nextTrainingIds.isNotEmpty()) {
                postIds = training.ids
                break
            }
        }
        if (nextTrainingIds.isEmpty())
            return@withContext null
        else {
            val lastId = trainingRepository.getLastTrainingId()
            val postTrainingsSchedule = trainingRepository.getTrainingAtDate(nextTrainingIds)

            for (training in postTrainingsSchedule) {
                val startTime = addTimeToDate(training.date, training.timeStart)
                if (compareDates(currentTime, startTime) <= 0 && lastId != training.id)
                    return@withContext training
            }
            val postTrainings = trainingRepository.getTrainingAtDate(ids = postIds)
            postTrainings.forEach { training ->
                val startTime = addTimeToDate(training.date, training.timeStart)
                if (compareDates(currentTime, startTime) <= 0 && lastId != training.id)
                    return@withContext training
            }
            return@withContext null
        }
    }

}