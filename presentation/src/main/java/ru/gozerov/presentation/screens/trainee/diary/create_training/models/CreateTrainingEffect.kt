package ru.gozerov.presentation.screens.trainee.diary.create_training.models

import ru.gozerov.domain.models.Exercise
import ru.gozerov.domain.models.Training

sealed interface CreateTrainingEffect {

    object None : CreateTrainingEffect

    data class AddedExercises(
        val exercises: List<Exercise>
    ) : CreateTrainingEffect

    data class LoadedTraining(
        val training: Training
    ) : CreateTrainingEffect

    object CreatedTraining : CreateTrainingEffect

    data class Error(
        val message: String
    ) : CreateTrainingEffect

    data class RemovedExercise(
        val id: Int,
        val index: Int
    ) : CreateTrainingEffect

}