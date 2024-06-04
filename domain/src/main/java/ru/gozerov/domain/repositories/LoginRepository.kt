package ru.gozerov.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.VKLoginResponse

interface LoginRepository {

    suspend fun loginThroughVk(): Flow<Result<VKLoginResponse>>

    suspend fun loginAsTrainee(accessToken: String, id: Long, email: String?): Flow<Result<Unit>>

    suspend fun loginAsTrainer(
        email: String,
        password: String
    ): Flow<Result<Unit>>

}