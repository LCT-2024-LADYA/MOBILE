package ru.gozerov.presentation.screens.trainer.chat.chat.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.ChatMessage

sealed interface TrainerChatEffect {

    object None : TrainerChatEffect

    class LoadedMessages(
        val messages: Flow<PagingData<ChatMessage>>
    ) : TrainerChatEffect

    data class Error(
        val message: String
    ) : TrainerChatEffect

}