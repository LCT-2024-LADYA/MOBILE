package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class GetTrainingsAtDateUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(ids: List<Int>): List<CustomTraining> =
        withContext(Dispatchers.IO) {
            return@withContext trainingRepository.getTrainingAtDate(ids)
        }

}