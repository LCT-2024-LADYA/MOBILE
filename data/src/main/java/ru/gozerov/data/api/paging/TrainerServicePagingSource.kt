package ru.gozerov.data.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.gozerov.data.api.ServiceApi
import ru.gozerov.data.api.models.response.CustomServiceDTO
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.data.repositories.runRequestSafelyNotResult
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class TrainerServicePagingSource @Inject constructor(
    private val serviceApi: ServiceApi,
    private val loginRepository: LoginRepository,
    private val loginStorage: LoginStorage
) : PagingSource<Int, CustomServiceDTO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CustomServiceDTO> {
        val page = params.key ?: 0
        val cursor = if (page != 0) (page * 50) + 1 else 0
        return try {
            val response = runRequestSafelyNotResult(
                checkToken = { loginRepository.checkToken() },
                accessTokenAction = { loginStorage.getTrainerAccessToken() },
                action = { token ->
                    serviceApi.getTrainerServices(token, cursor)
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

    override fun getRefreshKey(state: PagingState<Int, CustomServiceDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(50)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(50)
        }
    }

}