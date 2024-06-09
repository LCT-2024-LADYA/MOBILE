package ru.gozerov.domain.models

data class CreateExerciseModel(
    val id: Int,
    val reps: Int,
    val sets: Int,
    val status: Boolean = false,
    val weight: Int
)
