package ru.gozerov.presentation.screens.trainer.chat.list.models

sealed interface TrainerChatListIntent {

    object Reset : TrainerChatListIntent

    data class Init(
        val query: String
    ) : TrainerChatListIntent

    data class LoadChats(
        val query: String
    ) : TrainerChatListIntent

    data class LoadClients(
        val query: String,
    ) : TrainerChatListIntent

}