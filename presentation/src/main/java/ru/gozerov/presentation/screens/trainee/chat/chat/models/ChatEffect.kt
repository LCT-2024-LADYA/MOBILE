package ru.gozerov.presentation.screens.trainee.chat.chat.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.ChatItem
import ru.gozerov.domain.models.TrainerService

sealed interface ChatEffect {

    object None : ChatEffect

    class LoadedMessages(
        val messages: Flow<PagingData<ChatItem>>
    ) : ChatEffect

    data class Error(
        val message: String
    ) : ChatEffect

    data class TrainerServices(
        val services: List<TrainerService>
    ) : ChatEffect

}