package ru.gozerov.presentation.screens.trainee.main_training.main_training.models

sealed interface MainTrainingEffect {

    object None : MainTrainingEffect

    class Error(val message: String) : MainTrainingEffect

}