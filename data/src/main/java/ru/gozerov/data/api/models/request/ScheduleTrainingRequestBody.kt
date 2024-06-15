package ru.gozerov.data.api.models.request

import ru.gozerov.domain.models.ExerciseWithWeight

data class ScheduleTrainingRequestBody(
    val date: String,
    val id: Int,
    val time_start: String,
    val time_end: String,
    val exercises: List<ExerciseWithWeight>
)
