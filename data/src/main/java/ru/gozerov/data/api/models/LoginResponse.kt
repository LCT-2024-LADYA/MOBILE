package ru.gozerov.data.api.models

data class LoginResponse(
    val access_token: String,
    val refresh_token: String
)
