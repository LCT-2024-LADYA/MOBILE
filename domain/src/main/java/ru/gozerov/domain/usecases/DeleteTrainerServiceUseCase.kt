package ru.gozerov.domain.usecases

import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class DeleteTrainerServiceUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(id: Int): Result<Unit> {
        return loginRepository.deleteTrainerService(id)
    }

}