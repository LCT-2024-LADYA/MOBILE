package ru.gozerov.presentation.screens.trainee.main_training.process.models

sealed interface TrainingProcessEffect {

    object None : TrainingProcessEffect

    data class Error(
        val message: String
    ) : TrainingProcessEffect

}
