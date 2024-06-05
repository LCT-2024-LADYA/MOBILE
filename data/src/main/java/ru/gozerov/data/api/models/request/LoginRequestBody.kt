package ru.gozerov.data.api.models.request

data class LoginRequestBody(
    val email: String,
    val password: String
)