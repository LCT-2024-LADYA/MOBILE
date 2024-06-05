package ru.gozerov.presentation.navigation

import androidx.annotation.DrawableRes

sealed class Screen(
    val route: String,
    @DrawableRes val icon: Int? = null
) {

    object Login : Screen("login")

    object LoginTrainer : Screen("loginTrainer")

    object Register : Screen("register")

    object LoginTrainee : Screen("loginTrainee")


}