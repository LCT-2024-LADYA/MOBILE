package ru.gozerov.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.UserCard
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class GetClientCardsUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(query: String): Flow<PagingData<UserCard>> =
        withContext(Dispatchers.IO) {
            return@withContext loginRepository.getClientCards(query)
        }

}