package ru.gozerov.presentation.screens.trainee.profile.profile.models

import android.net.Uri

sealed interface ClientProfileIntent {

    object Reset : ClientProfileIntent

    object GetInfo : ClientProfileIntent

    data class UpdateInfo(
        val age: Int,
        val sex: Int,
        val email: String,
        val firstName: String,
        val lastName: String,
    ) : ClientProfileIntent

    data class UpdatePhoto(
        val photoUri: Uri
    ) : ClientProfileIntent

    object Logout : ClientProfileIntent

}