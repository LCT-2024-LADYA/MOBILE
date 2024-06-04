package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.VKLoginResponse
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginThroughVkUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(): Flow<Result<VKLoginResponse>> = withContext(Dispatchers.IO) {
        return@withContext loginRepository.loginThroughVk()
    }

}