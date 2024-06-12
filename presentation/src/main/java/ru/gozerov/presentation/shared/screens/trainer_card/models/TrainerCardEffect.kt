package ru.gozerov.presentation.shared.screens.trainer_card.models

import ru.gozerov.domain.models.TrainerInfo

sealed interface TrainerCardEffect {

    object None : TrainerCardEffect

    data class LoadedProfile(
        val trainer: TrainerInfo
    ) : TrainerCardEffect

    data class Error(
        val message: String
    ) : TrainerCardEffect

}