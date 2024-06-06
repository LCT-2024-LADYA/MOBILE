package ru.gozerov.data.api.models.response

data class UserInfoResponse(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val age: Int,
    val email: String,
    val photo_url: String?,
    val sex: Int
)