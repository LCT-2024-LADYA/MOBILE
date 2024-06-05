package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.TrainerInfo
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class GetTrainerInfoUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(): Result<TrainerInfo> = withContext(Dispatchers.IO) {
        return@withContext loginRepository.trainerMe()
    }

}