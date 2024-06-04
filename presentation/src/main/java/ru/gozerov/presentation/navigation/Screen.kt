package ru.gozerov.presentation.navigation

import androidx.annotation.DrawableRes

sealed class Screen(
    val route: String,
    @DrawableRes val icon: Int? = null
) {

    object Login : Screen("login")

    object VerifyEmail : Screen("verifyEmail")

    object LoginTrainer : Screen("loginTrainer")

}