package ru.gozerov.presentation.screens.trainee.chat.list.models

sealed interface ChatListIntent {

    object Reset : ChatListIntent

    data class Init(
        val query: String,
        val roles: List<Int>,
        val specializations: List<Int>
    ) : ChatListIntent

    data class LoadChats(
        val query: String
    ) : ChatListIntent

    data class LoadTrainers(
        val query: String,
        val roles: List<Int>,
        val specializations: List<Int>
    ) : ChatListIntent

    object LoadRolesAndSpecializations : ChatListIntent

}