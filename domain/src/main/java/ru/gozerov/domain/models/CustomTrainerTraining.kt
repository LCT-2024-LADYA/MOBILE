package ru.gozerov.domain.models

data class CustomTrainerTraining(
    val id: Int,
    val description: String,
    val name: String,
    val exercises: List<CustomExercise>,
    val isConfirm: Boolean,
    val wantsPublic: Boolean
)
