package ru.gozerov.data.api.models.response

data class GetChatsResponseBody(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val last_message: String,
    val photo_url: String?,
    val time_last_message: String
)
