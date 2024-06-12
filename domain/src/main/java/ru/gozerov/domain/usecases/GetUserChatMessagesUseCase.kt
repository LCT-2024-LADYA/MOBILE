package ru.gozerov.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.ChatMessage
import ru.gozerov.domain.repositories.ChatRepository
import javax.inject.Inject

class GetUserChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(trainerId: Int): Flow<PagingData<ChatMessage>> =
        withContext(Dispatchers.IO) {
            return@withContext chatRepository.getClientChatMessages(trainerId)
        }

}