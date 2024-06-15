package ru.gozerov.data.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.gozerov.data.api.LoginApi
import ru.gozerov.data.api.models.response.ClientCover

class ClientCoversPagingSource @AssistedInject constructor(
    private val loginApi: LoginApi,
    @Assisted("query") private val query: String
) : PagingSource<Int, ClientCover>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ClientCover> {
        val page = params.key ?: 0
        val cursor = if (page != 0) (page * 50) + 1 else 0
        return try {
            val response = loginApi.getUserCovers(query, cursor)
            LoadResult.Page(
                data = response.objects,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.objects.isEmpty() || response.cursor == 0) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ClientCover>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(50)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(50)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("query") query: String?
        ): ClientCoversPagingSource

    }

}