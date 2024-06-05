package ru.gozerov.data.api.models

data class LoginRequestBody(
    val email: String,
    val password: String
)