package ru.gozerov.presentation.shared.screens.trainer_card.models

sealed interface TrainerCardIntent {

    object Reset : TrainerCardIntent

    data class LoadProfile(
        val trainerId: Int
    ) : TrainerCardIntent

}