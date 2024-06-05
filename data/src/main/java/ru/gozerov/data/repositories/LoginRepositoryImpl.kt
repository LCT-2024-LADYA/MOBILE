package ru.gozerov.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gozerov.data.api.LoginApi
import ru.gozerov.data.api.models.LoginRequestBody
import ru.gozerov.data.api.models.RegisterRequestBody
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi
) : LoginRepository {
    override suspend fun register(email: String, password: String): Flow<Result<Unit>> = flow {
        loginApi.register(RegisterRequestBody(18, email, "Samara", "Butsovich", password, 1))
            .onSuccess {
                emit(Result.success(Unit))
            }
            .onFailure {
                emit(Result.failure(IllegalStateException()))
            }
    }

    override suspend fun loginAsTrainee(email: String, password: String): Flow<Result<Unit>> =
        flow {
            loginApi.loginAsTrainee(LoginRequestBody(email, password))
                .onSuccess {
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
                emit(Result.success(Unit))
            }
            .onFailure { throwable ->
                emit(Result.failure(throwable))
            }
    }

}