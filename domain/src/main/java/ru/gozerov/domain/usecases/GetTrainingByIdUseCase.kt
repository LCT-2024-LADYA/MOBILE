package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.Training
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class GetTrainingByIdUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(id: Int): Result<Training> = withContext(Dispatchers.IO) {
        return@withContext trainingRepository.getTrainingById(id)
    }

}