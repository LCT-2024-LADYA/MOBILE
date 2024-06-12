package ru.gozerov.presentation.shared.screens.client_card.models

sealed interface ClientCardIntent {

    object Reset : ClientCardIntent

    data class LoadProfile(
        val clientId: Int
    ) : ClientCardIntent

}