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
import ru.gozerov.data.api.models.request.CreatePlanRequestBody
import ru.gozerov.data.api.models.request.CreateTrainerTrainingDTO
import ru.gozerov.data.api.models.request.ScheduleTrainingRequestBody
import ru.gozerov.data.api.models.request.SetExerciseStatusRequestBody
import ru.gozerov.data.api.models.toCreateExerciseDTO
import ru.gozerov.data.api.models.toCreateTrainingDTO
import ru.gozerov.data.api.models.toCreatedTraining
import ru.gozerov.data.api.models.toCustomTrainerTraining
import ru.gozerov.data.api.models.toCustomTraining
import ru.gozerov.data.api.models.toExercise
import ru.gozerov.data.api.models.toScheduledTraining
import ru.gozerov.data.api.models.toTraining
import ru.gozerov.data.api.paging.ExercisePagingSource
import ru.gozerov.data.api.paging.TrainerTrainingsPagingSource
import ru.gozerov.data.api.paging.TrainingPagingSource
import ru.gozerov.data.api.paging.UserTrainingPagingSource
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.data.cache.TrainingStorage
import ru.gozerov.domain.models.CreateTrainingModel
import ru.gozerov.domain.models.CustomTrainerTraining
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.models.Exercise
import ru.gozerov.domain.models.ExerciseWithWeight
import ru.gozerov.domain.models.IdResponse
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainerTrainingCard
import ru.gozerov.domain.models.Training
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.domain.repositories.LoginRepository
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(
    private val trainingApi: TrainingApi,
    private val trainingPagingSourceFactory: TrainingPagingSource.Factory,
    private val userTrainingPagingSourceFactory: UserTrainingPagingSource.Factory,
    private val trainerTrainingPagingSourceFactory: TrainerTrainingsPagingSource.Factory,
    private val exercisePagingSourceFactory: ExercisePagingSource.Factory,
    private val loginRepository: LoginRepository,
    private val loginStorage: LoginStorage,
    private val trainingStorage: TrainingStorage
) : TrainingRepository {

    private val exercises = mutableListOf<Exercise>()
    private val trainings = mutableListOf<TrainingCard>()

    private var nextTrainingId = -1

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

    override suspend fun createCustomTraining(createTrainingModel: CreateTrainingModel): Result<IdResponse> =
        runRequestSafely(
            checkToken = { loginRepository.checkToken() },
            accessTokenAction = { loginStorage.getClientAccessToken() },
            action = { token ->
                trainingApi.createCustomTraining(token, createTrainingModel.toCreateTrainingDTO())
                    .map { result -> result.toCreatedTraining() }
            }
        )

    override suspend fun getTrainingAtDate(ids: List<Int>): List<CustomTraining> =
        trainingApi.getScheduledTraining(ids).map { training ->
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
        timeEnd: String,
        exercises: List<ExerciseWithWeight>
    ): Int = runRequestSafelyNotResult(
        checkToken = { loginRepository.checkToken() },
        accessTokenAction = { loginStorage.getClientAccessToken() },
        action = { token ->
            val trainingId = trainingApi.scheduleTraining(
                token,
                ScheduleTrainingRequestBody(date, id, timeStart, timeEnd, exercises)
            ).id
            this.exercises.clear()
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

    override suspend fun removeExercise(id: Int) {
        exercises.removeIf { exercise -> exercise.id == id }
    }

    override suspend fun getAddedExercises(): List<Exercise> = exercises.toList()

    override suspend fun clearAddedExercises() {
        exercises.clear()
    }

    override suspend fun setNextTraining(id: Int?) {
        nextTrainingId = id ?: -1
    }

    override suspend fun getLastAddedTrainingId(): Int = nextTrainingId

    override suspend fun clearLastTrainingId() {
        nextTrainingId = -1
    }

    override suspend fun deleteScheduledTraining(trainingId: Int) {
        return trainingApi.deleteScheduledTraining(trainingId)
    }

    override suspend fun getTrainerTrainingById(id: Int): CustomTrainerTraining {
        return trainingApi.getTrainerTrainingById(id).toCustomTrainerTraining()
    }

    override suspend fun getTrainerTrainings(query: String): Flow<PagingData<TrainerTrainingCard>> =
        withContext(Dispatchers.IO) {
            val trainingPagingSource = trainerTrainingPagingSourceFactory.create(query)
            val pager = Pager(PagingConfig(50)) {
                trainingPagingSource
            }
            return@withContext pager.flow
        }

    override suspend fun createTrainerCustomTraining(
        createTrainingModel: CreateTrainingModel,
        wantsPublic: Boolean,
    ): IdResponse =
        runRequestSafelyNotResult(
            checkToken = { loginRepository.checkToken() },
            accessTokenAction = { loginStorage.getTrainerAccessToken() },
            action = { token ->
                trainingApi.createTrainerCustomTraining(
                    token,
                    CreateTrainerTrainingDTO(
                        createTrainingModel.description,
                        createTrainingModel.exercises.map { it.toCreateExerciseDTO() },
                        createTrainingModel.name,
                        false
                    )
                )
            }
        )

    override suspend fun addTrainingToCreating(training: TrainingCard) {
        trainings.add(training)
    }

    override suspend fun removeTrainingFromCreating(id: Int) {
        trainings.removeIf { training -> training.id == id }
    }

    override suspend fun getAddedTrainings(): List<TrainingCard> = trainings

    override suspend fun clearAddedTrainings() {
        trainings.clear()
    }

    override suspend fun createPlan(
        name: String,
        description: String,
        trainings: List<Int>,
        userId: Int
    ): Int = trainingApi.createPlanTrainer(
        userId,
        CreatePlanRequestBody(name, description, trainings)
    ).id


}