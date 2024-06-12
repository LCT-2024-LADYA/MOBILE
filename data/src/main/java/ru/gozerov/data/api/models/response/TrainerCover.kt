package ru.gozerov.data.api.models.response

import ru.gozerov.domain.models.Role
import ru.gozerov.domain.models.Specialization

data class TrainerCover(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val photo_url: String?,
    val age: Int,
    val sex: Int,
    val experience: Int,
    val quote: String?,
    val roles: List<Role>,
    val specializations: List<Specialization>,
)
