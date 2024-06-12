package ru.gozerov.presentation.shared.screens.client_card.models

import ru.gozerov.domain.models.ClientInfo

sealed interface ClientCardEffect {

    object None : ClientCardEffect

    data class LoadedProfile(
        val client: ClientInfo
    ) : ClientCardEffect

    data class Error(
        val message: String
    ) : ClientCardEffect


}