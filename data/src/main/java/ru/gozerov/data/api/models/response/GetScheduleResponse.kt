package ru.gozerov.data.api.models.response

data class GetScheduleResponse(
    val date: String,
    val user_training_ids: List<Int>
)
