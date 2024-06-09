package ru.gozerov.data.api.models.response

data class CustomTrainingDTO(
    val id: Int,
    val description: String,
    val name: String,
    val exercises: List<CustomExerciseDTO>
)

