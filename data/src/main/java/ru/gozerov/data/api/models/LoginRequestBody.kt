package ru.gozerov.data.api.models

data class LoginRequestBody(
    val access_token: String,
    val email: String?,
    val vk_id: Long
)