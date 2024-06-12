package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.CustomExercise
import ru.gozerov.domain.models.toExercise
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class AddExercisesToCreatingUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(exercises: List<CustomExercise>) = withContext(Dispatchers.IO) {
        exercises.forEach { exercise ->
            trainingRepository.addExerciseToCreating(exercise.toExercise())
        }
        return@withContext
    }

}