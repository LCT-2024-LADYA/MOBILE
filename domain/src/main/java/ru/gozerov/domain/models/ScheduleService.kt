package ru.gozerov.domain.models

data class ScheduleService(
    val date: String,
    val id: Int,
    val isPayed: Boolean,
    val scheduleId: Int,
    val service: TrainerService,
    val serviceId: Int,
    val timeStart: String,
    val timeEnd: String,
    val isTrainerApproved: Boolean?,
    val trainerId: Int,
    val user: UserCard,
    val isClientApproved: Boolean?,
    val userId: Int
)
