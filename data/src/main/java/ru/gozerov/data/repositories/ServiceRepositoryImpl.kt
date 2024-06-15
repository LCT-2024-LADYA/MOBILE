package ru.gozerov.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.gozerov.data.api.ServiceApi
import ru.gozerov.data.api.models.request.CreateCustomServiceRequestBody
import ru.gozerov.data.api.models.request.ScheduleServiceRequestBody
import ru.gozerov.data.api.models.request.ServiceStatus
import ru.gozerov.data.api.models.toCustomService
import ru.gozerov.data.api.models.toScheduledService
import ru.gozerov.data.api.models.toScheduledTraining
import ru.gozerov.data.api.paging.TrainerServicePagingSource
import ru.gozerov.data.api.paging.UserServicePagingSource
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.domain.models.CustomService
import ru.gozerov.domain.models.IdResponse
import ru.gozerov.domain.models.ScheduleService
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainerService
import ru.gozerov.domain.repositories.LoginRepository
import ru.gozerov.domain.repositories.ServiceRepository
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val serviceApi: ServiceApi,
    private val loginStorage: LoginStorage,
    private val loginRepository: LoginRepository,
    private val trainerServicePagingSource: TrainerServicePagingSource,
    private val userServicePagingSource: UserServicePagingSource
) : ServiceRepository {

    override suspend fun createCustomService(serviceId: Int, userId: Int): IdResponse =
        runRequestSafelyNotResult(
            checkToken = { loginRepository.checkToken() },
            accessTokenAction = { loginStorage.getTrainerAccessToken() },
            action = { token ->
                serviceApi.createCustomService(
                    token,
                    CreateCustomServiceRequestBody(serviceId, userId)
                )
            }
        )

    override suspend fun getScheduledServices(ids: List<Int>): List<ScheduleService> =
        serviceApi.getScheduledServices(ids).map { service ->
            service.toScheduledService()
        }

    override suspend fun scheduleService(
        date: String,
        id: Int,
        timeStart: String,
        timeEnd: String
    ): IdResponse =
        serviceApi.scheduleService(ScheduleServiceRequestBody(date, id, timeStart, timeEnd))

    override suspend fun getSchedule(month: Int): List<ScheduledTraining> =
        runRequestSafelyNotResult(
            checkToken = { loginRepository.checkToken() },
            accessTokenAction = { loginStorage.getTrainerAccessToken() },
            action = { token ->
                serviceApi.getSchedule(token, month)
                    .map { response -> response.toScheduledTraining() }
            }
        )

    override suspend fun deleteScheduledService(id: Int) = serviceApi.deleteScheduledService(id)

    override suspend fun updateServiceStatus(serviceId: Int, status: Boolean, type: Int) =
        serviceApi.updateServiceStatus(serviceId, ServiceStatus(status, type))

    override suspend fun getTrainerServices(): Flow<PagingData<CustomService>> =
        withContext(Dispatchers.IO) {
            val pager = Pager(PagingConfig(50)) {
                trainerServicePagingSource
            }
            return@withContext pager.flow.map { data ->
                data.map { service -> service.toCustomService() }
            }
        }


    override suspend fun getUserServices(): Flow<PagingData<CustomService>> =
        withContext(Dispatchers.IO) {
            val pager = Pager(PagingConfig(50)) {
                userServicePagingSource
            }
            return@withContext pager.flow.map { data ->
                data.map { service -> service.toCustomService() }
            }
        }

    override suspend fun getServiceById(serviceId: Int): TrainerService =
        serviceApi.getServiceById(serviceId)

    override suspend fun deleteService(id: Int) {
        return serviceApi.deleteService(id)
    }

}