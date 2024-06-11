package ru.gozerov.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.gozerov.data.api.TrainingApi
import ru.gozerov.data.api.models.request.SetExerciseStatusRequestBody
import ru.gozerov.data.api.models.response.ScheduleTrainingRequestBody
import ru.gozerov.data.api.models.toCreatedTraining
import ru.gozerov.data.api.models.toCustomTraining
import ru.gozerov.data.api.models.toExercise
import ru.gozerov.data.api.models.toScheduledTraining
import ru.gozerov.data.api.models.toTraining
import ru.gozerov.data.api.paging.ExercisePagingSource
import ru.gozerov.data.api.paging.TrainingPagingSource
import ru.gozerov.data.api.paging.UserTrainingPagingSource
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.data.cache.TrainingStorage
import ru.gozerov.domain.models.CreateTrainingModel
import ru.gozerov.domain.models.CreatedTraining
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.models.Exercise
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.Training
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.domain.repositories.LoginRepository
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val trainingApi: TrainingApi,
    private val trainingPagingSourceFactory: TrainingPagingSource.Factory,
    private val userTrainingPagingSourceFactory: UserTrainingPagingSource.Factory,
    private val exercisePagingSourceFactory: ExercisePagingSource.Factory,
    private val loginRepository: LoginRepository,
    private val loginStorage: LoginStorage,
    private val trainingStorage: TrainingStorage
) : TrainingRepository {

    private val exercises = mutableListOf<Exercise>()

    override suspend fun getSimpleTrainings(query: String?): Flow<PagingData<TrainingCard>> =
        withContext(Dispatchers.IO) {
            val trainingPagingSource = trainingPagingSourceFactory.create(query)
            val pager = Pager(PagingConfig(50)) {
                trainingPagingSource
            }
            return@withContext pager.flow
        }

    override suspend fun getSimpleUserTraining(query: String?): Flow<PagingData<TrainingCard>> =
        withContext(Dispatchers.IO) {
            val trainingPagingSource = userTrainingPagingSourceFactory.create(query)
            val pager = Pager(PagingConfig(50)) {
                trainingPagingSource
            }
            return@withContext pager.flow
        }

    override suspend fun createCustomTraining(createTrainingModel: CreateTrainingModel): Result<CreatedTraining> =
        runRequestSafely(
            checkToken = { loginRepository.checkToken() },
            accessTokenAction = { loginStorage.getClientAccessToken() },
            action = { token ->
                trainingApi.createCustomTraining(token, createTrainingModel)
                    .map { result -> result.toCreatedTraining() }
            }
        )

    override suspend fun getTrainingAtDate(ids: List<Int>): List<CustomTraining> =
        trainingApi.getScheduledTraining(ids.toTypedArray()).map { training ->
            training.toCustomTraining()
        }


    override suspend fun getExercises(query: String?): Flow<PagingData<Exercise>> =
        withContext(Dispatchers.IO) {
            val exercisePagingSource = exercisePagingSourceFactory.create(query)
            val pager = Pager(PagingConfig(50)) {
                exercisePagingSource
            }
            return@withContext pager.flow.map { data ->
                data.map { exerciseDTO -> exerciseDTO.toExercise() }
            }
        }

    override suspend fun getSchedule(month: Int): List<ScheduledTraining> =
        runRequestSafelyNotResult(
            checkToken = { loginRepository.checkToken() },
            accessTokenAction = { loginStorage.getClientAccessToken() },
            action = { token ->
                trainingApi.getSchedule(token, month).map { response ->
                    response.toScheduledTraining()
                }
            }
        )

    override suspend fun scheduleTraining(
        id: Int,
        date: String,
        timeStart: String,
        timeEnd: String
    ): Int = runRequestSafelyNotResult(
        checkToken = { loginRepository.checkToken() },
        accessTokenAction = { loginStorage.getClientAccessToken() },
        action = { token ->
            val trainingId = trainingApi.scheduleTraining(
                token,
                ScheduleTrainingRequestBody(date, id, timeStart, timeEnd)
            ).id
            exercises.clear()
            return@runRequestSafelyNotResult trainingId
        }
    )

    override suspend fun getTrainingById(id: Int): Result<Training> = runRequestSafely(
        checkToken = { loginRepository.checkToken() },
        accessTokenAction = { loginStorage.getClientAccessToken() },
        action = { token ->
            trainingApi.getTrainingById(token, id).map { dto ->
                dto.toTraining()
            }
        }
    )

    override suspend fun completeExercise(trainingId: Int, exerciseId: Int) =
        runRequestSafelyNotResult(
            checkToken = { loginRepository.checkToken() },
            accessTokenAction = { loginStorage.getClientAccessToken() },
            action = { token ->
                trainingApi.setExerciseStatus(
                    token,
                    trainingId,
                    exerciseId,
                    SetExerciseStatusRequestBody()
                )
            }
        )

    override suspend fun setLastTrainingId(id: Int) {
        trainingStorage.saveLastTrainingId(id)
    }

    override suspend fun getLastTrainingId(): Int? {
        val id = trainingStorage.getLastTrainingId()
        return if (id == -1) null else id
    }

    override suspend fun addExerciseToCreating(exercise: Exercise) {
        exercises.add(exercise)
    }

    override suspend fun getAddedExercises(): List<Exercise> = exercises.toList()

}