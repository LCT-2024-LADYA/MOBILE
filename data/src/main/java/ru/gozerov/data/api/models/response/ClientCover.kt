package ru.gozerov.data.api.models.response

data class ClientCover(
    val id: Int,
    val age: Int,
    val photo_url: String?,
    val first_name: String,
    val last_name: String,
    val sex: Int
)
