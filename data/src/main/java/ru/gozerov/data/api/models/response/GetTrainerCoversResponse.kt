package ru.gozerov.data.api.models.response

data class GetTrainerCoversResponse(
    val objects: List<TrainerCover>,
    val cursor: Int
)
