package ru.gozerov.domain.models

data class CreateExerciseModel(
    val id: Int,
    val step: Int,
    val reps: Int,
    val sets: Int,
    val weight: Int
)
