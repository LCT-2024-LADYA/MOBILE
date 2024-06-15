package ru.gozerov.presentation.screens.trainer.diary.training_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.gozerov.domain.models.CustomTrainerTraining
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.CustomExerciseCard
import ru.gozerov.presentation.shared.views.NavUpToolbar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainerTrainingDetailsScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    trainingId: Int,
    viewModel: TrainerTrainingDetailsViewModel
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val trainingState = remember { mutableStateOf<CustomTrainerTraining?>(null) }

    LaunchedEffect(null) {
        try {
            trainingState.value = viewModel.getTraining(id = trainingId)
        } catch (e: Exception) {
            snackbarHostState.showError(coroutineScope = this, e.message.toString())
        }
    }

    Scaffold(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        trainingState.value?.let { training ->
            Column(modifier = Modifier.fillMaxSize()) {
                NavUpToolbar(navController = navController)
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            modifier = Modifier.padding(horizontal = 32.dp),
                            text = training.name,
                            fontSize = 22.sp,
                            color = FitLadyaTheme.colors.text
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            modifier = Modifier.padding(horizontal = 32.dp),
                            text = training.description,
                            color = FitLadyaTheme.colors.text
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 32.dp)
                                .fillMaxWidth(),
                            color = FitLadyaTheme.colors.secondaryBorder
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                    }
                    items(training.exercises.size) { index ->
                        CustomExerciseCard(exercise = training.exercises[index], position = index)
                    }
                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }
    }
}