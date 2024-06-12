package ru.gozerov.presentation.screens.trainee.chat.chat.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.ChatMessage

sealed interface ChatEffect {

    object None : ChatEffect

    class LoadedMessages(
        val messages: Flow<PagingData<ChatMessage>>
    ) : ChatEffect

    data class Error(
        val message: String
    ) : ChatEffect

}