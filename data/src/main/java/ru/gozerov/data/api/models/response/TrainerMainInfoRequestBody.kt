package ru.gozerov.data.api.models.response

data class TrainerMainInfoRequestBody(
    val age: Int,
    val email: String,
    val experience: Int,
    val first_name: String,
    val last_name: String,
    val quote: String,
    val sex: Int
)
