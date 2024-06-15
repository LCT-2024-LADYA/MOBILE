package ru.gozerov.presentation.screens.trainer.diary.create_plan

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainer.diary.create_plan.models.CreatePlanEffect
import ru.gozerov.presentation.screens.trainer.diary.create_plan.models.CreatePlanIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.shared.views.SimpleTrainingCardEditable
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreatePlanScreen(
    navController: NavController,
    viewModel: CreatePlanViewModel,
    contentPaddingValues: PaddingValues,
    userId: Int
) {
    val effect = viewModel.effect.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val trainingName = rememberSaveable { mutableStateOf(viewModel.name) }
    val description = rememberSaveable { mutableStateOf(viewModel.description) }

    val trainings = remember { mutableStateOf(listOf<TrainingCard>()) }

    val errorMessage = stringResource(id = R.string.incorrect_data)

    SideEffect {
        viewModel.handleIntent(CreatePlanIntent.GetAddedTrainings)
    }

    when (effect) {
        is CreatePlanEffect.None -> {}
        is CreatePlanEffect.RemovedTraining -> {
            val newExercises = trainings.value.toMutableList()
            val exerciseToRemove = newExercises.firstOrNull { exercise -> exercise.id == effect.id }
            newExercises.remove(exerciseToRemove)
            trainings.value = newExercises
            viewModel.handleIntent(CreatePlanIntent.Reset)
        }

        is CreatePlanEffect.AddedTrainings -> {
            trainings.value = effect.trainings
            viewModel.handleIntent(CreatePlanIntent.Reset)
        }

        is CreatePlanEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(CreatePlanIntent.Reset)
        }

        is CreatePlanEffect.CreatedPlan -> {
            navController.popBackStack()
            viewModel.handleIntent(CreatePlanIntent.Reset)
        }
    }

    Scaffold(
        modifier = Modifier.padding(contentPaddingValues),
        containerColor = FitLadyaTheme.colors.primaryBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                NavUpWithTitleToolbar(
                    navController = navController,
                    title = stringResource(id = R.string.create_plan)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomTextField(
                            textState = trainingName,
                            onValueChange = {
                                viewModel.name = it
                                trainingName.value = it
                            },
                            labelText = stringResource(id = R.string.plan_name),
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        CustomTextField(
                            textState = description,
                            onValueChange = {
                                viewModel.description = it
                                description.value = it
                            },
                            labelText = stringResource(id = R.string.plan_description),
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                            onClick = {
                                navController.navigate(Screen.SelectTraining.route)
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.add_training),
                                color = FitLadyaTheme.colors.secondaryText
                            )
                        }
                    }
                    items(trainings.value.size) { index ->
                        SimpleTrainingCardEditable(
                            trainingCard = trainings.value[index],
                            onDelete = {
                                viewModel.handleIntent(CreatePlanIntent.RemoveTraining(trainings.value[index].id))
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(56.dp))
                    }
                }
            }
            Button(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                onClick = {
                    if (description.value.isNotBlank() && trainingName.value.isNotBlank() &&
                        trainings.value.isNotEmpty()
                    ) {
                        viewModel.handleIntent(
                            CreatePlanIntent.CreatePlan(
                                trainingName.value,
                                description.value,
                                trainings.value.map { t -> t.id },
                                userId
                            )
                        )
                    } else {
                        snackbarHostState.showError(coroutineScope, errorMessage)
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.attach_to_user),
                    color = FitLadyaTheme.colors.secondaryText
                )
            }
        }

    }
}