package ru.gozerov.presentation.screens.login.login_trainer.models

sealed interface LoginTrainerEffect {

    object None : LoginTrainerEffect

    object SuccessLogin : LoginTrainerEffect

    class Error : LoginTrainerEffect

}