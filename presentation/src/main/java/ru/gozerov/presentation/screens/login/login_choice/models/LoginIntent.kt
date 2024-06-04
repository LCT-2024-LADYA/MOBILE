package ru.gozerov.presentation.screens.login.login_choice.models

sealed interface LoginIntent {

    object LoginThroughVK : LoginIntent

    object Navigate : LoginIntent

}