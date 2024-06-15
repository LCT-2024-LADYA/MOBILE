package ru.gozerov.domain.models

data class CreateTrainerTrainingModel(
    val description: String,
    val exercises: List<CreateExerciseModel>,
    val name: String,
    val wantsPublic: Boolean
)