package ru.gozerov.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.presentation.screens.login.login_choice.ChoiceLoginScreen
import ru.gozerov.presentation.screens.login.login_trainee.LoginTraineeScreen
import ru.gozerov.presentation.screens.login.login_trainee.LoginTraineeViewModel
import ru.gozerov.presentation.screens.login.login_trainer.LoginTrainerScreen
import ru.gozerov.presentation.screens.login.login_trainer.LoginTrainerViewModel
import ru.gozerov.presentation.screens.login.register_trainee.RegisterProfileScreen
import ru.gozerov.presentation.screens.login.register_trainee.RegisterScreen
import ru.gozerov.presentation.screens.login.register_trainee.RegisterViewModel
import ru.gozerov.presentation.screens.trainee.chat.chat.ChatScreen
import ru.gozerov.presentation.screens.trainee.chat.chat.ChatViewModel
import ru.gozerov.presentation.screens.trainee.diary.find_exercise.FindExerciseScreen
import ru.gozerov.presentation.screens.trainee.diary.find_exercise.FindExerciseViewModel
import ru.gozerov.presentation.screens.trainee.tabs.TraineeTabsScreen
import ru.gozerov.presentation.screens.trainer.chat.chat.TrainerChatScreen
import ru.gozerov.presentation.screens.trainer.chat.chat.TrainerChatViewModel
import ru.gozerov.presentation.screens.trainer.tabs.TrainerTabsScreen
import ru.gozerov.presentation.shared.screens.client_card.ClientCardScreen
import ru.gozerov.presentation.shared.screens.client_card.ClientCardViewModel
import ru.gozerov.presentation.shared.screens.trainer_card.TrainerCardScreen
import ru.gozerov.presentation.shared.screens.trainer_card.TrainerCardViewModel

@Composable
fun NavHostContainer(
    startDestination: String,
    navController: NavHostController,
    padding: PaddingValues,
) {

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
                route = Screen.TraineeTabs.route
            ) {
                TraineeTabsScreen(rootNavController = navController, padding)
            }

            composable(
                route = Screen.TrainerTabs.route
            ) {
                TrainerTabsScreen(rootNavController = navController, padding)
            }

            composable(
                route = Screen.FindExercise.route
            ) {
                val viewModel = hiltViewModel<FindExerciseViewModel>()
                FindExerciseScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }

            composable(
                route = Screen.ClientChat.route,
            ) {
                val viewModel = hiltViewModel<ChatViewModel>()
                val trainer =
                    navController.previousBackStackEntry?.savedStateHandle?.get<ChatCard>("trainer")
                trainer?.let {
                    ChatScreen(
                        navController = navController,
                        viewModel = viewModel,
                        trainer = trainer
                    )
                }
            }

            composable(
                route = Screen.TrainerChat.route,
            ) {
                val viewModel = hiltViewModel<TrainerChatViewModel>()
                val client =
                    navController.previousBackStackEntry?.savedStateHandle?.get<ChatCard>("client")
                client?.let {
                    TrainerChatScreen(
                        navController = navController,
                        viewModel = viewModel,
                        user = client
                    )
                }
            }

            composable(
                route = Screen.ClientCard.route,
            ) {
                val clientId =
                    navController.previousBackStackEntry?.savedStateHandle?.get<Int>(
                        "clientId"
                    )
                val viewModel = hiltViewModel<ClientCardViewModel>()
                clientId?.let {
                    ClientCardScreen(
                        clientId = clientId,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }

            composable(
                route = Screen.TrainerCard.route,
            ) {
                val trainerId =
                    navController.previousBackStackEntry?.savedStateHandle?.get<Int>(
                        "trainerId"
                    )

                val viewModel = hiltViewModel<TrainerCardViewModel>()
                trainerId?.let {
                    TrainerCardScreen(
                        trainerId = trainerId,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }

        }
    )

}