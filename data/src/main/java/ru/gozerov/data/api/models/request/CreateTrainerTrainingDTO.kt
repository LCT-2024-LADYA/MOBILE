package ru.gozerov.data.api.models.request

data class CreateTrainerTrainingDTO(
    val description: String,
    val exercises: List<CreateExerciseDTO>,
    val name: String,
    val wants_public: Boolean
)
