package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.CreateTrainingModel
import ru.gozerov.domain.repositories.TrainingRepository
import ru.gozerov.domain.utils.convertDateToUTC
import ru.gozerov.domain.utils.convertToUTC
import javax.inject.Inject

class CreateTrainingUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(
        createTrainingModel: CreateTrainingModel,
        date: String,
        startTime: String,
        endTime: String
    ) = withContext(Dispatchers.IO) {
        val result = trainingRepository.createCustomTraining(createTrainingModel)
        return@withContext result.map { createdTraining ->
            trainingRepository.scheduleTraining(
                createdTraining.id,
                convertDateToUTC(date, startTime),
                convertToUTC(startTime),
                convertToUTC(endTime)
            )
        }
    }

}