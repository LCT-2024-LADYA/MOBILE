package ru.gozerov.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import ru.gozerov.data.api.LoginApi
import ru.gozerov.data.api.models.request.LoginRequestBody
import ru.gozerov.data.api.models.toClientInfo
import ru.gozerov.data.api.models.toRegisterRequestBody
import ru.gozerov.data.api.models.toTrainerInfo
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.domain.models.CheckTokenResult
import ru.gozerov.domain.models.ClientInfo
import ru.gozerov.domain.models.RegisterModel
import ru.gozerov.domain.models.TrainerInfo
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi,
    private val loginStorage: LoginStorage
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

    override suspend fun clientMe(): Result<ClientInfo> {
        val token =
            loginStorage.getClientAccessToken() ?: throw IllegalStateException("Not Authorized")
        return loginApi.getClientMe(token).map { response ->
            response.toClientInfo()
        }
    }

    override suspend fun trainerMe(): Result<TrainerInfo> {
        val token =
            loginStorage.getTrainerAccessToken() ?: throw IllegalStateException("Not Authorized")
        return loginApi.getTrainerMe(token).map { response ->
            response.toTrainerInfo()
        }
    }

    override suspend fun checkToken(): CheckTokenResult {
        val userType = loginStorage.getRole()
        if (userType == 0) return CheckTokenResult(isAuth = false, isClient = false)
        if (userType == 1) {
            clientMe()
                .onSuccess {
                    return CheckTokenResult(isAuth = true, isClient = true)
                }
                .onFailure { throwable ->
                    if ((throwable as? HttpException)?.code() == 401) {
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
            trainerMe()
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

}