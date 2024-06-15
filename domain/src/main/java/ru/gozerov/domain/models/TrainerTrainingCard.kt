package ru.gozerov.domain.models

data class TrainerTrainingCard(
    val id: Int,
    val name: String,
    val exercises: Int,
    val description: String,
    val is_confirm: Boolean,
    val wants_public: Boolean
)