package ru.gozerov.presentation.screens.trainer.tabs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.gozerov.presentation.navigation.trainer.TrainerBottomNavHostContainer
import ru.gozerov.presentation.navigation.trainer.TrainerBottomNavigationBar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun TrainerTabsScreen(rootNavController: NavHostController, contentPaddingValues: PaddingValues) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            TrainerBottomNavigationBar(navController = navController, contentPaddingValues)
        },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) { contentPadding ->
        TrainerBottomNavHostContainer(
            navController = navController,
            rootNavController = rootNavController,
            padding = contentPadding
        )
    }
}