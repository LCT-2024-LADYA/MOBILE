package ru.gozerov.presentation.screens.trainee.profile.services.models

sealed interface ClientServicesIntent {

    object Reset : ClientServicesIntent

    object LoadServices : ClientServicesIntent

    data class SetStatus(
        val serviceId: Int,
        val status: Boolean,
        val type: Int
    ) : ClientServicesIntent

    data class DeleteService(
        val id: Int
    ) : ClientServicesIntent

}