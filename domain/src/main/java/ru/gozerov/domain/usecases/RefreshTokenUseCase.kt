package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.CheckTokenResult
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(): CheckTokenResult = withContext(Dispatchers.IO) {
        return@withContext loginRepository.checkToken()
    }

}