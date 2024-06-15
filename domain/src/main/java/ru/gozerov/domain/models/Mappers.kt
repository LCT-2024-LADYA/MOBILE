package ru.gozerov.domain.models

fun CustomExercise.toExercise() = Exercise(id, photos, name, tags)

fun TrainerTrainingCard.toTrainingCard() = TrainingCard(id, name, exercises, description)