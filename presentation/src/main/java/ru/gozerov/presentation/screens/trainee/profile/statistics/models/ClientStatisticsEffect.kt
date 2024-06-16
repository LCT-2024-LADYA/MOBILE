package ru.gozerov.presentation.screens.trainee.profile.statistics.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.ProgressCard

sealed interface ClientStatisticsEffect {

    object None : ClientStatisticsEffect

    data class LoadedStatistics(
        val statistics: Flow<PagingData<ProgressCard>>
    ) : ClientStatisticsEffect

    class Error(
        val message: String
    ) : ClientStatisticsEffect

}