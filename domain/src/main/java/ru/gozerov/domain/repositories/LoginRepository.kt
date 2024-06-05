package ru.gozerov.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.CheckTokenResult
import ru.gozerov.domain.models.ClientInfo
import ru.gozerov.domain.models.RegisterModel
import ru.gozerov.domain.models.TrainerInfo

interface LoginRepository {

    suspend fun register(registerModel: RegisterModel): Flow<Result<Unit>>

    suspend fun loginAsTrainee(email: String, password: String): Flow<Result<Unit>>

    suspend fun loginAsTrainer(email: String, password: String): Flow<Result<Unit>>

    suspend fun clientMe(): Result<ClientInfo>

    suspend fun trainerMe(): Result<TrainerInfo>

    suspend fun checkToken(): CheckTokenResult

}