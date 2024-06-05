package ru.gozerov.domain.models

data class TrainerInfo(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val photoUrl: String?,
    val age: Int,
    val sex: Int,
    val experience: Int,
    val quote: String?,
    val roles: List<Role>,
    val specializations: List<Specialization>,
    val services: List<TrainerService>,
    val achievements: List<Achievement>
)
