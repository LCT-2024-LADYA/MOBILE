package ru.gozerov.presentation.screens.trainee.chat.chat.models

import androidx.paging.PagingData
import ru.gozerov.domain.models.ChatMessage

sealed interface ChatIntent {

    object Reset : ChatIntent

    data class SaveMessages(
        val data: PagingData<ChatMessage>
    ) : ChatIntent

    data class GetMessages(
        val trainerId: Int
    ) : ChatIntent

    class SendMessage(
        val to: Int,
        val message: String,
        val serviceId: Int? = null
    ) : ChatIntent

    class UpdateIds(
        val trainerId: Int,
        val clientId: Int
    ) : ChatIntent

    class GetTrainerServices(
        val trainerId: Int
    ) : ChatIntent

    object CheckNewMessages : ChatIntent

}