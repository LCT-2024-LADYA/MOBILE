package ru.gozerov.domain.repositories

import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun register(email: String, password: String): Flow<Result<Unit>>

    suspend fun loginAsTrainee(email: String, password: String): Flow<Result<Unit>>

    suspend fun loginAsTrainer(email: String, password: String): Flow<Result<Unit>>

}