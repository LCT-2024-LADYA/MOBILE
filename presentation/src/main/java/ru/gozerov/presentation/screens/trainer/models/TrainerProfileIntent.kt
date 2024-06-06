package ru.gozerov.presentation.screens.trainer.models

import android.net.Uri
import ru.gozerov.domain.models.TrainerMainInfoDTO

sealed interface TrainerProfileIntent {

    data object Reset : TrainerProfileIntent

    data object GetMainInfo : TrainerProfileIntent

    data object GetSpecializations : TrainerProfileIntent

    data object GetRoles : TrainerProfileIntent

    data class UpdateProfile(
        val trainerMainInfoDTO: TrainerMainInfoDTO,
        val roles: List<Int>,
        val specializations: List<Int>
    ) : TrainerProfileIntent

    data class UpdatePhoto(
        val uri: Uri
    ) : TrainerProfileIntent

    data class CreateService(val name: String, val price: Int) : TrainerProfileIntent

    data class RemoveService(val id: Int) : TrainerProfileIntent

    data class CreateAchievement(val name: String) : TrainerProfileIntent

    data class RemoveAchievement(val id: Int) : TrainerProfileIntent

}