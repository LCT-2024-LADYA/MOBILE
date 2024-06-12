package ru.gozerov.presentation.screens.trainer.chat.list.models

sealed interface TrainerChatListIntent {

    object Reset : TrainerChatListIntent

    data class Init(
        val queryChats: String,
        val queryClients: String
    ) : TrainerChatListIntent

    object LoadChats : TrainerChatListIntent

    data class LoadClients(
        val query: String,
    ) : TrainerChatListIntent

}