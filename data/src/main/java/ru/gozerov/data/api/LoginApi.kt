package ru.gozerov.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.gozerov.data.api.models.LoginRequestBody
import ru.gozerov.data.api.models.LoginResponse
import ru.gozerov.data.api.models.RegisterRequestBody

interface LoginApi {

    @POST("api/auth/register/user")
    suspend fun register(@Body registerRequestBody: RegisterRequestBody): Result<LoginResponse>

    @POST("api/auth/login/user")
    suspend fun loginAsTrainee(@Body loginRequestBody: LoginRequestBody): Result<LoginResponse>


    @POST("api/auth/login/trainer")
    suspend fun loginAsTrainer(@Body loginRequestBody: LoginRequestBody): Result<LoginResponse>



}