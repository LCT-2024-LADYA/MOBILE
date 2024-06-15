package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.CustomExercise
import ru.gozerov.domain.models.ExerciseWithWeight
import ru.gozerov.domain.repositories.TrainingRepository
import ru.gozerov.domain.utils.convertDateToUTC
import ru.gozerov.domain.utils.convertToUTC
import javax.inject.Inject

class ScheduleTrainingUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(
        id: Int,
        date: String,
        timeStart: String,
        timeEnd: String,
        exercises: List<CustomExercise>
    ) = withContext(Dispatchers.IO) {
        return@withContext trainingRepository.scheduleTraining(
            id,
            convertDateToUTC(date, timeStart),
            convertToUTC(timeStart),
            convertToUTC(timeEnd),
            exercises.map { exercise ->
                ExerciseWithWeight(exercise.id, exercise.reps, exercise.sets, exercise.weight)
            }
        )
    }

}