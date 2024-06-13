package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class RemoveClientPhotoUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        loginRepository.removeClientPhoto()
    }

}