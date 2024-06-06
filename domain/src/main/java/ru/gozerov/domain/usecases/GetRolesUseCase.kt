package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.Role
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class GetRolesUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(): Result<List<Role>> = withContext(Dispatchers.IO) {
        return@withContext loginRepository.getRoles()
    }

}