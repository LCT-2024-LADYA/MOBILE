package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.ServiceRepository
import ru.gozerov.domain.utils.convertDateToUTC
import ru.gozerov.domain.utils.convertToUTC
import javax.inject.Inject

class CreateUserServiceUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository
) {

    suspend operator fun invoke(
        userId: Int,
        serviceId: Int,
        date: String,
        startTime: String,
        endTime: String
    ) = withContext(Dispatchers.IO) {
        val id = serviceRepository.createCustomService(serviceId, userId).id
        return@withContext serviceRepository.scheduleService(
            convertDateToUTC(date, startTime),
            id,
            convertToUTC(startTime),
            convertToUTC(endTime)
        )
    }

}