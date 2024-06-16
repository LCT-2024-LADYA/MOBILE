package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.ChatItem
import ru.gozerov.domain.repositories.ChatRepository
import javax.inject.Inject

class CheckNewMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(): SharedFlow<ChatItem.ChatMessage> = withContext(Dispatchers.IO) {
        return@withContext chatRepository.checkNewMessages()
    }

}