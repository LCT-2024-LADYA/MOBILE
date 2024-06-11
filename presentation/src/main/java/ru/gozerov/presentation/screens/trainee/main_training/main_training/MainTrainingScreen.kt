package ru.gozerov.presentation.screens.trainee.main_training.main_training

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.main_training.main_training.models.MainTrainingEffect
import ru.gozerov.presentation.screens.trainee.main_training.main_training.models.MainTrainingIntent
import ru.gozerov.presentation.screens.trainee.main_training.main_training.models.MainTrainingViewState
import ru.gozerov.presentation.screens.trainee.main_training.main_training.views.MainTraining
import ru.gozerov.presentation.screens.trainee.main_training.main_training.views.NoTraining
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainTrainingScreen(
    navController: NavController,
    viewModel: MainTrainingViewModel,
    contentPaddingValues: PaddingValues
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val currentTraining = remember { mutableStateOf<CustomTraining?>(null) }
    val showEmpty = remember { mutableStateOf(false) }

    val viewState = viewModel.viewState.collectAsState().value
    val effect = viewModel.effect.collectAsState().value

    LaunchedEffect(null) {
        viewModel.handleIntent(MainTrainingIntent.LoadNextTraining)
    }

    when (viewState) {
        is MainTrainingViewState.None -> {}
        is MainTrainingViewState.LoadedTraining -> {
            showEmpty.value = false
            currentTraining.value = viewState.training
            viewModel.handleIntent(MainTrainingIntent.Reset)
        }

        is MainTrainingViewState.Empty -> {
            currentTraining.value = null
            showEmpty.value = true
            viewModel.handleIntent(MainTrainingIntent.Reset)
        }
    }
    when (effect) {
        is MainTrainingEffect.None -> {}
        is MainTrainingEffect.Error -> {
            snackbarHostState.showError(
                coroutineScope = coroutineScope,
                message = effect.message
            )
            viewModel.handleIntent(MainTrainingIntent.Reset)
        }
    }

    Scaffold(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        if (showEmpty.value) {
            NoTraining {
                navController.navigate(Screen.CreateTraining.route)
            }
        }
        currentTraining.value?.let { training ->
            MainTraining(
                training = training,
                onStartClicked = {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "training",
                        currentTraining.value
                    )
                    navController.navigate(Screen.TrainingProcess.route)
                }
            )
        }
    }
}