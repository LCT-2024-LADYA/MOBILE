package ru.gozerov.presentation.screens.trainer.chat.chat.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.ChatMessage
import ru.gozerov.domain.models.TrainerService

sealed interface TrainerChatEffect {

    object None : TrainerChatEffect

    class TrainerServices(
        val services: List<TrainerService>
    ) : TrainerChatEffect

    class LoadedMessages(
        val messages: Flow<PagingData<ChatMessage>>
    ) : TrainerChatEffect

    data class Error(
        val message: String
    ) : TrainerChatEffect

}