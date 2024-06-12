package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.ChatRepository
import javax.inject.Inject

class RunWebSocketUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        return@withContext chatRepository.runWebSocket()
    }

}