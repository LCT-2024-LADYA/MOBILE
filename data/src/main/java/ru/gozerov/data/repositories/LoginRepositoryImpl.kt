package ru.gozerov.data.repositories

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import ru.gozerov.data.api.LoginApi
import ru.gozerov.data.api.models.request.LoginRequestBody
import ru.gozerov.data.api.models.response.CreateAchievementRequestBody
import ru.gozerov.data.api.models.response.MainInfoRequestBody
import ru.gozerov.data.api.models.response.TrainerServiceRequestBody
import ru.gozerov.data.api.models.toClientInfo
import ru.gozerov.data.api.models.toRegisterRequestBody
import ru.gozerov.data.api.models.toTrainerCard
import ru.gozerov.data.api.models.toTrainerInfo
import ru.gozerov.data.api.models.toTrainerMainInfoRequestBody
import ru.gozerov.data.api.models.toUserCard
import ru.gozerov.data.api.paging.ClientCoversPagingSource
import ru.gozerov.data.api.paging.TrainerCoversPagingSource
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.domain.models.CheckTokenResult
import ru.gozerov.domain.models.ClientInfo
import ru.gozerov.domain.models.RegisterModel
import ru.gozerov.domain.models.Role
import ru.gozerov.domain.models.Specialization
import ru.gozerov.domain.models.TrainerCard
import ru.gozerov.domain.models.TrainerInfo
import ru.gozerov.domain.models.TrainerMainInfoDTO
import ru.gozerov.domain.models.UserCard
import ru.gozerov.domain.repositories.LoginRepository
import java.io.InputStream
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi,
    private val loginStorage: LoginStorage,
    @ApplicationContext private val context: Context,
    private val trainerCoversPagingSourceFactory: TrainerCoversPagingSource.Factory,
    private val clientCoversPagingSourceFactory: ClientCoversPagingSource.Factory
) : LoginRepository {
    override suspend fun register(registerModel: RegisterModel): Flow<Result<Unit>> = flow {
        loginApi.register(registerModel.toRegisterRequestBody())
            .onSuccess { response ->
                loginStorage.saveClientTokens(response.access_token, response.refresh_token)
                emit(Result.success(Unit))
            }
            .onFailure {
                emit(Result.failure(IllegalStateException()))
            }
    }

    override suspend fun loginAsTrainee(email: String, password: String): Flow<Result<Unit>> =
        flow {
            loginApi.loginAsTrainee(LoginRequestBody(email, password))
                .onSuccess { response ->
                    loginStorage.saveClientTokens(response.access_token, response.refresh_token)
                    emit(Result.success(Unit))
                }
                .onFailure {
                    emit(Result.failure(IllegalStateException()))
                }
        }


    override suspend fun loginAsTrainer(
        email: String,
        password: String
    ): Flow<Result<Unit>> = flow {
        val response = loginApi.loginAsTrainer(LoginRequestBody(email, password))

        response
            .onSuccess { loginResponse ->
                loginStorage.saveTrainerTokens(
                    loginResponse.access_token,
                    loginResponse.refresh_token
                )
                emit(Result.success(Unit))
            }
            .onFailure { throwable ->
                emit(Result.failure(throwable))
            }
    }

    override suspend fun clientMe(): Result<ClientInfo> = runRequestSafely(
        checkToken = { checkToken() },
        accessTokenAction = { loginStorage.getClientAccessToken() },
        action = { token ->
            loginApi.getClientMe(token).map { response ->
                response.toClientInfo()
            }
        }
    )

    override suspend fun trainerMe(): Result<TrainerInfo> = runRequestSafely(
        checkToken = { checkToken() },
        accessTokenAction = { loginStorage.getTrainerAccessToken() },
        action = { token ->
            loginApi.getTrainerMe(token).map { response ->
                response.toTrainerInfo()
            }
        }
    )

    override suspend fun checkToken(): CheckTokenResult {
        val userType = loginStorage.getRole()
        if (userType == 0) return CheckTokenResult(isAuth = false, isClient = false)
        if (userType == 1) {
            val token = loginStorage.getClientAccessToken() ?: return CheckTokenResult(
                isAuth = false,
                isClient = false
            )
            loginApi.getClientMe(token)
                .onSuccess {
                    return CheckTokenResult(isAuth = true, isClient = true)
                }
                .onFailure { throwable ->
                    if ((throwable as? HttpException)?.code() == 400) {
                        val refreshToken = loginStorage.getClientRefreshToken()
                            ?: return CheckTokenResult(isAuth = false, isClient = false)
                        val response = loginApi.refreshClientToken(refreshToken)

                        response
                            .onSuccess { loginResponse ->
                                loginStorage.saveClientTokens(
                                    loginResponse.access_token,
                                    loginResponse.refresh_token
                                )
                                return CheckTokenResult(isAuth = true, isClient = true)
                            }
                            .onFailure {
                                return CheckTokenResult(isAuth = false, isClient = false)
                            }
                    } else
                        throw IllegalStateException()
                }
        } else if (userType == 2) {
            val token = loginStorage.getTrainerAccessToken() ?: return CheckTokenResult(
                isAuth = false,
                isClient = false
            )
            loginApi.getTrainerMe(token)
                .onSuccess {
                    return CheckTokenResult(isAuth = true, isClient = false)
                }
                .onFailure { throwable ->
                    if ((throwable as? HttpException)?.code() == 400) {
                        val refreshToken = loginStorage.getTrainerRefreshToken()
                            ?: return CheckTokenResult(isAuth = false, isClient = false)
                        val response = loginApi.refreshTrainerToken(refreshToken)

                        response
                            .onSuccess { loginResponse ->
                                loginStorage.saveTrainerTokens(
                                    loginResponse.access_token,
                                    loginResponse.refresh_token
                                )
                                return CheckTokenResult(isAuth = true, isClient = false)
                            }
                            .onFailure {
                                return CheckTokenResult(isAuth = false, isClient = false)
                            }
                    } else
                        throw IllegalStateException()
                }
        }

        throw IllegalArgumentException("Unknown userType")
    }

    override suspend fun updateClientInfo(
        age: Int,
        email: String,
        firstName: String,
        lastName: String,
        sex: Int
    ): Result<Unit> = runRequestSafely(
        checkToken = { checkToken() },
        accessTokenAction = { loginStorage.getClientAccessToken() },
        action = { token ->
            val dto = MainInfoRequestBody(age, email, firstName, lastName, sex)
            loginApi.updateClientMainInfo(token, dto)
        }
    )

    override suspend fun updateClientPhoto(uri: Uri): Result<Unit> = runRequestSafely(
        checkToken = { checkToken() },
        accessTokenAction = { loginStorage.getClientAccessToken() },
        action = { token ->
            val part = getImagePart(uri)
            loginApi.uploadClientPhoto(token, part)
        }
    )

    override suspend fun updateTrainerPhoto(uri: Uri): Result<Unit> = runRequestSafely(
        checkToken = { checkToken() },
        accessTokenAction = { loginStorage.getTrainerAccessToken() },
        action = { token ->
            val part = getImagePart(uri)
            loginApi.uploadTrainerPhoto(token, part)
        }
    )

    override suspend fun createTrainerService(name: String, price: Int): Result<Int> =
        runRequestSafely(
            checkToken = { checkToken() },
            accessTokenAction = { loginStorage.getTrainerAccessToken() },
            action = { token ->
                loginApi.createTrainerService(token, TrainerServiceRequestBody(name, price))
                    .map { it.id }
            }
        )

    override suspend fun deleteTrainerService(id: Int): Result<Unit> = runRequestSafely(
        checkToken = { checkToken() },
        accessTokenAction = { loginStorage.getTrainerAccessToken() },
        action = { token ->
            loginApi.deleteTrainerService(token, id)
        }
    )

    override suspend fun createAchievement(achievement: String): Result<Int> = runRequestSafely(
        checkToken = { checkToken() },
        accessTokenAction = { loginStorage.getTrainerAccessToken() },
        action = { token ->
            loginApi.createAchievement(token, CreateAchievementRequestBody(achievement))
                .map { it.id }
        }
    )

    override suspend fun deleteAchievement(id: Int): Result<Unit> = runRequestSafely(
        checkToken = { checkToken() },
        accessTokenAction = { loginStorage.getTrainerAccessToken() },
        action = { token ->
            loginApi.deleteAchievement(token, id)
        }
    )

    override suspend fun getSpecializations(): Result<List<Specialization>> =
        loginApi.getSpecializations()

    override suspend fun getRoles(): Result<List<Role>> = loginApi.getRoles()
    override suspend fun updateTrainerProfile(
        trainerMainInfoDTO: TrainerMainInfoDTO,
        roles: List<Int>,
        specializations: List<Int>
    ): Flow<Result<Unit>> = runFlowRequestSafely(
        checkToken = { checkToken() },
        accessTokenAction = { loginStorage.getTrainerAccessToken() },
        action = { token ->
            val dto = trainerMainInfoDTO.toTrainerMainInfoRequestBody()

            loginApi.updateTrainerMainInfo(token, dto)
            loginApi.updateTrainerRoles(token, roles)
            loginApi.updateTrainerSpecializations(token, specializations)
        }
    )

    override suspend fun logoutAsClient() {
        loginStorage.clearClientTokens()
    }

    override suspend fun logoutAsTrainer() {
        loginStorage.clearTrainerTokens()
    }

    override suspend fun getTrainerById(id: Int): TrainerInfo {
        return loginApi.getTrainer(id).toTrainerInfo()
    }

    override suspend fun getClientById(id: Int): ClientInfo {
        return loginApi.getUser(id).toClientInfo()
    }

    override suspend fun getTrainerCards(
        query: String,
        roles: List<Int>,
        specializations: List<Int>
    ): Flow<PagingData<TrainerCard>> {
        val pagingSource = trainerCoversPagingSourceFactory.create(query, roles, specializations)
        val pager = Pager(PagingConfig(50)) {
            pagingSource
        }
        return pager.flow.map { data ->
            data.map { cover -> cover.toTrainerCard() }
        }
    }

    override suspend fun getClientCards(query: String): Flow<PagingData<UserCard>> {
        val pagingSource = clientCoversPagingSourceFactory.create(query)
        val pager = Pager(PagingConfig(50)) {
            pagingSource
        }
        return pager.flow.map { data ->
            data.map { cover -> cover.toUserCard() }
        }
    }

    override suspend fun removeClientPhoto(): Unit = runRequestSafelyNotResult(
        checkToken = { checkToken() },
        accessTokenAction = { loginStorage.getClientAccessToken() },
        action = { token ->
            loginApi.uploadClientPhoto(token, null)
        }
    )

    override suspend fun removeTrainerPhoto(): Unit = runRequestSafelyNotResult(
        checkToken = { checkToken() },
        accessTokenAction = { loginStorage.getTrainerAccessToken() },
        action = { token ->
            loginApi.uploadTrainerPhoto(token, null)
        }
    )

    private fun getImagePart(imageUri: Uri?): MultipartBody.Part? {
        var part: MultipartBody.Part? = null
        imageUri?.let { uri ->
            val mimeType = context.contentResolver.getType(uri)
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes() ?: return null
            inputStream.close()
            val requestBody = bytes.toRequestBody(mimeType?.toMediaTypeOrNull())
            part = MultipartBody.Part.createFormData("photo", "photo.jpeg", requestBody)
        }
        return part
    }


}