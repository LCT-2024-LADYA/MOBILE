package ru.gozerov.presentation.screens.trainee.main_training.main_training.models

import ru.gozerov.domain.models.CustomTraining

sealed interface MainTrainingViewState {

    object None : MainTrainingViewState

    object Empty : MainTrainingViewState

    data class LoadedTraining(
        val training: CustomTraining
    ) : MainTrainingViewState

}