package ru.gozerov.presentation.screens.login.verify_email.models

sealed interface VerifyEmailEffect {

    object None: VerifyEmailEffect

    object SuccessLogin: VerifyEmailEffect

    class Error: VerifyEmailEffect

}