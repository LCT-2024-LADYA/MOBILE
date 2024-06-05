package ru.gozerov.data.api.models.response

import ru.gozerov.domain.models.Role
import ru.gozerov.domain.models.Specialization
import ru.gozerov.domain.models.TrainerService

data class TrainerInfoResponse(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val photo_url: String?,
    val email: String,
    val age: Int,
    val sex: Int,
    val experience: Int,
    val quote: String?,
    val roles: List<Role>,
    val specializations: List<Specialization>,
    val services: List<TrainerService>,
    val achievements: List<AchievementDTO>
)
