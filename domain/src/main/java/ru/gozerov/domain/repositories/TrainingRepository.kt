package ru.gozerov.domain.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.CreateTrainingModel
import ru.gozerov.domain.models.CustomTrainerTraining
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.models.Exercise
import ru.gozerov.domain.models.ExerciseWithWeight
import ru.gozerov.domain.models.IdResponse
import ru.gozerov.domain.models.ProgressCard
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainerTrainingCard
import ru.gozerov.domain.models.Training
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.domain.models.TrainingPlan
import ru.gozerov.domain.models.TrainingPlanCard

interface TrainingRepository {

    suspend fun getSimpleTrainings(query: String? = null): Flow<PagingData<TrainingCard>>

    suspend fun getSimpleUserTraining(query: String? = null): Flow<PagingData<TrainingCard>>

    suspend fun createCustomTraining(createTrainingModel: CreateTrainingModel): Result<IdResponse>

    suspend fun getTrainingAtDate(ids: List<Int>): List<CustomTraining>

    suspend fun getExercises(query: String? = null): Flow<PagingData<Exercise>>

    suspend fun getSchedule(month: Int): List<ScheduledTraining>

    suspend fun scheduleTraining(
        id: Int,
        date: String,
        timeStart: String,
        timeEnd: String,
        exercises: List<ExerciseWithWeight>
    ): Int

    suspend fun getTrainingById(id: Int): Result<Training>

    suspend fun completeExercise(trainingId: Int, exerciseId: Int)

    suspend fun setLastTrainingId(id: Int)

    suspend fun getLastTrainingId(): Int?

    suspend fun addExerciseToCreating(exercise: Exercise)

    suspend fun removeExercise(id: Int)

    suspend fun getAddedExercises(): List<Exercise>

    suspend fun clearAddedExercises()

    suspend fun setNextTraining(id: Int?)

    suspend fun getLastAddedTrainingId(): Int

    suspend fun clearLastTrainingId()

    suspend fun deleteScheduledTraining(trainingId: Int)

    suspend fun getTrainerTrainingById(id: Int): CustomTrainerTraining

    suspend fun getTrainerTrainings(query: String): Flow<PagingData<TrainerTrainingCard>>

    suspend fun createTrainerCustomTraining(
        createTrainingModel: CreateTrainingModel,
        wantsPublic: Boolean
    ): IdResponse

    suspend fun addTrainingToCreating(training: TrainingCard)

    suspend fun removeTrainingFromCreating(id: Int)

    suspend fun getAddedTrainings(): List<TrainingCard>

    suspend fun clearAddedTrainings()

    suspend fun createPlan(
        name: String,
        description: String,
        trainings: List<Int>,
        userId: Int
    ): Int

    suspend fun getUserPlans(): List<TrainingPlanCard>

    suspend fun getPlan(id: Int): TrainingPlan

    suspend fun getProgress(
        query: String,
        dateStart: String,
        dateEnd: String
    ): Flow<PagingData<ProgressCard>>

}