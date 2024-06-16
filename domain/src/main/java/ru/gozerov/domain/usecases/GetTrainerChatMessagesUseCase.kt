package ru.gozerov.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.ChatItem
import ru.gozerov.domain.repositories.ChatRepository
import javax.inject.Inject

class GetTrainerChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(clientId: Int): Flow<PagingData<ChatItem>> =
        withContext(Dispatchers.IO) {
            return@withContext chatRepository.getTrainerChatMessages(clientId)
        }

}