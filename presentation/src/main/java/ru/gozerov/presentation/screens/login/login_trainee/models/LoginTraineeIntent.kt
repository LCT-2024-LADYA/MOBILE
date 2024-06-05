package ru.gozerov.presentation.screens.login.login_trainee.models

sealed interface LoginTraineeIntent {

    object Reset : LoginTraineeIntent

    data class Login(
        val email: String,
        val password: String
    ) : LoginTraineeIntent

}