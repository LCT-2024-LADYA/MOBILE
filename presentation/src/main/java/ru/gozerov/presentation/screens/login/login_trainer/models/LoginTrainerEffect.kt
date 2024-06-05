package ru.gozerov.presentation.screens.login.login_trainer.models

sealed interface LoginTrainerEffect {

    object None : LoginTrainerEffect

    object SuccessLogin : LoginTrainerEffect

    data class Error(val message: String) : LoginTrainerEffect

}