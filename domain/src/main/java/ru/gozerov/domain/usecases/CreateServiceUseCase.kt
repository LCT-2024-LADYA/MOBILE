package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class CreateServiceUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(name: String, price: Int): Result<Int> =
        withContext(Dispatchers.IO) {
            return@withContext loginRepository.createTrainerService(name, price)
        }

}
