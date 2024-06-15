package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class SetNextTrainingUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(id: Int?) = withContext(Dispatchers.IO) {
        return@withContext trainingRepository.setNextTraining(id)
    }

}