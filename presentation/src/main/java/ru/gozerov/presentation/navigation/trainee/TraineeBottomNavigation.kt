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
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainingPlan
import ru.gozerov.domain.models.TrainingPlanCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.chat.list.ChatListScreen
import ru.gozerov.presentation.screens.trainee.chat.list.ChatListViewModel
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
import ru.gozerov.presentation.screens.trainee.main_training.training_details.TrainingDetailsScreen
import ru.gozerov.presentation.screens.trainee.profile.profile.ClientProfileScreen
import ru.gozerov.presentation.screens.trainee.profile.profile.ClientProfileViewModel
import ru.gozerov.presentation.screens.trainee.profile.statistics.ClientStatisticsScreen
import ru.gozerov.presentation.screens.trainee.profile.statistics.ClientStatisticsViewModel
import ru.gozerov.presentation.screens.trainer.diary.diary_plan.SchedulePlanScreen
import ru.gozerov.presentation.screens.trainer.diary.diary_plan.SchedulePlanViewModel
import ru.gozerov.presentation.screens.trainer.diary.paste_training.PasteTrainingScreen
import ru.gozerov.presentation.screens.trainer.diary.paste_training.PasteTrainingViewModel
import ru.gozerov.presentation.screens.trainer.diary.plan_details.PlanDetailsScreen
import ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.TrainingToScheduleScreen
import ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.TrainingToScheduleViewModel

sealed class TraineeBottomNavBarItem(
    val route: String,
    @DrawableRes val iconId: Int
) {
    object MainFlow : TraineeBottomNavBarItem("main", R.drawable.ic_run_man)
    object ChatFlow : TraineeBottomNavBarItem("chat", R.drawable.ic_chat)
    object DiaryFlow : TraineeBottomNavBarItem("diary", R.drawable.ic_diary)
    object ProfileFlow : TraineeBottomNavBarItem("profile", R.drawable.ic_account)
}

