package ru.gozerov.domain.repositories

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.ChatMessage

interface ChatRepository {

    suspend fun getTrainerChats(query: String): List<ChatCard>

    suspend fun getTrainerChatMessages(userId: Int): Flow<PagingData<ChatMessage>>

    suspend fun getClientChats(query: String): List<ChatCard>

    suspend fun getClientChatMessages(trainerId: Int): Flow<PagingData<ChatMessage>>

    suspend fun runWebSocket()

    suspend fun sendMessage(to: Int, message: String, serviceId: Int?)

    suspend fun checkNewMessages(): SharedFlow<ChatMessage>

}