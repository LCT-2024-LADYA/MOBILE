package ru.gozerov.data.api.models.response

data class GetExercisesResponse(
    val cursor: Int,
    val objects: List<ExerciseDTO>
)
