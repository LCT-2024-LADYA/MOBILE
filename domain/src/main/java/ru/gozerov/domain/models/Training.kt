package ru.gozerov.domain.models

data class Training(
    val id: Int,
    val name: String,
    val date: String,
    val time: String,
    val exerciseCount: Int,
    val description: String,
    val exercises: List<Exercise>
)
