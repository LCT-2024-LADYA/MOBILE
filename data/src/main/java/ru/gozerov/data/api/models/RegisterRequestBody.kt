package ru.gozerov.data.api.models

data class RegisterRequestBody(
    val age: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val password: String,
    val sex: Int
)
