package ru.gozerov.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.TrainerCard
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class GetTrainerCardsUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(
        query: String,
        roles: List<Int>,
        specializations: List<Int>
    ): Flow<PagingData<TrainerCard>> =
        withContext(Dispatchers.IO) {
            return@withContext loginRepository.getTrainerCards(query, roles, specializations)
        }

}