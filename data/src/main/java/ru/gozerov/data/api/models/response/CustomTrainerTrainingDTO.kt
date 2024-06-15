package ru.gozerov.data.api.models.response

data class CustomTrainerTrainingDTO(
    val id: Int,
    val description: String,
    val name: String,
    val exercises: List<CustomExerciseDTO>,
    val is_confirm: Boolean,
    val wants_public: Boolean
)
