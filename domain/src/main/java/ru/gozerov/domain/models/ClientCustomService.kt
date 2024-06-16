package ru.gozerov.domain.models

data class ClientCustomService(
    val id: Int,
    val isPayed: Boolean,
    val service: TrainerService,
    val serviceId: Int,
    val isTrainerApproved: Boolean?,
    val trainerId: Int,
    val trainer: TrainerCard,
    val isClientApproved: Boolean?,
    val userId: Int
)