package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.CreateTrainingModel
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class CreateTrainerTrainingUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(createTrainingModel: CreateTrainingModel, wantsPublic: Boolean) =
        withContext(Dispatchers.IO) {
            return@withContext trainingRepository.createTrainerCustomTraining(
                createTrainingModel,
                wantsPublic
            )
        }

}