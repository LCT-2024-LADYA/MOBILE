package ru.gozerov.presentation.screens.login.register_trainee.models

sealed interface RegisterIntent {

    object Navigate : RegisterIntent

    data class Register(
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val age: Int,
        val sex: Int
    ) : RegisterIntent

}