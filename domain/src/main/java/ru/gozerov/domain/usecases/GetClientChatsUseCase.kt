package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.repositories.ChatRepository
import javax.inject.Inject

class GetClientChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(): List<ChatCard> = withContext(Dispatchers.IO) {
        return@withContext chatRepository.getClientChats()
    }

}