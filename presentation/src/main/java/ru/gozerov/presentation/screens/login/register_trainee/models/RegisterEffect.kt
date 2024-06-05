package ru.gozerov.presentation.screens.login.register_trainee.models

sealed interface RegisterEffect {

    object None : RegisterEffect

    object SuccessLoginTrainee: RegisterEffect

    class Error(val message: String) : RegisterEffect

}