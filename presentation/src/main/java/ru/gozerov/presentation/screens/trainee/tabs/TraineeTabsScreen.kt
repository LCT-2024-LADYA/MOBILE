package ru.gozerov.presentation.screens.trainee.tabs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.gozerov.presentation.navigation.trainee.TraineeBottomNavHostContainer
import ru.gozerov.presentation.navigation.trainee.TraineeBottomNavigationBar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun TraineeTabsScreen(rootNavController: NavHostController, contentPaddingValues: PaddingValues) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            TraineeBottomNavigationBar(navController = navController, contentPaddingValues)
        },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) { contentPadding ->
        TraineeBottomNavHostContainer(
            navController = navController,
            rootNavController = rootNavController,
            padding = contentPadding
        )
    }
}