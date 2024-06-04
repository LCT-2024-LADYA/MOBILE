package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginAsTrainerUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(email: String, password: String): Flow<Result<Unit>> =
        withContext(Dispatchers.IO) {
            return@withContext loginRepository.loginAsTrainer(email, password)
        }

}