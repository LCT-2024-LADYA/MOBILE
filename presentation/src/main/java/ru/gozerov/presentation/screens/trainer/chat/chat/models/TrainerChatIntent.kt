package ru.gozerov.presentation.screens.trainer.chat.chat.models

import androidx.paging.PagingData
import ru.gozerov.domain.models.ChatItem

sealed interface TrainerChatIntent {

    object Reset : TrainerChatIntent

    object GetServices : TrainerChatIntent

    data class GetMessages(
        val userId: Int
    ) : TrainerChatIntent

    data class SaveMessages(
        val data: PagingData<ChatItem>
    ) : TrainerChatIntent

    class SendMessage(
        val to: Int,
        val message: String,
        val serviceId: Int? = null
    ) : TrainerChatIntent

    class UpdateIds(
        val trainerId: Int,
        val clientId: Int
    ) : TrainerChatIntent

    object CheckNewMessages : TrainerChatIntent

}