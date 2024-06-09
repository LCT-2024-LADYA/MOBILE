package ru.gozerov.domain.models

data class CreateTrainingModel(
    val description: String,
    val exercises: List<CreateExerciseModel>,
    val name: String
)
