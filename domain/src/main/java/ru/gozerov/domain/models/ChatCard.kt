package ru.gozerov.domain.models

data class ChatCard(
    val id: Int,
    val userPhoto: String?,
    val username: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int
)
