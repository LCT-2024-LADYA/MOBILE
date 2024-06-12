package ru.gozerov.domain.models

fun CustomExercise.toExercise() = Exercise(id, photos, name, tags)