package ru.gozerov.presentation.screens.login.login_trainer.models

sealed interface LoginTrainerIntent {

    object Reset : LoginTrainerIntent

    data class Login(
        val email: String,
        val password: String
    ) : LoginTrainerIntent

}