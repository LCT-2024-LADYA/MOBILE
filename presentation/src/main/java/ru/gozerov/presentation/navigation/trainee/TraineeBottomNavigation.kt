package ru.gozerov.presentation.navigation.trainee

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.gozerov.domain.models.Training
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.chat.ChatListScreen
import ru.gozerov.presentation.screens.trainee.diary.DiaryScreen
import ru.gozerov.presentation.screens.trainee.main_training.MainTrainingScreen
import ru.gozerov.presentation.screens.trainee.main_training.process.TrainingProcessScreen
import ru.gozerov.presentation.screens.trainee.profile.ClientProfileScreen
import ru.gozerov.presentation.screens.trainee.profile.ClientProfileViewModel

sealed class TraineeBottomNavBarItem(
    val route: String,
    @DrawableRes val iconId: Int
) {
    object MainFlow : TraineeBottomNavBarItem("main", R.drawable.ic_run_man)
    object ChatFlow : TraineeBottomNavBarItem("chat", R.drawable.ic_chat)
    object DiaryFlow : TraineeBottomNavBarItem("diary", R.drawable.ic_diary)
    object Profile : TraineeBottomNavBarItem("profile", R.drawable.ic_account)
}

val traineeBottomNavBarItems =
    listOf(
        TraineeBottomNavBarItem.MainFlow,
        TraineeBottomNavBarItem.ChatFlow,
        TraineeBottomNavBarItem.DiaryFlow,
        TraineeBottomNavBarItem.Profile
    )

@Composable
fun TraineeBottomNavHostContainer(
    navController: NavHostController,
    rootNavController: NavController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = TraineeBottomNavBarItem.MainFlow.route,
        builder = {

            navigation(Screen.MainTraining.route, TraineeBottomNavBarItem.MainFlow.route) {
                composable(
                    route = Screen.MainTraining.route
                ) {
                    MainTrainingScreen(
                        navController = navController,
                        contentPaddingValues = padding
                    )
                }
                composable(
                    route = Screen.TrainingProcess.route
                ) { _ ->
                    val training =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Training>("training")
                    training?.let {
                        TrainingProcessScreen(
                            navController = navController,
                            contentPaddingValues = padding,
                            training = training
                        )
                    }
                }
            }
            
            composable(
                route = TraineeBottomNavBarItem.ChatFlow.route,
            ) {
                ChatListScreen()
            }

            composable(
                route = TraineeBottomNavBarItem.DiaryFlow.route
            )
            {
                DiaryScreen()
            }

            composable(
                route = TraineeBottomNavBarItem.Profile.route
            ) {
                val viewModel = hiltViewModel<ClientProfileViewModel>()
                ClientProfileScreen(rootNavController, navController, viewModel, padding)
            }
        }
    )

}