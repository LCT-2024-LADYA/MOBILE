package ru.gozerov.data.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.gozerov.data.api.TrainingApi
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.data.repositories.runRequestSafelyNotResult
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.domain.repositories.LoginRepository

class UserTrainingPagingSource @AssistedInject constructor(
    private val trainingApi: TrainingApi,
    @Assisted("query") private val query: String?,
    private val loginRepository: LoginRepository,
    private val loginStorage: LoginStorage
) : PagingSource<Int, TrainingCard>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TrainingCard> {
        val page = params.key ?: 0
        val cursor = if (page != 0) (page * 50) + 1 else 0
        return try {
            val response = runRequestSafelyNotResult(
                checkToken = { loginRepository.checkToken() },
                accessTokenAction = { loginStorage.getClientAccessToken() },
                action = { token ->
                    query?.run {
                        trainingApi.getUserTrainings(
                            token,
                            query,
                            cursor
                        )
                    } ?: trainingApi.getUserTrainings(token, cursor)
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

    override fun getRefreshKey(state: PagingState<Int, TrainingCard>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(50)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(50)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("query") query: String?
        ): UserTrainingPagingSource

    }


}