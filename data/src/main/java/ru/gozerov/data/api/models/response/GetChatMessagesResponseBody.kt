package ru.gozerov.data.api.models.response

data class GetChatMessagesResponseBody(
    val cursor: Int,
    val objects: List<ChatMessageDTO>
)
