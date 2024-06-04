package ru.gozerov.data.repositories

import android.content.Context
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import ru.gozerov.data.api.LoginApi
import ru.gozerov.data.api.models.LoginRequestBody
import ru.gozerov.domain.models.VKLoginResponse
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginApi: LoginApi
) : LoginRepository {
    override suspend fun loginThroughVk(): Flow<Result<VKLoginResponse>> {
        val flow = MutableSharedFlow<Result<VKLoginResponse>>(1, 0, BufferOverflow.DROP_OLDEST)
        val vkid = VKID(context)
        val vkAuthCallback = object : VKID.AuthCallback {

            override fun onSuccess(accessToken: AccessToken) {
                flow.tryEmit(Result.success(VKLoginResponse(accessToken.token, accessToken.userID)))
            }

            override fun onFail(fail: VKIDAuthFail) {
                when (fail) {
                    is VKIDAuthFail.Canceled -> {
                        flow.tryEmit(Result.failure(IllegalStateException()))
                    }

                    else -> {
                        flow.tryEmit(Result.failure(IllegalStateException()))
                    }
                }
            }

        }
        vkid.authorize(vkAuthCallback)
        return flow.asSharedFlow()
    }

    override suspend fun loginAsTrainee(
        accessToken: String,
        id: Long,
        email: String?
    ): Flow<Result<Unit>> = flow {
        val response = loginApi.loginAsTrainee(LoginRequestBody(accessToken, email, id))
        response
            .onSuccess { loginResponse ->
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