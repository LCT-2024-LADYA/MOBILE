package ru.gozerov.domain.models

data class ClientInfo(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val email: String,
    val photoUrl: String?
)
