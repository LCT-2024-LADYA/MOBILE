package ru.gozerov.presentation.screens.trainee.profile.models

import ru.gozerov.domain.models.ClientInfo

sealed interface ClientProfileEffect {

    object None : ClientProfileEffect

    data class LoadedProfile(
        val clientInfo: ClientInfo
    ) : ClientProfileEffect

    data class Error(
        val message: String
    ) : ClientProfileEffect

    data object SuccessfulInfoUpdate : ClientProfileEffect

    data object SuccessfulPhotoUpdate : ClientProfileEffect

    data object Logout : ClientProfileEffect

}