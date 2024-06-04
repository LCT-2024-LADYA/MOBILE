package ru.gozerov.domain.models

data class Exercise(
    val id: Int,
    val photoUrl: String,
    val name: String,
    val tags: List<String>,
    val weight: Double,
    val setsCount: Int,
    val repsCount: Int
)
