package ru.gozerov.data.api.models.response

data class GetServicesPagination(
    val cursor: Int,
    val objects: List<CustomServiceDTO>
)