package ru.gozerov.presentation.screens.trainee.profile.services.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.ClientCustomService

sealed interface ClientServicesEffect {

    object None : ClientServicesEffect

    data class LoadedServices(
        val services: Flow<PagingData<ClientCustomService>>
    ) : ClientServicesEffect

    data class DeletedService(
        val id: Int
    ) : ClientServicesEffect

    class Error(
        val message: String
    ) : ClientServicesEffect

}