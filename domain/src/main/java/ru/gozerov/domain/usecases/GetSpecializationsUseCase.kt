package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.Specialization
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class GetSpecializationsUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(): Result<List<Specialization>> = withContext(Dispatchers.IO) {
        return@withContext loginRepository.getSpecializations()
    }

}