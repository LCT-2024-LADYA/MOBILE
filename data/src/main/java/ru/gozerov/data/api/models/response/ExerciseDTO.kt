package ru.gozerov.data.api.models.response

data class ExerciseDTO(
    val id: Int,
    val name: String,
    val additionalMuscle: String,
    val difficulty: String,
    val equipment: String,
    val muscle: String,
    val type: String,
    val photos: List<String>
)
