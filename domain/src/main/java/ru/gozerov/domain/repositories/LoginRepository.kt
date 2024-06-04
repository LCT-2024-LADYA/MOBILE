package ru.gozerov.domain.repositories

import androidx.activity.ComponentActivity
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun loginAsTrainee(accessToken: String, id: Long, email: String?): Flow<Result<Unit>>

    suspend fun loginAsTrainer(
        email: String,
        password: String
    ): Flow<Result<Unit>>

}