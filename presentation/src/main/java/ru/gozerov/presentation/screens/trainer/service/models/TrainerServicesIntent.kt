package ru.gozerov.presentation.screens.trainer.service.models

sealed interface TrainerServicesIntent {

    object Reset : TrainerServicesIntent

    object LoadServices : TrainerServicesIntent

    data class SetStatus(
        val serviceId: Int,
        val status: Boolean,
        val type: Int
    ) : TrainerServicesIntent

    data class DeleteService(
        val id: Int
    ) : TrainerServicesIntent

}