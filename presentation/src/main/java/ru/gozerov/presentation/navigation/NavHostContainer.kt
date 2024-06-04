package ru.gozerov.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.gozerov.presentation.screens.login.login_choice.LoginScreen
import ru.gozerov.presentation.screens.login.login_choice.LoginViewModel
import ru.gozerov.presentation.screens.login.login_trainer.LoginTrainerScreen
import ru.gozerov.presentation.screens.login.login_trainer.LoginTrainerViewModel
import ru.gozerov.presentation.screens.login.verify_email.VerifyEmailScreen
import ru.gozerov.presentation.screens.login.verify_email.VerifyEmailViewModel

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
                val loginViewModel = hiltViewModel<LoginViewModel>()
                LoginScreen(
                    navController = navController,
                    viewModel = loginViewModel
                )
            }

            composable(
                route = Screen.VerifyEmail.route + "/{token}/{vkId}"
            ) { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: ""
                val vkId = backStackEntry.arguments?.getString("vkId")?.toLong() ?: 0L
                val viewModel = hiltViewModel<VerifyEmailViewModel>()
                VerifyEmailScreen(accessToken = token, vkId = vkId, viewModel = viewModel)
            }

            composable(
                route = Screen.LoginTrainer.route
            ) {
                val viewModel = hiltViewModel<LoginTrainerViewModel>()
                LoginTrainerScreen(viewModel = viewModel)
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