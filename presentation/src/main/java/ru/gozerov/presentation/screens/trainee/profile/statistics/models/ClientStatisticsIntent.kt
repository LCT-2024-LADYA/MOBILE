package ru.gozerov.presentation.screens.trainee.profile.statistics.models

sealed interface ClientStatisticsIntent {

    object Reset : ClientStatisticsIntent

    data class LoadStatistics(
        val query: String,
        val dateStart: String,
        val dateEnd: String
    ) : ClientStatisticsIntent

}