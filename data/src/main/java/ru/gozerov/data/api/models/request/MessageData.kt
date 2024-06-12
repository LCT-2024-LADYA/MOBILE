package ru.gozerov.data.api.models.request

import kotlinx.serialization.Serializable

@Serializable
data class MessageData(
    val to: Int,
    val message: String?,
    val service_id: Int?
)
