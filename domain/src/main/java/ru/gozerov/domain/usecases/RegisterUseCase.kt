package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.RegisterModel
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        age: Int,
        sex: Int
    ): Flow<Result<Unit>> =
        withContext(Dispatchers.IO) {
            return@withContext loginRepository.register(
                RegisterModel(
                    email,
                    password,
                    firstName,
                    lastName,
                    age,
                    sex
                )
            )
        }

}