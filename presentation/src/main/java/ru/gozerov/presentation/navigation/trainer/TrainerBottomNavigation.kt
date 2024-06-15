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
import ru.gozerov.presentation.screens.trainee.diary.create_training.CreateTrainingScreen
import ru.gozerov.presentation.screens.trainee.diary.create_training.CreateTrainingViewModel
import ru.gozerov.presentation.screens.trainee.diary.find_training.FindTrainingScreen
import ru.gozerov.presentation.screens.trainee.diary.find_training.FindTrainingViewModel
import ru.gozerov.presentation.screens.trainee.diary.select_training.SelectTrainingScreen
import ru.gozerov.presentation.screens.trainee.diary.select_training.SelectTrainingViewModel
import ru.gozerov.presentation.screens.trainer.chat.list.TrainerChatListScreen
import ru.gozerov.presentation.screens.trainer.chat.list.TrainerChatListViewModel
import ru.gozerov.presentation.screens.trainer.diary.create_plan.CreatePlanScreen
import ru.gozerov.presentation.screens.trainer.diary.create_plan.CreatePlanViewModel
import ru.gozerov.presentation.screens.trainer.diary.create_service.CreateServiceScreen
import ru.gozerov.presentation.screens.trainer.diary.create_service.CreateServiceViewModel
import ru.gozerov.presentation.screens.trainer.diary.diary.TrainerDiaryScreen
import ru.gozerov.presentation.screens.trainer.diary.diary.TrainerDiaryViewModel
import ru.gozerov.presentation.screens.trainer.diary.find_training.FindTrainerTrainingScreen
import ru.gozerov.presentation.screens.trainer.diary.training_details.TrainerTrainingDetailsScreen
import ru.gozerov.presentation.screens.trainer.diary.training_details.TrainerTrainingDetailsViewModel
import ru.gozerov.presentation.screens.trainer.profile.TrainerProfileScreen
import ru.gozerov.presentation.screens.trainer.profile.TrainerProfileViewModel
import ru.gozerov.presentation.screens.trainer.service.TrainerServicesScreen
import ru.gozerov.presentation.screens.trainer.service.TrainerServicesViewModel

sealed class TrainerBottomNavBarItem(
    val route: String,
    @DrawableRes val iconId: Int
) {

    object ServiceFlow : TrainerBottomNavBarItem("serviceFlow", R.drawable.ic_people)

    object ChatFlow : TrainerBottomNavBarItem("trainerChatFlow", R.drawable.ic_chat)
    object DiaryFlow : TrainerBottomNavBarItem("trainerDiaryFlow", R.drawable.ic_diary)
    object Profile : TrainerBottomNavBarItem("trainerProfileFlow", R.drawable.ic_account)
}

val trainerBottomNavBarItems =
    listOf(
        TrainerBottomNavBarItem.ServiceFlow,
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
        startDestination = TrainerBottomNavBarItem.ServiceFlow.route,
        builder = {

            navigation(Screen.TrainerServices.route, TrainerBottomNavBarItem.ServiceFlow.route) {
                composable(Screen.TrainerServices.route) {
                    val viewModel = hiltViewModel<TrainerServicesViewModel>()
                    TrainerServicesScreen(padding, viewModel)
                }
            }

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
                    val viewModel = hiltViewModel<TrainerDiaryViewModel>()
                    TrainerDiaryScreen(
                        contentPaddingValues = padding,
                        navController = navController,
                        viewModel = viewModel
                    )
                }

                composable(
                    route = Screen.CreateService.route
                ) {
                    val viewModel = hiltViewModel<CreateServiceViewModel>()
                    val date =
                        navController.previousBackStackEntry?.savedStateHandle?.get<String>("date")
                    CreateServiceScreen(
                        contentPaddingValues = padding,
                        navController = navController,
                        viewModel = viewModel,
                        serviceDate = date
                    )
                }

                composable(
                    route = Screen.CreatePlan.route
                ) {
                    val id =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Int>("id")
                    id?.let {
                        val viewModel = hiltViewModel<CreatePlanViewModel>()

                        CreatePlanScreen(
                            navController = navController,
                            viewModel = viewModel,
                            contentPaddingValues = padding,
                            userId = id
                        )
                    }
                }


                composable(
                    route = Screen.SelectTraining.route
                ) {
                    val viewModel = hiltViewModel<SelectTrainingViewModel>()
                    SelectTrainingScreen(
                        navController = navController,
                        contentPaddingValues = padding,
                        viewModel = viewModel
                    )
                }

                composable(
                    route = Screen.TrainerTrainingDetails.route
                ) {
                    val training =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Int>(
                            "id"
                        )
                    training?.let {
                        val viewModel = hiltViewModel<TrainerTrainingDetailsViewModel>()
                        TrainerTrainingDetailsScreen(
                            paddingValues = padding,
                            navController = navController,
                            trainingId = training,
                            viewModel = viewModel
                        )
                    }
                }

                composable(
                    route = Screen.FindTraining.route
                ) {
                    val viewModel = hiltViewModel<SelectTrainingViewModel>()
                    FindTrainerTrainingScreen(
                        navController = navController,
                        contentPaddingValues = padding,
                        viewModel = viewModel
                    )
                }

                composable(
                    route = Screen.CreateTraining.route
                ) {
                    val id = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("id")
                    val date =
                        navController.previousBackStackEntry?.savedStateHandle?.get<String>("date")
                    val viewModel = hiltViewModel<CreateTrainingViewModel>()
                    CreateTrainingScreen(
                        isTrainer = true,
                        trainingId = id,
                        trainingDate = date,
                        parentNavController = rootNavController,
                        navController = navController,
                        contentPaddingValues = padding,
                        viewModel = viewModel,
                        backRoute = Screen.TrainerDiary.route
                    )
                }


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