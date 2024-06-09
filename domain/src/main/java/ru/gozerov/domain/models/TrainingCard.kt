package ru.gozerov.domain.models

data class TrainingCard(
    val id: Int,
    val name: String,
    val date: String,
    val exerciseCount: Int,
    val description: String
)
