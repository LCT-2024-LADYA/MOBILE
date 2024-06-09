package ru.gozerov.domain.models

data class CustomTraining(
    val id: Int,
    val date: String,
    val description: String,
    val name: String,
    val timeStart: String,
    val timeEnd: String,
    val exercises: List<CustomExercise>
)
