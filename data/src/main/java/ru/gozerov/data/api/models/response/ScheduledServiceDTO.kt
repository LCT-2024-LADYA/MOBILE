package ru.gozerov.data.api.models.response

import ru.gozerov.domain.models.TrainerService

data class ScheduledServiceDTO(
    val date: String,
    val id: Int,
    val is_payed: Boolean,
    val schedule_id: Int,
    val service: TrainerService,
    val service_id: Int,
    val time_start: String,
    val time_end: String,
    val trainer_confirm: Boolean?,
    val trainer_id: Int,
    val user: ClientCover,
    val user_confirm: Boolean?,
    val user_id: Int
)
