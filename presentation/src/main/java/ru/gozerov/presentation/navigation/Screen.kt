package ru.gozerov.presentation.navigation

import androidx.annotation.DrawableRes

sealed class Screen(
    val route: String,
    @DrawableRes val icon: Int? = null
) {

    object ChoiceLogin : Screen("choiceLogin")

    object LoginTrainer : Screen("loginTrainer")

    object Register : Screen("register")

    object RegisterProfile : Screen("registerProfile")

    object LoginTrainee : Screen("loginTrainee")

    object TrainerProfile : Screen("trainerProfile")

    object TraineeTabs : Screen("traineeTabs")

    object MainTraining : Screen("mainTraining")

    object TrainingProcess : Screen("trainingProcess")

    object EndTraining : Screen("endTraining")

    object CreateTraining : Screen("createTraining")

    object FindExercise : Screen("findExercise")

    object FindTraining : Screen("findTraining")

    object TrainingDetails : Screen("trainingDetails")

    object ClientDiary : Screen("clientDiary")

    object ClientChatList : Screen("clientChatList")

    object ClientChat : Screen("clientChat")

    object TrainerDiary : Screen("trainerDiary")

    object TrainerChatList : Screen("trainerChatList")

    object TrainerChat : Screen("trainerChat")

    object TrainerTabs : Screen("trainerTabs")

    object ClientCard : Screen("clientCard")

    object TrainerCard : Screen("trainerCard")

    object ClientStatisticsScreen : Screen("clientStatistics")

    object TraineeProfile : Screen("traineeProfile")

}