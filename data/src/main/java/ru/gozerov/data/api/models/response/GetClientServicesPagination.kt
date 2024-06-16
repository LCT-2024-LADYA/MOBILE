package ru.gozerov.data.api.models.response

data class GetClientServicesPagination(
    val cursor: Int,
    val objects: List<CustomClientServiceDTO>
)
