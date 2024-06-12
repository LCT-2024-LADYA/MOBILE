package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(to: Int, message: String) = withContext(Dispatchers.IO) {
        return@withContext chatRepository.sendMessage(to, message)
    }

}