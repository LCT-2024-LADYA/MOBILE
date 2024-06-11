package ru.gozerov.presentation.screens.trainee.diary.find_training.models

sealed interface FindTrainingIntent {

    object Reset: FindTrainingIntent

    object GetTrainings: FindTrainingIntent

    data class FindInAllTrainings(
        val query: String
    ): FindTrainingIntent

    data class FindInUserTrainings(
        val query: String
    ): FindTrainingIntent

}