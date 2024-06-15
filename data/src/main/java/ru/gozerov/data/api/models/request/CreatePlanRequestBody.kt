package ru.gozerov.data.api.models.request

data class CreatePlanRequestBody(
    val name: String,
    val description: String,
    val trainings: List<Int>
)
