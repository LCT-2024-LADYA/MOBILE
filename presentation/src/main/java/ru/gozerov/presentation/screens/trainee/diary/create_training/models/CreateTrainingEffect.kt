package ru.gozerov.presentation.screens.trainee.diary.create_training.models

import ru.gozerov.domain.models.Exercise

sealed interface CreateTrainingEffect {

    object None : CreateTrainingEffect

    data class AddedExercises(
        val exercises: List<Exercise>
    ) : CreateTrainingEffect

    object CreatedTraining : CreateTrainingEffect

    data class Error(
        val message: String
    ) : CreateTrainingEffect

}