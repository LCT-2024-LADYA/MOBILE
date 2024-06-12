package ru.gozerov.data.api.models.response

data class ClientProfileDTO(
    val id: Int,
    val age: Int,
    val email: String,
    val photo_url: String?,
    val first_name: String,
    val last_name: String,
    val sex: Int
)
