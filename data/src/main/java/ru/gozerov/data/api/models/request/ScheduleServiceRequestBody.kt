package ru.gozerov.data.api.models.request

data class ScheduleServiceRequestBody(
    val date: String,
    val schedule_id: Int,
    val time_start: String,
    val time_end: String
)
