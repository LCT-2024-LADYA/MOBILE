package ru.gozerov.data.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.gozerov.data.api.ChatApi
import ru.gozerov.data.api.models.response.ChatMessageDTO
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.data.repositories.runRequestSafelyNotResult
import ru.gozerov.domain.repositories.LoginRepository

class ClientMessagePagingSource @AssistedInject constructor(
    private val chatApi: ChatApi,
    private val loginStorage: LoginStorage,
    private val loginRepository: LoginRepository,
    @Assisted("trainerId") private val trainerId: Int
) : PagingSource<Int, ChatMessageDTO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChatMessageDTO> {
        val page = params.key ?: 0
        val cursor = if (page != 0) (page * 50) + 1 else 0
        return try {
            val response = runRequestSafelyNotResult(
                checkToken = { loginRepository.checkToken() },
                accessTokenAction = { loginStorage.getClientAccessToken() },
                action = { token ->
                    chatApi.getUserChatMessages(token, trainerId, cursor)
                }
            )
            LoadResult.Page(
                data = response.objects,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.objects.isEmpty() || response.cursor == 0) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ChatMessageDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(50)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(50)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("trainerId") trainerId: Int
        ): ClientMessagePagingSource

    }

}