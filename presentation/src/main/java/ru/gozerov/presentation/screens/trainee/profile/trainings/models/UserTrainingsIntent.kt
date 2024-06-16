package ru.gozerov.presentation.screens.trainee.profile.trainings.models

sealed interface UserTrainingsIntent {

    object Reset : UserTrainingsIntent

    data class LoadTrainings(
        val query: String
    ) : UserTrainingsIntent

}