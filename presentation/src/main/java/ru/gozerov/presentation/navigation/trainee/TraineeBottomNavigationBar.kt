package ru.gozerov.presentation.navigation.trainee

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun TraineeBottomNavigationBar(navController: NavController, contentPaddingValues: PaddingValues) {
    BottomNavigation(
        modifier = Modifier.padding(bottom = contentPaddingValues.calculateBottomPadding()),
        backgroundColor = FitLadyaTheme.colors.secondary,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        traineeBottomNavBarItems.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(screen.iconId),
                        contentDescription = null,
                        tint = if (isSelected) FitLadyaTheme.colors.primary else FitLadyaTheme.colors.primaryBorder
                    )
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }

}