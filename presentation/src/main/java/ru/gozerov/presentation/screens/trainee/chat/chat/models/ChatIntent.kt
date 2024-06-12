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
        val message: String
    ) : ChatIntent

    class UpdateIds(
        val trainerId: Int,
        val clientId: Int
    ) : ChatIntent

    object CheckNewMessages : ChatIntent

}