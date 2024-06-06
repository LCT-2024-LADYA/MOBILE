package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.TrainerMainInfoDTO
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class UpdateTrainerProfileUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(
        trainerMainInfoDTO: TrainerMainInfoDTO,
        roles: List<Int>,
        specializations: List<Int>
    ): Flow<Result<Unit>> = withContext(Dispatchers.IO) {
        return@withContext loginRepository.updateTrainerProfile(
            trainerMainInfoDTO,
            roles,
            specializations
        )
    }

}