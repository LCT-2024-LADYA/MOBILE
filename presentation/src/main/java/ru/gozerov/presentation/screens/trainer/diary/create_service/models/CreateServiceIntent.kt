package ru.gozerov.presentation.screens.trainer.diary.create_service.models

sealed interface CreateServiceIntent {

    object Reset : CreateServiceIntent

    object LoadServices : CreateServiceIntent

    data class CreateService(
        val userId: Int,
        val serviceId: Int,
        val date: String,
        val timeStart: String,
        val timeEnd: String
    ) : CreateServiceIntent

    data class SearchUsers(
        val query: String
    ) : CreateServiceIntent

}