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
import ru.gozerov.presentation.screens.login.login_trainer.LoginTrainerScreen
import ru.gozerov.presentation.screens.login.login_trainer.LoginTrainerViewModel
import ru.gozerov.presentation.screens.login.register_trainee.RegisterProfileScreen
import ru.gozerov.presentation.screens.login.register_trainee.RegisterScreen
import ru.gozerov.presentation.screens.login.register_trainee.RegisterViewModel
import ru.gozerov.presentation.screens.trainee.profile.ClientProfileScreen
import ru.gozerov.presentation.screens.trainer.TrainerProfileScreen

@Composable
fun NavHostContainer(
    startDestination: String,
    navController: NavHostController,
    padding: PaddingValues,
) {
    /*val startDestination =
        if (isLoginNeeded) Screen.ChooseAccount.route else Screen.MainSection.route*/

    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = {
            composable(
                route = Screen.ChoiceLogin.route
            ) {
                ChoiceLoginScreen(navController = navController)
            }

            composable(
                route = Screen.LoginTrainer.route
            ) {
                val viewModel = hiltViewModel<LoginTrainerViewModel>()
                LoginTrainerScreen(navController = navController, viewModel = viewModel)
            }

            composable(
                route = Screen.Register.route
            ) {
                RegisterScreen(navController = navController)
            }

            composable(
                route = Screen.LoginTrainee.route
            ) {
                val viewModel = hiltViewModel<LoginTraineeViewModel>()
                LoginTraineeScreen(navController, viewModel)
            }

            composable(
                route = Screen.ClientProfile.route
            ) {
                ClientProfileScreen(navController)
            }

            composable(
                route = Screen.RegisterProfile.route + "/{email}/{password}"
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email")
                    ?: throw IllegalArgumentException("no args")
                val password = backStackEntry.arguments?.getString("password")
                    ?: throw IllegalArgumentException("no args")
                val viewModel = hiltViewModel<RegisterViewModel>()
                RegisterProfileScreen(
                    navController = navController,
                    viewModel = viewModel,
                    email,
                    password
                )
            }

            composable(
                route = Screen.TrainerProfile.route
            ) {
                TrainerProfileScreen(navController)
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