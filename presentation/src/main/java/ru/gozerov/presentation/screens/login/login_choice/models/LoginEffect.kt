package ru.gozerov.presentation.screens.login.login_choice.models

sealed interface LoginEffect {

    object None : LoginEffect

    data class SuccessLogin(
        val token: String,
        val vkId: Long
    ) : LoginEffect

    class Error : LoginEffect

}