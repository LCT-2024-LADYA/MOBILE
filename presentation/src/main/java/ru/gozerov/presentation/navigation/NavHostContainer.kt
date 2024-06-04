package ru.gozerov.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.gozerov.presentation.screens.login.LoginScreen
import ru.gozerov.presentation.screens.login.LoginViewModel

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