package ru.gozerov.domain.models

data class CustomService(
    val id: Int,
    val isPayed: Boolean,
    val service: TrainerService,
    val serviceId: Int,
    val isTrainerApproved: Boolean?,
    val trainerId: Int,
    val user: UserCard,
    val isClientApproved: Boolean?,
    val userId: Int
)
