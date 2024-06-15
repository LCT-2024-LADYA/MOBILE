package ru.gozerov.domain.models

data class TrainingPlanCard(
    val id: Int,
    val name: String,
    val description: String,
    val trainings: Int
)
