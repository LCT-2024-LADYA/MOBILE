package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.Exercise
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class GetAddedExercisesUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(): List<Exercise> = withContext(Dispatchers.IO) {
        return@withContext trainingRepository.getAddedExercises()
    }

}