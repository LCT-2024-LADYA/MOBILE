package ru.gozerov.data.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gozerov.data.api.models.request.CreatePlanRequestBody
import ru.gozerov.data.api.models.request.CreateTrainerTrainingDTO
import ru.gozerov.data.api.models.request.CreateTrainingDTO
import ru.gozerov.data.api.models.request.ScheduleTrainingRequestBody
import ru.gozerov.data.api.models.request.SetExerciseStatusRequestBody
import ru.gozerov.data.api.models.response.CreateTrainingResponse
import ru.gozerov.data.api.models.response.CustomTrainerTrainingDTO
import ru.gozerov.data.api.models.response.CustomTrainingDTO
import ru.gozerov.data.api.models.response.GetExercisesResponse
import ru.gozerov.data.api.models.response.GetProgressResponse
import ru.gozerov.data.api.models.response.GetScheduleResponse
import ru.gozerov.data.api.models.response.GetTrainerTrainingsResponse
import ru.gozerov.data.api.models.response.GetTrainingsResponse
import ru.gozerov.data.api.models.response.ScheduledTrainingDTO
import ru.gozerov.domain.models.IdResponse
import ru.gozerov.domain.models.TrainingPlan
import ru.gozerov.domain.models.TrainingPlanCard

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
        @Body createTrainingRequestBody: CreateTrainingDTO
    ): Result<CreateTrainingResponse>

    @POST("api/training/trainer")
    suspend fun createTrainerCustomTraining(
        @Header("access_token") token: String,
        @Body createTrainingRequestBody: CreateTrainerTrainingDTO
    ): IdResponse

    @GET("api/training/date")
    suspend fun getScheduledTraining(@Query("user_training_ids") ids: List<Int>): List<ScheduledTrainingDTO>

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


    @GET("api/training/{training_id}/trainer")
    suspend fun getTrainerTrainingById(
        @Path("training_id") id: Int
    ): CustomTrainerTrainingDTO

    @GET("api/training/trainer")
    suspend fun getTrainerTrainings(
        @Header("access_token") token: String,
        @Query("search") query: String,
        @Query("cursor") page: Int
    ): GetTrainerTrainingsResponse

    @PATCH("api/training/{training_id}/exercise/{exercise_id}/status")
    suspend fun setExerciseStatus(
        @Header("access_token") token: String,
        @Path("training_id") trainingId: Int,
        @Path("exercise_id") exerciseId: Int,
        @Body setExerciseStatusRequestBody: SetExerciseStatusRequestBody
    )

    @DELETE("api/training/schedule/{user_training_id}")
    suspend fun deleteScheduledTraining(@Path("user_training_id") trainingId: Int)

    @POST("api/training/plan/user/{user_id}")
    suspend fun createPlanTrainer(
        @Path("user_id") userId: Int,
        @Body createPlanRequestBody: CreatePlanRequestBody
    ): IdResponse

    @GET("api/training/plan/{plan_id}")
    suspend fun getPlan(@Path("plan_id") planId: Int): TrainingPlan

    @GET("api/training/plan/user")
    suspend fun getPlanCovers(@Header("access_token") token: String): List<TrainingPlanCard>

    @GET("api/training/progress")
    suspend fun getProgress(
        @Header("access_token") token: String,
        @Query("search") query: String,
        @Query("date_start") dateStart: String,
        @Query("date_end") dateEnd: String,
        @Query("page") page: Int
    ): GetProgressResponse

}