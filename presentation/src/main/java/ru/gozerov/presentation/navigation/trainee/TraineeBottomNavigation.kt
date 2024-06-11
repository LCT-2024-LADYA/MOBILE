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
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.chat.list.ChatListScreen
import ru.gozerov.presentation.screens.trainee.diary.create_training.CreateTrainingScreen
import ru.gozerov.presentation.screens.trainee.diary.create_training.CreateTrainingViewModel
import ru.gozerov.presentation.screens.trainee.diary.diary.DiaryScreen
import ru.gozerov.presentation.screens.trainee.diary.diary.DiaryViewModel
import ru.gozerov.presentation.screens.trainee.diary.find_training.FindTrainingScreen
import ru.gozerov.presentation.screens.trainee.diary.find_training.FindTrainingViewModel
import ru.gozerov.presentation.screens.trainee.main_training.main_training.MainTrainingScreen
import ru.gozerov.presentation.screens.trainee.main_training.main_training.MainTrainingViewModel
import ru.gozerov.presentation.screens.trainee.main_training.process.TrainingProcessScreen
import ru.gozerov.presentation.screens.trainee.main_training.process.TrainingProcessViewModel
import ru.gozerov.presentation.screens.trainee.main_training.process.end.EndTrainingScreen
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
                    val viewModel = hiltViewModel<MainTrainingViewModel>()
                    MainTrainingScreen(
                        navController = navController,
                        contentPaddingValues = padding,
                        viewModel = viewModel
                    )
                }
                composable(
                    route = Screen.TrainingProcess.route
                ) { _ ->
                    val training =
                        navController.previousBackStackEntry?.savedStateHandle?.get<CustomTraining>(
                            "training"
                        )
                    val viewModel = hiltViewModel<TrainingProcessViewModel>()
                    training?.let {
                        TrainingProcessScreen(
                            navController = navController,
                            contentPaddingValues = padding,
                            training = training,
                            viewModel = viewModel
                        )
                    }
                }
                composable(
                    route = Screen.EndTraining.route
                ) {
                    EndTrainingScreen(
                        navController = navController,
                        contentPaddingValues = padding
                    )
                }
                composable(
                    route = Screen.CreateTraining.route
                ) {
                    val viewModel = hiltViewModel<CreateTrainingViewModel>()
                    CreateTrainingScreen(
                        parentNavController = rootNavController,
                        navController = navController,
                        contentPaddingValues = padding,
                        viewModel = viewModel
                    )
                }

                composable(
                    route = Screen.FindTraining.route
                ) {
                    val viewModel = hiltViewModel<FindTrainingViewModel>()
                    FindTrainingScreen(
                        navController = navController,
                        contentPaddingValues = padding,
                        viewModel = viewModel
                    )
                }
            }

            composable(
                route = TraineeBottomNavBarItem.ChatFlow.route,
            ) {
                ChatListScreen()
            }

            composable(
                route = TraineeBottomNavBarItem.DiaryFlow.route
            ) {
                val viewModel = hiltViewModel<DiaryViewModel>()
                DiaryScreen(
                    viewModel = viewModel,
                    contentPaddingValues = padding,
                    navController = navController
                )
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