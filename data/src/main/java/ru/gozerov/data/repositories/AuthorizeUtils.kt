package ru.gozerov.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

suspend fun <E> runRequestSafely(
    checkToken: suspend () -> Unit,
    accessTokenAction: () -> String?,
    action: suspend (token: String) -> Result<E>
): Result<E> {
    val token = accessTokenAction()
        ?: return Result.failure(IllegalStateException("not authorized"))
    val result = action(token)
    if (result.isFailure && (result.exceptionOrNull() as? HttpException)?.code() == 400) {
        checkToken()
        val newToken = accessTokenAction() ?: return Result.failure(
            IllegalStateException("not authorized")
        )
        return action(newToken)
    } else {
        return result
    }
}

suspend fun <E> runFlowRequestSafely(
    checkToken: suspend () -> Unit,
    accessTokenAction: () -> String?,
    action: suspend (token: String) -> Result<E>
): Flow<Result<E>> = flow {
    val token = accessTokenAction()
    token?.let {
        val result = action(token)
        if (result.isFailure && (result.exceptionOrNull() as? HttpException)?.code() == 400) {
            checkToken()
            val newToken = accessTokenAction()
            newToken?.let {
                emit(action(newToken))
            } ?: emit(
                Result.failure(
                    IllegalStateException("not authorized")
                )
            )
        } else
            emit(result)
    } ?: emit(Result.failure(IllegalStateException("not authorized")))

}