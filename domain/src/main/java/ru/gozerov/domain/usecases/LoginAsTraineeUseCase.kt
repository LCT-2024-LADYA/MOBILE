package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginAsTraineeUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(accessToken: String, id: Long, email: String?): Flow<Result<Unit>> =
        withContext(Dispatchers.IO) {
            loginRepository.loginAsTrainee(accessToken, id, email)
        }

}