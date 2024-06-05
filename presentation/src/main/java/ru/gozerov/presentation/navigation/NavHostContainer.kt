package ru.gozerov.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.gozerov.presentation.screens.login.login_choice.ChoiceLoginScreen
import ru.gozerov.presentation.screens.login.login_trainee.LoginTraineeScreen
import ru.gozerov.presentation.screens.login.login_trainee.LoginTraineeViewModel
import ru.gozerov.presentation.screens.login.register_trainee.RegisterScreen
import ru.gozerov.presentation.screens.login.register_trainee.RegisterViewModel
import ru.gozerov.presentation.screens.login.login_trainer.LoginTrainerScreen
import ru.gozerov.presentation.screens.login.login_trainer.LoginTrainerViewModel

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
    isLoginNeeded: Boolean
) {
    /*val startDestination =
        if (isLoginNeeded) Screen.ChooseAccount.route else Screen.MainSection.route*/

    val startDestination = Screen.Login.route
    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = {
            composable(
                route = Screen.Login.route
            ) {
                ChoiceLoginScreen(navController = navController)
            }

            composable(
                route = Screen.LoginTrainer.route
            ) {
                val viewModel = hiltViewModel<LoginTrainerViewModel>()
                LoginTrainerScreen(viewModel = viewModel)
            }

            composable(
                route = Screen.Register.route
            ) {
                val viewModel = hiltViewModel<RegisterViewModel>()
                RegisterScreen(navController = navController, viewModel = viewModel)
            }

            composable(
                route = Screen.LoginTrainee.route
            ) {
                val viewModel = hiltViewModel<LoginTraineeViewModel>()
                LoginTraineeScreen(navController, viewModel)
            }
            /* composable(
                 route = Screen.MainSection.route,
                 enterTransition = { enterAnimation() },
                 exitTransition = { exitAnimation() }
             ) {
                 MainSection(rootNavController = navController, padding)
             }*/
        }
    )

}