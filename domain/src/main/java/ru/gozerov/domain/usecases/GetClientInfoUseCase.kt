package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.ClientInfo
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class GetClientInfoUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(): Result<ClientInfo> = withContext(Dispatchers.IO) {
        loginRepository.clientMe()
    }

}