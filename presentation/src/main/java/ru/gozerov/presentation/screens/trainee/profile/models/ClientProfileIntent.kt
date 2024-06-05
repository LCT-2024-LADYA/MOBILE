package ru.gozerov.presentation.screens.trainee.profile.models

import android.net.Uri

sealed interface ClientProfileIntent {

    object Reset : ClientProfileIntent

    object GetInfo : ClientProfileIntent

    data class FillProfile(
        val id: Int,
        val age: Int,
        val sex: Int,
        val email: String
    ) : ClientProfileIntent

    data class UpdateInfo(
        val id: Int,
        val firstName: String,
        val lastName: String,
        val photoUri: Uri?
    ) : ClientProfileIntent

}