package ru.gozerov.presentation.screens.trainee.chat.list.models

sealed interface ChatListIntent {

    object Reset : ChatListIntent

    data class Init(
        val queryTrainers: String,
        val roles: List<Int>,
        val specializations: List<Int>,
        val queryChats: String
    ) : ChatListIntent

    object LoadChats : ChatListIntent

    data class LoadTrainers(
        val query: String,
        val roles: List<Int>,
        val specializations: List<Int>
    ) : ChatListIntent

}