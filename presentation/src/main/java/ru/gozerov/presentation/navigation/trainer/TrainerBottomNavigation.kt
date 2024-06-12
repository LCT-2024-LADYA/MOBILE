package ru.gozerov.presentation.navigation.trainer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainer.chat.list.TrainerChatListScreen
import ru.gozerov.presentation.screens.trainer.chat.list.TrainerChatListViewModel
import ru.gozerov.presentation.screens.trainer.diary.diary.TrainerDiaryScreen
import ru.gozerov.presentation.screens.trainer.profile.TrainerProfileScreen
import ru.gozerov.presentation.screens.trainer.profile.TrainerProfileViewModel

sealed class TrainerBottomNavBarItem(
    val route: String,
    @DrawableRes val iconId: Int
) {

    object ChatFlow : TrainerBottomNavBarItem("trainerChatFlow", R.drawable.ic_chat)
    object DiaryFlow : TrainerBottomNavBarItem("trainerDiaryFlow", R.drawable.ic_diary)
    object Profile : TrainerBottomNavBarItem("trainerProfileFlow", R.drawable.ic_account)
}

val trainerBottomNavBarItems =
    listOf(
        TrainerBottomNavBarItem.ChatFlow,
        TrainerBottomNavBarItem.DiaryFlow,
        TrainerBottomNavBarItem.Profile
    )

@Composable
fun TrainerBottomNavHostContainer(
    navController: NavHostController,
    rootNavController: NavController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = TrainerBottomNavBarItem.ChatFlow.route,
        builder = {

            navigation(Screen.TrainerChatList.route, TrainerBottomNavBarItem.ChatFlow.route) {

                composable(
                    route = Screen.TrainerChatList.route,
                ) {
                    val viewModel = hiltViewModel<TrainerChatListViewModel>()
                    TrainerChatListScreen(
                        contentPaddingValues = padding,
                        parentNavController = rootNavController,
                        viewModel = viewModel
                    )
                }

            }

            navigation(Screen.TrainerDiary.route, TrainerBottomNavBarItem.DiaryFlow.route) {

                composable(
                    route = Screen.TrainerDiary.route
                ) {
                    //val viewModel = hiltViewModel<DiaryViewModel>()
                    TrainerDiaryScreen(
                        // viewModel = viewModel,
                        contentPaddingValues = padding,
                        // navController = navController
                    )
                }
                /*
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

                                composable(
                                    route = Screen.CreateTraining.route
                                ) {
                                    val id = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("id")
                                    val viewModel = hiltViewModel<CreateTrainingViewModel>()
                                    CreateTrainingScreen(
                                        trainingId = id,
                                        parentNavController = rootNavController,
                                        navController = navController,
                                        contentPaddingValues = padding,
                                        viewModel = viewModel
                                    )
                                }

                                composable(
                                    route = Screen.TrainingDetails.route
                                ) {
                                    val training =
                                        navController.previousBackStackEntry?.savedStateHandle?.get<CustomTraining>(
                                            "training"
                                        )
                                    training?.let {
                                        TrainingDetailsScreen(
                                            navController = navController,
                                            training = training
                                        )
                                    }
                                }*/

            }

            composable(
                route = TrainerBottomNavBarItem.Profile.route
            ) {
                val viewModel = hiltViewModel<TrainerProfileViewModel>()
                TrainerProfileScreen(rootNavController, navController, viewModel, padding)
            }
        }
    )

}