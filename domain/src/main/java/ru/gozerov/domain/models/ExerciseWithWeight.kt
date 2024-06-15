package ru.gozerov.domain.models

data class ExerciseWithWeight(
    val id: Int,
    val reps: Int,
    val sets: Int,
    val weight: Int
)
