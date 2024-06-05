package ru.gozerov.presentation.screens.login.login_trainee.models

sealed interface LoginTraineeEffect {

    object None : LoginTraineeEffect

    object SuccessLogin : LoginTraineeEffect

    data class Error(val message: String) : LoginTraineeEffect

}