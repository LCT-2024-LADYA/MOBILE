package ru.gozerov.domain.models

data class ChatMessage(
    val id: Int,
    val isToUser: Boolean,
    val message: String?,
    val serviceId: Int?,
    val time: String,
    val trainerId: Int,
    val userId: Int
)
