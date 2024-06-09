package ru.gozerov.data.api.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.gozerov.data.api.TrainingApi
import ru.gozerov.data.api.models.response.ExerciseDTO

class ExercisePagingSource @AssistedInject constructor(
    private val trainingApi: TrainingApi,
    @Assisted("query") private val query: String?
) : PagingSource<Int, ExerciseDTO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ExerciseDTO> {
        val page = params.key ?: 0
        return try {
            val response =
                query?.run { trainingApi.getExercises(query, page) } ?: trainingApi.getExercises(
                    page
                )
            LoadResult.Page(
                data = response.objects,
                prevKey = if (page == 0) null else page - 50,
                nextKey = if (response.objects.isEmpty()) null else response.cursor
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ExerciseDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(50)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(50)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("query") query: String?
        ): ExercisePagingSource

    }


}