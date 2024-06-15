package ru.gozerov.presentation.screens.trainee.diary.select_training.models

import ru.gozerov.domain.models.TrainingCard

sealed interface SelectTrainingIntent {

    object Reset : SelectTrainingIntent

    object GetTrainings : SelectTrainingIntent

    data class FindInAllTrainings(
        val query: String
    ) : SelectTrainingIntent

    data class FindInTrainerTrainings(
        val query: String
    ) : SelectTrainingIntent

    data class AddTraining(
        val training: TrainingCard
    ) : SelectTrainingIntent

}