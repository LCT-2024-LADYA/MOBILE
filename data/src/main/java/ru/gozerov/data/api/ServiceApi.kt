package ru.gozerov.data.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gozerov.data.api.models.request.CreateCustomServiceRequestBody
import ru.gozerov.data.api.models.request.ScheduleServiceRequestBody
import ru.gozerov.data.api.models.request.ServiceStatus
import ru.gozerov.data.api.models.response.CustomServiceDTO
import ru.gozerov.data.api.models.response.GetClientServicesPagination
import ru.gozerov.data.api.models.response.GetScheduleResponse
import ru.gozerov.data.api.models.response.GetServicesPagination
import ru.gozerov.data.api.models.response.ScheduledServiceDTO
import ru.gozerov.domain.models.IdResponse
import ru.gozerov.domain.models.TrainerService

interface ServiceApi {

    @POST("api/service")
    suspend fun createCustomService(
        @Header("access_token") token: String,
        @Body body: CreateCustomServiceRequestBody
    ): IdResponse

    @GET("api/service/schedule")
    suspend fun getScheduledServices(@Query("schedule_ids") ids: List<Int>): List<ScheduledServiceDTO>

    @POST("api/service/schedule")
    suspend fun scheduleService(@Body scheduleServiceRequestBody: ScheduleServiceRequestBody): IdResponse

    @GET("api/service/schedule/{month}")
    suspend fun getSchedule(
        @Header("access_token") token: String,
        @Path("month") month: Int
    ): List<GetScheduleResponse>

    @DELETE("api/service/schedule/{schedule_id}")
    suspend fun deleteScheduledService(@Path("schedule_id") id: Int)

    @PUT("api/service/status/{service_id}")
    suspend fun updateServiceStatus(@Path("service_id") serviceId: Int, @Body status: ServiceStatus)

    @GET("api/service/trainer")
    suspend fun getTrainerServices(
        @Header("access_token") token: String,
        @Query("cursor") cursor: Int
    ): GetServicesPagination

    @GET("api/service/user")
    suspend fun getUserServices(
        @Header("access_token") token: String,
        @Query("cursor") cursor: Int
    ): GetClientServicesPagination

    @GET("api/service/{id}")
    suspend fun getServiceById(@Path("id") serviceId: Int): TrainerService

    @DELETE("api/service/{service_id}")
    suspend fun deleteService(@Path("service_id") id: Int)

}