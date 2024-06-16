package ru.gozerov.domain.models

sealed class ChatItem {
    data class ChatMessage(
        val id: Int,
        val isToUser: Boolean,
        val message: String?,
        val serviceId: Int?,
        val time: String,
        val trainerId: Int,
        val userId: Int
    ) : ChatItem()

    data class DateMessage(
        val message: String
    ) : ChatItem()

}
