package ru.gozerov.data.api.models.request

data class RegisterRequestBody(
    val email: String,
    val password: String,
    val first_name: String,
    val last_name: String,
    val age: Int,
    val sex: Int
)
