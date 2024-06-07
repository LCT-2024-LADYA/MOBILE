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

    object TrainerProfile : Screen("trainerProfile")

    object TraineeTabs : Screen("traineeTabs")

    object MainTraining : Screen("mainTraining")

    object TrainingProcess : Screen("trainingProcess")


}