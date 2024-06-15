package ru.gozerov.domain.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.CustomService
import ru.gozerov.domain.models.IdResponse
import ru.gozerov.domain.models.ScheduleService
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainerService

interface ServiceRepository {

    suspend fun createCustomService(serviceId: Int, userId: Int): IdResponse

    suspend fun getScheduledServices(ids: List<Int>): List<ScheduleService>

    suspend fun scheduleService(
        date: String,
        id: Int,
        timeStart: String,
        timeEnd: String
    ): IdResponse

    suspend fun getSchedule(
        month: Int
    ): List<ScheduledTraining>

    suspend fun deleteScheduledService(id: Int)

    suspend fun updateServiceStatus(serviceId: Int, status: Boolean, type: Int)

    suspend fun getTrainerServices(): Flow<PagingData<CustomService>>

    suspend fun getUserServices(): Flow<PagingData<CustomService>>

    suspend fun getServiceById(serviceId: Int): TrainerService

    suspend fun deleteService(id: Int)

}