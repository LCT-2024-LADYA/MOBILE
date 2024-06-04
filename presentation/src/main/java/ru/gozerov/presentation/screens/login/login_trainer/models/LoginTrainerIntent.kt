package ru.gozerov.presentation.screens.login.login_trainer.models

sealed interface LoginTrainerIntent {

    data class Login(
        val email: String,
        val password: String
    ) : LoginTrainerIntent

}