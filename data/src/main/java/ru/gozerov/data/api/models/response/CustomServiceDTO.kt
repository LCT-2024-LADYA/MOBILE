package ru.gozerov.data.api.models.response

import ru.gozerov.domain.models.TrainerService

data class CustomServiceDTO(
    val id: Int,
    val is_payed: Boolean,
    val service: TrainerService,
    val service_id: Int,
    val trainer_confirm: Boolean?,
    val trainer_id: Int,
    val user: ClientCover,
    val user_confirm: Boolean?,
    val user_id: Int
)
