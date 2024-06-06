package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class UpdateClientInfoUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(
        age: Int,
        email: String,
        firstName: String,
        lastName: String,
        sex: Int
    ): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext loginRepository.updateClientInfo(age, email, firstName, lastName, sex)
    }

}