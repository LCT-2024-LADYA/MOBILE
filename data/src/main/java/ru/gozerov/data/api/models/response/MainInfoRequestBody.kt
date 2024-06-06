package ru.gozerov.data.api.models.response

data class MainInfoRequestBody(
    val age: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val sex: Int
)
