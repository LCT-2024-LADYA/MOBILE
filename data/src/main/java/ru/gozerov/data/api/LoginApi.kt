package ru.gozerov.data.api

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gozerov.data.api.models.request.LoginRequestBody
import ru.gozerov.data.api.models.request.LoginResponse
import ru.gozerov.data.api.models.request.RegisterRequestBody
import ru.gozerov.data.api.models.response.CreateAchievementRequestBody
import ru.gozerov.data.api.models.response.IdResponse
import ru.gozerov.data.api.models.response.MainInfoRequestBody
import ru.gozerov.data.api.models.response.TrainerInfoResponse
import ru.gozerov.data.api.models.response.TrainerMainInfoRequestBody
import ru.gozerov.data.api.models.response.TrainerServiceRequestBody
import ru.gozerov.data.api.models.response.UserInfoResponse
import ru.gozerov.domain.models.Role
import ru.gozerov.domain.models.Specialization

interface LoginApi {

    @POST("api/auth/register/user")
    suspend fun register(@Body registerRequestBody: RegisterRequestBody): Result<LoginResponse>

    @POST("api/auth/login/user")
    suspend fun loginAsTrainee(@Body loginRequestBody: LoginRequestBody): Result<LoginResponse>

    @GET("api/user/me")
    suspend fun getClientMe(@Header("access_token") token: String): Result<UserInfoResponse>

    @GET("api/auth/refresh")
    suspend fun refreshClientToken(@Query("refresh_token") refreshToken: String): Result<LoginResponse>

    @PUT("api/user/main")
    suspend fun updateClientMainInfo(
        @Header("access_token") token: String,
        @Body mainInfoDTO: MainInfoRequestBody
    ): Result<Unit>

    @Multipart
    @PUT("api/user/photo")
    suspend fun uploadClientPhoto(
        @Header("access_token") token: String,
        @Part photo: MultipartBody.Part?
    ): Result<Unit>

    @POST("api/auth/login/trainer")
    suspend fun loginAsTrainer(@Body loginRequestBody: LoginRequestBody): Result<LoginResponse>

    @GET("api/trainer/me")
    suspend fun getTrainerMe(@Header("access_token") token: String): Result<TrainerInfoResponse>

    @GET("api/auth/refresh")
    suspend fun refreshTrainerToken(@Query("refresh_token") refreshToken: String): Result<LoginResponse>

    @PUT("api/trainer/main")
    suspend fun updateTrainerMainInfo(
        @Header("access_token") token: String,
        @Body mainInfoDTO: TrainerMainInfoRequestBody
    ): Result<Unit>

    @Multipart
    @PUT("api/trainer/photo")
    suspend fun uploadTrainerPhoto(
        @Header("access_token") token: String,
        @Part photo: MultipartBody.Part?
    ): Result<Unit>

    @PUT("api/trainer/roles")
    suspend fun updateTrainerRoles(
        @Header("access_token") token: String,
        @Body roles: List<Int>
    ): Result<Unit>


    @POST("api/trainer/service")
    suspend fun createTrainerService(
        @Header("access_token") token: String,
        @Body service: TrainerServiceRequestBody
    ): Result<IdResponse>


    @DELETE("api/trainer/service/{service_id}")
    suspend fun deleteTrainerService(
        @Header("access_token") token: String,
        @Path("service_id") id: Int
    ): Result<Unit>


    @PUT("api/trainer/specializations")
    suspend fun updateTrainerSpecializations(
        @Header("access_token") token: String,
        @Body specializations: List<Int>
    ): Result<Unit>

    @POST("api/trainer/achievement")
    suspend fun createAchievement(
        @Header("access_token") token: String,
        @Body achievement: CreateAchievementRequestBody
    ): Result<IdResponse>

    @DELETE("api/trainer/achievement/{achievement_id}")
    suspend fun deleteAchievement(
        @Header("access_token") token: String,
        @Path("achievement_id") id: Int
    ): Result<Unit>

    @GET("api/role")
    suspend fun getRoles(): Result<List<Role>>

    @GET("api/specialization")
    suspend fun getSpecializations(): Result<List<Specialization>>


}