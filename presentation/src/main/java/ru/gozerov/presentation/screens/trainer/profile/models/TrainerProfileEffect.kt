package ru.gozerov.presentation.screens.trainer.profile.models

import ru.gozerov.domain.models.Role
import ru.gozerov.domain.models.Specialization
import ru.gozerov.domain.models.TrainerInfo

sealed interface TrainerProfileEffect {

    object None : TrainerProfileEffect

    data class LoadedRoles(val roles: List<Role>) : TrainerProfileEffect

    data class LoadedSpecializations(val specializations: List<Specialization>) :
        TrainerProfileEffect

    data class LoadedProfile(val mainInfo: TrainerInfo) : TrainerProfileEffect

    data class SuccessCreatedAchievement(val id: Int, val name: String) : TrainerProfileEffect

    data class SuccessCreatedService(
        val id: Int,
        val name: String,
        val price: Int,
        val profileAccess: Boolean
    ) :
        TrainerProfileEffect

    data object SuccessPhotoUpload : TrainerProfileEffect

    data object SuccessUpdatedProfile : TrainerProfileEffect

    data object RemovedPhoto : TrainerProfileEffect

    class Error(val message: String) : TrainerProfileEffect

    object Logout : TrainerProfileEffect

}