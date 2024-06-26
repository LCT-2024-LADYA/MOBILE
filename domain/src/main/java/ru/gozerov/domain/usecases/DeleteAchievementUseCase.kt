package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class DeleteAchievementUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext loginRepository.deleteAchievement(id)
    }

}