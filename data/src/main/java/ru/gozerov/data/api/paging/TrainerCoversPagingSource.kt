package ru.gozerov.data.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.gozerov.data.api.LoginApi
import ru.gozerov.data.api.models.response.TrainerCover

class TrainerCoversPagingSource @AssistedInject constructor(
    private val loginApi: LoginApi,
    @Assisted("query") private val query: String,
    @Assisted("roles") private val roles: List<Int>,
    @Assisted("specializations") private val specializations: List<Int>
) : PagingSource<Int, TrainerCover>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrainerCover> {
        val page = params.key ?: 0
        val cursor = if (page != 0) (page * 50) + 1 else 0
        return try {
            val response = loginApi.getTrainerCovers(
                query,
                cursor,
                roles.toTypedArray(),
                specializations.toTypedArray()
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

    override fun getRefreshKey(state: PagingState<Int, TrainerCover>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(50)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(50)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("query") query: String?,
            @Assisted("roles") roles: List<Int>,
            @Assisted("specializations") specializations: List<Int>
        ): TrainerCoversPagingSource

    }

}