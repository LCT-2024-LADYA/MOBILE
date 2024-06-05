package ru.gozerov.domain.models

data class RegisterModel(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val sex: Int
)
