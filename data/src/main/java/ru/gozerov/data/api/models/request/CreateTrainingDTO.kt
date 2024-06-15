package ru.gozerov.data.api.models.request

data class CreateTrainingDTO(
    val description: String,
    val exercises: List<CreateExerciseDTO>,
    val name: String
)
