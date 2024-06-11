package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class CompleteExerciseUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(trainingId: Int, exerciseId: Int): Unit =
        withContext(Dispatchers.IO) {
            return@withContext trainingRepository.completeExercise(trainingId, exerciseId)
        }

}