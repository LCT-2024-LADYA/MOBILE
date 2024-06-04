package ru.gozerov.presentation.screens.login.verify_email.models

sealed interface VerifyEmailIntent {

    data class VerifyEmail(
        val accessToken: String,
        val vkId: Long,
        val email: String
    ) : VerifyEmailIntent

}