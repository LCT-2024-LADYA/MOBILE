package ru.gozerov.data.api.models.response

data class ScheduledTrainingDTO(
    val id: Int,
    val training_id: Int,
    val date: String,
    val description: String,
    val name: String,
    val time_start: String,
    val time_end: String,
    val exercises: List<CustomExerciseDTO>
)
