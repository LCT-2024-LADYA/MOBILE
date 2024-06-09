package ru.gozerov.domain.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.CreateTrainingModel
import ru.gozerov.domain.models.CreatedTraining
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.models.Exercise
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.Training
import ru.gozerov.domain.models.TrainingCard

interface TrainingRepository {

    suspend fun getSimpleTrainings(query: String? = null): Flow<PagingData<TrainingCard>>

    suspend fun getSimpleUserTraining(query: String? = null): Flow<PagingData<TrainingCard>>

    suspend fun createCustomTrainer(createTrainingModel: CreateTrainingModel): Result<CreatedTraining>

    suspend fun getTrainingAtDate(ids: List<Int>): Result<List<CustomTraining>>

    suspend fun getExercises(query: String? = null): Flow<PagingData<Exercise>>

    suspend fun getSchedule(month: Int): Result<List<ScheduledTraining>>

    suspend fun scheduleTraining(
        id: Int,
        date: String,
        timeStart: String,
        timeEnd: String
    ): Result<Int>

    suspend fun getTrainingById(id: Int): Result<Training>

    suspend fun completeExercise(trainingId: Int, exerciseId: Int)


}