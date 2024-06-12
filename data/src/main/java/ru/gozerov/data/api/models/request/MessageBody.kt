package ru.gozerov.data.api.models.request

import kotlinx.serialization.Serializable

@Serializable
data class MessageBody(
    val type: String,
    val data: MessageData
)
