package ru.gozerov.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.gozerov.data.api.models.LoginRequestBody
import ru.gozerov.data.api.models.LoginResponse

interface LoginApi {

    @POST("api/auth/vk")
    suspend fun loginAsTrainee(@Body loginRequestBody: LoginRequestBody): Result<LoginResponse>


    @GET("api/huiohuo")
    suspend fun loginAsTrainer(): Result<LoginResponse>



}