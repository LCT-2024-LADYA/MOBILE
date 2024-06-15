package ru.gozerov.data.api.models.request

data class TrainerServiceRequestBody(
    val name: String,
    val price: Int,
    val profile_access: Boolean = false
)