val traineeBottomNavBarItems =
    listOf(
        TraineeBottomNavBarItem.MainFlow,
        TraineeBottomNavBarItem.ChatFlow,
        TraineeBottomNavBarItem.DiaryFlow,
        TraineeBottomNavBarItem.ProfileFlow
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
                    val id = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("id")
                    val date =
                        navController.previousBackStackEntry?.savedStateHandle?.get<String>("date")
                    val viewModel = hiltViewModel<CreateTrainingViewModel>()
                    CreateTrainingScreen(
                        trainingId = id,
                        isTrainer = false,
                        trainingDate = date,
                        parentNavController = rootNavController,
                        navController = navController,
                        contentPaddingValues = padding,
                        viewModel = viewModel,
                        backRoute = Screen.MainTraining.route
                    )
                }

                composable(
                    route = Screen.FindTraining.route
                ) {
                    val viewModel = hiltViewModel<FindTrainingViewModel>()
                    val date =
                        navController.previousBackStackEntry?.savedStateHandle?.get<String>("date")
                    FindTrainingScreen(
                        navController = navController,
                        contentPaddingValues = padding,
                        viewModel = viewModel,
                        date = date
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
                }

                composable(
                    route = Screen.PasteTraining.route
                ) {
                    val training =
                        navController.previousBackStackEntry?.savedStateHandle?.get<CustomTraining>(
                            "training"
                        )
                    val trainings =
                        navController.previousBackStackEntry?.savedStateHandle?.get<List<ScheduledTraining>>(
                            "trainings"
                        )
                    if (trainings != null && training != null) {
                        val viewModel = hiltViewModel<PasteTrainingViewModel>()
                        PasteTrainingScreen(
                            contentPaddingValues = padding,
                            scheduledTrainings = trainings,
                            training = training,
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }

                composable(
                    route = Screen.PlanDetailsScreen.route
                ) {
                    val plan =
                        navController.previousBackStackEntry?.savedStateHandle?.get<TrainingPlan>(
                            "plan"
                        )
                    plan?.let {
                        PlanDetailsScreen(
                            contentPaddingValues = padding,
                            plan = plan,
                            navController = navController
                        )
                    }
                }
            }


            navigation(Screen.ClientChatList.route, TraineeBottomNavBarItem.ChatFlow.route) {

                composable(
                    route = Screen.ClientChatList.route,
                ) {
                    val viewModel = hiltViewModel<ChatListViewModel>()
                    ChatListScreen(
                        contentPaddingValues = padding,
                        parentNavController = rootNavController,
                        viewModel = viewModel
                    )
                }

            }

            navigation(Screen.ClientDiary.route, TraineeBottomNavBarItem.DiaryFlow.route) {

                composable(
                    route = Screen.ClientDiary.route
                ) {
                    val viewModel = hiltViewModel<DiaryViewModel>()
                    DiaryScreen(
                        viewModel = viewModel,
                        contentPaddingValues = padding,
                        navController = navController
                    )
                }

                composable(
                    route = Screen.SchedulePlan.route
                ) {
                    val trainings =
                        navController.previousBackStackEntry?.savedStateHandle?.get<List<ScheduledTraining>>(
                            "trainings"
                        )
                    val plan =
                        navController.previousBackStackEntry?.savedStateHandle?.get<TrainingPlanCard>(
                            "plan"
                        )
                    if (plan != null && trainings != null) {
                        val viewModel = hiltViewModel<SchedulePlanViewModel>()
                        SchedulePlanScreen(navController, padding, viewModel, plan, trainings)
                    }
                }

                composable(
                    route = Screen.TrainingToSchedule.route
                ) {
                    val start = navController.previousBackStackEntry?.savedStateHandle?.get<Int>(
                        "start"
                    )
                    val end = navController.previousBackStackEntry?.savedStateHandle?.get<Int>(
                        "end"
                    )
                    val month = navController.previousBackStackEntry?.savedStateHandle?.get<Int>(
                        "month"
                    )
                    val plan =
                        navController.previousBackStackEntry?.savedStateHandle?.get<TrainingPlanCard>(
                            "plan"
                        )
                    if (start != null && end != null && month != null && plan != null) {
                        val viewModel = hiltViewModel<TrainingToScheduleViewModel>()
                        TrainingToScheduleScreen(
                            start,
                            end,
                            month,
                            plan,
                            navController,
                            padding,
                            Screen.ClientDiary.route,
                            viewModel
                        )
                    }
                }

                composable(
                    route = Screen.FindTraining.route
                ) {
                    val viewModel = hiltViewModel<FindTrainingViewModel>()
                    val date =
                        navController.previousBackStackEntry?.savedStateHandle?.get<String>("date")
                    FindTrainingScreen(
                        navController = navController,
                        contentPaddingValues = padding,
                        viewModel = viewModel,
                        date = date
                    )
                }

                composable(
                    route = Screen.CreateTraining.route
                ) {
                    val id = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("id")
                    val date =
                        navController.previousBackStackEntry?.savedStateHandle?.get<String>("date")
                    val viewModel = hiltViewModel<CreateTrainingViewModel>()

                    val backRoute =
                        navController.previousBackStackEntry?.savedStateHandle?.get<String>("backRoute")
                    CreateTrainingScreen(
                        trainingId = id,
                        isTrainer = false,
                        trainingDate = date,
                        parentNavController = rootNavController,
                        navController = navController,
                        contentPaddingValues = padding,
                        viewModel = viewModel,
                        backRoute = backRoute ?: Screen.ClientDiary.route
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
                }

            }

            navigation(Screen.TraineeProfile.route, TraineeBottomNavBarItem.ProfileFlow.route) {
                composable(
                    route = Screen.TraineeProfile.route
                ) {
                    val viewModel = hiltViewModel<ClientProfileViewModel>()
                    ClientProfileScreen(rootNavController, navController, viewModel, padding)
                }

                composable(
                    route = Screen.ClientStatisticsScreen.route
                ) {
                    val viewModel = hiltViewModel<ClientStatisticsViewModel>()
                    ClientStatisticsScreen(
                        navController = navController,
                        viewModel = viewModel,
                        contentPaddingValues = padding
                    )
                }
            }
        }
    )

}