package ru.gozerov.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import ru.gozerov.data.api.models.request.LoginRequestBody
import ru.gozerov.data.api.models.request.LoginResponse
import ru.gozerov.data.api.models.request.RegisterRequestBody
import ru.gozerov.data.api.models.response.TrainerInfoResponse
import ru.gozerov.data.api.models.response.UserInfoResponse

interface LoginApi {

    @POST("api/auth/register/user")
    suspend fun register(@Body registerRequestBody: RegisterRequestBody): Result<LoginResponse>

    @POST("api/auth/login/user")
    suspend fun loginAsTrainee(@Body loginRequestBody: LoginRequestBody): Result<LoginResponse>

    @GET("api/user/me")
    suspend fun getClientMe(@Header("access_token") token: String): Result<UserInfoResponse>

    @GET("api/auth/refresh")
    suspend fun refreshClientToken(@Query("refresh_token") refreshToken: String): Result<LoginResponse>

    @POST("api/auth/login/trainer")
    suspend fun loginAsTrainer(@Body loginRequestBody: LoginRequestBody): Result<LoginResponse>

    @GET("api/trainer/me")
    suspend fun getTrainerMe(@Header("access_token") token: String): Result<TrainerInfoResponse>

    @GET("api/auth/refresh")
    suspend fun refreshTrainerToken(@Query("refresh_token") refreshToken: String): Result<LoginResponse>

}