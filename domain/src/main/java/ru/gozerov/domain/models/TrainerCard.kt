package ru.gozerov.domain.models

data class TrainerCard(
    val id: Int,
    val photoUrl: String?,
    val firstName: String,
    val lastName: String,
    val roles: List<Role>,
    val age: Int,
    val sex: Int,
    val quote: String?,
    val specializations: List<Specialization>,
    val experience: Int
)
