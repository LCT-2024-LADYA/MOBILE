package ru.gozerov.domain.repositories

import android.net.Uri
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.CheckTokenResult
import ru.gozerov.domain.models.ClientInfo
import ru.gozerov.domain.models.RegisterModel
import ru.gozerov.domain.models.Role
import ru.gozerov.domain.models.Specialization
import ru.gozerov.domain.models.TrainerCard
import ru.gozerov.domain.models.TrainerInfo
import ru.gozerov.domain.models.TrainerMainInfoDTO
import ru.gozerov.domain.models.UserCard

interface LoginRepository {

    suspend fun register(registerModel: RegisterModel): Flow<Result<Unit>>

    suspend fun loginAsTrainee(email: String, password: String): Flow<Result<Unit>>

    suspend fun loginAsTrainer(email: String, password: String): Flow<Result<Unit>>

    suspend fun clientMe(): Result<ClientInfo>

    suspend fun trainerMe(): Result<TrainerInfo>

    suspend fun checkToken(): CheckTokenResult

    suspend fun updateClientInfo(
        age: Int,
        email: String,
        firstName: String,
        lastName: String,
        sex: Int
    ): Result<Unit>

    suspend fun updateClientPhoto(uri: Uri): Result<Unit>

    suspend fun updateTrainerPhoto(uri: Uri): Result<Unit>

    suspend fun createTrainerService(name: String, price: Int): Result<Int>

    suspend fun deleteTrainerService(id: Int): Result<Unit>

    suspend fun createAchievement(achievement: String): Result<Int>

    suspend fun deleteAchievement(id: Int): Result<Unit>

    suspend fun getSpecializations(): Result<List<Specialization>>

    suspend fun getRoles(): Result<List<Role>>

    suspend fun updateTrainerProfile(
        trainerMainInfoDTO: TrainerMainInfoDTO,
        roles: List<Int>,
        specializations: List<Int>
    ): Flow<Result<Unit>>

    suspend fun logoutAsClient()

    suspend fun logoutAsTrainer()

    suspend fun getTrainerById(id: Int): TrainerInfo

    suspend fun getClientById(id: Int): ClientInfo

    suspend fun getTrainerCards(
        query: String,
        roles: List<Int>,
        specializations: List<Int>
    ): Flow<PagingData<TrainerCard>>

    suspend fun getClientCards(query: String): Flow<PagingData<UserCard>>

    suspend fun removeClientPhoto()

    suspend fun removeTrainerPhoto()

}