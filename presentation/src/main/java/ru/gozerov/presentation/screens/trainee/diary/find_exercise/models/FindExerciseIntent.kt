package ru.gozerov.presentation.screens.trainee.diary.find_exercise.models

import ru.gozerov.domain.models.Exercise

sealed interface FindExerciseIntent {

    object Reset : FindExerciseIntent

    data class SearchExercise(
        val query: String
    ) : FindExerciseIntent

    data class AddExercise(
        val exercise: Exercise
    ) : FindExerciseIntent

}