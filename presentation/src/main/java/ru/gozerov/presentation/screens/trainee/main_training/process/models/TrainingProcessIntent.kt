package ru.gozerov.presentation.screens.trainee.main_training.process.models

sealed interface TrainingProcessIntent {

    object Reset : TrainingProcessIntent

    data class CompleteExercise(
        val trainingId: Int,
        val exerciseId: Int
    ) : TrainingProcessIntent

    data class EndTraining(
        val trainingId: Int
    ) : TrainingProcessIntent

}