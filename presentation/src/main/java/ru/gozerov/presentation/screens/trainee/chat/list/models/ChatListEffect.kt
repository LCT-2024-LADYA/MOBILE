package ru.gozerov.presentation.screens.trainee.chat.list.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.Role
import ru.gozerov.domain.models.Specialization
import ru.gozerov.domain.models.TrainerCard

sealed interface ChatListEffect {

    object None : ChatListEffect

    data class LoadedChatsAndTrainers(
        val chats: List<ChatCard>,
        val trainersFlow: Flow<PagingData<TrainerCard>>
    ) : ChatListEffect

    data class LoadedRolesAndSpecializations(
        val roles: List<Role>,
        val specializations: List<Specialization>
    ) : ChatListEffect

    data class LoadedChats(
        val chats: List<ChatCard>
    ) : ChatListEffect

    data class LoadedTrainers(
        val trainersFlow: Flow<PagingData<TrainerCard>>
    ) : ChatListEffect

    data class Error(
        val message: String
    ) : ChatListEffect

}