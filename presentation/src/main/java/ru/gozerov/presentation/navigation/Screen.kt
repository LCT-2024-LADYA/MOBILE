package ru.gozerov.presentation.navigation

import androidx.annotation.DrawableRes

sealed class Screen(
    val route: String,
    @DrawableRes val icon: Int? = null
) {

    object ChoiceLogin : Screen("choiceLogin")

    object LoginTrainer : Screen("loginTrainer")

    object Register : Screen("register")

    object RegisterProfile : Screen("registerProfile")

    object LoginTrainee : Screen("loginTrainee")

    object ClientProfile : Screen("clientProfile")

    object TrainerProfile : Screen("trainerProfile")

}