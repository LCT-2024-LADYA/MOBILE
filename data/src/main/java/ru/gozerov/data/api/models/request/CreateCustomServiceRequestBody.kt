package ru.gozerov.data.api.models.request

data class CreateCustomServiceRequestBody(
    val service_id: Int,
    val user_id: Int
)
