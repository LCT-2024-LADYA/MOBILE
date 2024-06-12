package ru.gozerov.data.api.models.response

data class GetUserCoversResponse(
    val objects: List<ClientCover>,
    val cursor: Int
)