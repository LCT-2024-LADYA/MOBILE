package ru.gozerov.presentation.screens.trainer.chat.list.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.UserCard

sealed interface TrainerChatListEffect {

    object None : TrainerChatListEffect

    data class LoadedChatsAndClients(
        val chats: List<ChatCard>,
        val clientsFlow: Flow<PagingData<UserCard>>
    ) : TrainerChatListEffect

    data class LoadedChats(
        val chats: List<ChatCard>
    ) : TrainerChatListEffect

    data class LoadedClients(
        val clientsFlow: Flow<PagingData<UserCard>>
    ) : TrainerChatListEffect

    data class Error(
        val message: String
    ) : TrainerChatListEffect

}