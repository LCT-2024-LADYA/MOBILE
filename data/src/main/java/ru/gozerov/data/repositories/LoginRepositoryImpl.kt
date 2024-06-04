package ru.gozerov.data.repositories

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.gozerov.data.api.LoginApi
import ru.gozerov.data.api.models.LoginRequestBody
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi
) : LoginRepository {

    override suspend fun loginAsTrainee(
        accessToken: String,
        id: Long,
        email: String?
    ): Flow<Result<Unit>> = flow {
        Log.e("AAA", "fsfa")
        val response = loginApi.loginAsTrainee(LoginRequestBody(accessToken, "ozeroffgrisha@gmail.com", id))
        response
            .onSuccess { loginResponse ->
                Log.e("AAAA", loginResponse.toString())
                emit(Result.success(Unit))
            }
            .onFailure { throwable ->
                emit(Result.failure(throwable))
            }
    }


    override suspend fun loginAsTrainer(
        email: String,
        password: String
    ): Flow<Result<Unit>> = flow {
        val response = loginApi.loginAsTrainer()

        response
            .onSuccess { loginResponse ->
                emit(Result.success(Unit))
            }
            .onFailure { throwable ->
                emit(Result.failure(throwable))
            }
    }

}