package ru.gozerov.data.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.gozerov.data.api.TrainingApi
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.data.repositories.runRequestSafelyNotResult
import ru.gozerov.domain.models.ProgressCard
import ru.gozerov.domain.repositories.LoginRepository

class GetProgressPagingSource @AssistedInject constructor(
    @Assisted("query") private val query: String,
    @Assisted("dateStart") private val dateStart: String,
    @Assisted("dateEnd") private val dateEnd: String,
    private val trainingApi: TrainingApi,
    private val loginRepository: LoginRepository,
    private val loginStorage: LoginStorage
) : PagingSource<Int, ProgressCard>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProgressCard> {
        val page = params.key ?: 0
        return try {
            val response = runRequestSafelyNotResult(
                checkToken = { loginRepository.checkToken() },
                accessTokenAction = { loginStorage.getClientAccessToken() },
                action = { token ->
                    trainingApi.getProgress(token, query, dateStart, dateEnd, page)
                }
            )
            LoadResult.Page(
                data = response.objects,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.objects.isEmpty() || !response.is_more) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProgressCard>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(50)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(50)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("query") query: String,
            @Assisted("dateStart") dateStart: String,
            @Assisted("dateEnd") dateEnd: String,
        ): GetProgressPagingSource

    }

}