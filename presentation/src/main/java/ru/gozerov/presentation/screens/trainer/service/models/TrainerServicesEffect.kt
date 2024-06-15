package ru.gozerov.presentation.screens.trainer.service.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.CustomService
import ru.gozerov.domain.models.TrainerService

sealed interface TrainerServicesEffect {

    object None: TrainerServicesEffect

    data class LoadedServices(
        val services: Flow<PagingData<CustomService>>
    ) : TrainerServicesEffect

    data class DeletedService(
        val id: Int
    ) : TrainerServicesEffect

    class Error(
        val message: String
    ): TrainerServicesEffect

}