package ru.gozerov.data.api.models.response

data class ScheduleTrainingRequestBody(
    val date: String,
    val id: Int,
    val time_start: String,
    val time_end: String
)
