package ru.gozerov.presentation.screens.trainee.main_training.main_training.models

sealed interface MainTrainingIntent {

    object Reset : MainTrainingIntent

    object LoadNextTraining : MainTrainingIntent

}