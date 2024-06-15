package ru.gozerov.domain.models

data class PayedService(
    val id: Int,
    val service: TrainerService,
    val isPayed: Boolean,
    val isTrainerApproved: Boolean?,
    val isClientApproved: Boolean?,
    val trainerId: Int,
    val userId: Int,
    val user: UserCard
)
