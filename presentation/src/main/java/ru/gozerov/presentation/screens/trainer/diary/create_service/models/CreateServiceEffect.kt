package ru.gozerov.presentation.screens.trainer.diary.create_service.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.TrainerService
import ru.gozerov.domain.models.UserCard

sealed interface CreateServiceEffect {

    object None : CreateServiceEffect

    data class LoadedServices(
        val services: List<TrainerService>
    ) : CreateServiceEffect

    data class LoadedUsers(
        val users: Flow<PagingData<UserCard>>
    ) : CreateServiceEffect

    data class Error(
        val message: String
    ) : CreateServiceEffect

    object CreatedService : CreateServiceEffect

}