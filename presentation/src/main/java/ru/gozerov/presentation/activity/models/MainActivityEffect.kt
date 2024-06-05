package ru.gozerov.presentation.activity.models

sealed interface MainActivityEffect {

    object None : MainActivityEffect

    data class AuthResult(
        val navigateToLogin: Boolean,
        val isClient: Boolean
    ) : MainActivityEffect

    data class Error(
        val message: String
    ) : MainActivityEffect

}