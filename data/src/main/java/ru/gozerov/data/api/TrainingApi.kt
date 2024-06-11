package ru.gozerov.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gozerov.data.api.models.request.SetExerciseStatusRequestBody
import ru.gozerov.data.api.models.response.CreateTrainingResponse
import ru.gozerov.data.api.models.response.CustomTrainingDTO
import ru.gozerov.data.api.models.response.GetExercisesResponse
import ru.gozerov.data.api.models.response.GetScheduleResponse
import ru.gozerov.data.api.models.response.GetTrainingsResponse
import ru.gozerov.data.api.models.response.IdResponse
import ru.gozerov.data.api.models.response.ScheduleTrainingRequestBody
import ru.gozerov.data.api.models.response.ScheduledTrainingDTO
import ru.gozerov.domain.models.CreateTrainingModel

interface TrainingApi {

    @GET("api/training")
    suspend fun getTrainings(
        @Query("search") query: String,
        @Query("cursor") page: Int
    ): GetTrainingsResponse


    @GET("api/training")
    suspend fun getTrainings(
        @Query("cursor") page: Int
    ): GetTrainingsResponse

    @POST("api/training")
    suspend fun createCustomTraining(
        @Header("access_token") token: String,
        @Body createTrainingRequestBody: CreateTrainingModel
    ): Result<CreateTrainingResponse>

    @GET("api/training/date")
    suspend fun getScheduledTraining(@Query("user_training_ids") ids: Array<Int>): List<ScheduledTrainingDTO>

    @GET("api/training/exercise")
    suspend fun getExercises(
        @Query("search") query: String,
        @Query("cursor") page: Int
    ): GetExercisesResponse

    @GET("api/training/exercise")
    suspend fun getExercises(
        @Query("cursor") page: Int
    ): GetExercisesResponse

    @GET("api/training/schedule")
    suspend fun getSchedule(
        @Header("access_token") token: String,
        @Query("month") month: Int
    ): List<GetScheduleResponse>

    @POST("api/training/schedule")
    suspend fun scheduleTraining(
        @Header("access_token") token: String,
        @Body scheduleTrainingRequestBody: ScheduleTrainingRequestBody
    ): IdResponse

    @GET("api/training/user")
    suspend fun getUserTrainings(
        @Header("access_token") token: String,
        @Query("search") query: String,
        @Query("cursor") page: Int
    ): GetTrainingsResponse

    @GET("api/training/user")
    suspend fun getUserTrainings(
        @Header("access_token") token: String,
        @Query("cursor") page: Int
    ): GetTrainingsResponse

    @GET("api/training/{training_id}")
    suspend fun getTrainingById(
        @Header("access_token") token: String,
        @Path("training_id") id: Int
    ): Result<CustomTrainingDTO>

    @PATCH("api/training/{training_id}/exercise/{exercise_id}/status")
    suspend fun setExerciseStatus(
        @Header("access_token") token: String,
        @Path("training_id") trainingId: Int,
        @Path("exercise_id") exerciseId: Int,
        @Body setExerciseStatusRequestBody: SetExerciseStatusRequestBody
    )

}