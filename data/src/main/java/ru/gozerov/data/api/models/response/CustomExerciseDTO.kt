package ru.gozerov.data.api.models.response

data class CustomExerciseDTO(
    val id: Int,
    val name: String,
    val additionalMuscle: String,
    val difficulty: String,
    val equipment: String,
    val muscle: String,
    val type: String,
    val status: Boolean,
    val photos: List<String>,
    val reps: Int,
    val sets: Int,
    val weight: Int
)
