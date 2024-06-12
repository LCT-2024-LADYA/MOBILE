package ru.gozerov.presentation.screens.trainee.diary.create_training

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.gozerov.domain.models.CreateExerciseModel
import ru.gozerov.domain.models.CreateTrainingModel
import ru.gozerov.domain.models.Exercise
import ru.gozerov.domain.models.toExercise
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.diary.create_training.models.CreateTrainingEffect
import ru.gozerov.presentation.screens.trainee.diary.create_training.models.CreateTrainingIntent
import ru.gozerov.presentation.shared.utils.isValidInt
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.shared.views.DateDDMMYYYYTextField
import ru.gozerov.presentation.shared.views.DateHHMMTextField
import ru.gozerov.presentation.shared.views.EditableCustomExerciseCard
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateTrainingScreen(
    trainingId: Int?,
    parentNavController: NavController,
    navController: NavController,
    contentPaddingValues: PaddingValues,
    viewModel: CreateTrainingViewModel
) {
    val effect = viewModel.effect.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val trainingName = rememberSaveable { mutableStateOf("") }
    val date = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val timeStart =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val timeEnd =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val description = rememberSaveable { mutableStateOf("") }

    val exercises = remember { mutableStateOf(listOf<Exercise>()) }
    val weightsState = mutableListOf<MutableState<String>>()
    val setsState = mutableListOf<MutableState<String>>()
    val repsState = mutableListOf<MutableState<String>>()

    val errorMessage = stringResource(id = R.string.incorrect_data)

    LaunchedEffect(null) {
        if (exercises.value.isEmpty()) {
            viewModel.handleIntent(CreateTrainingIntent.Clear)
            trainingId?.let {
                viewModel.handleIntent(CreateTrainingIntent.GetTraining(trainingId))
            }
        }
    }

    SideEffect {
        viewModel.handleIntent(CreateTrainingIntent.GetAddedExercises)
    }

    when (effect) {
        is CreateTrainingEffect.None -> {}
        is CreateTrainingEffect.AddedExercises -> {
            Log.e("AAA", "hu")
            exercises.value = effect.exercises
        }

        is CreateTrainingEffect.LoadedTraining -> {
            trainingName.value = effect.training.name
            description.value = effect.training.description
            exercises.value = effect.training.exercises.map { exercise -> exercise.toExercise() }
            viewModel.handleIntent(CreateTrainingIntent.Reset)
        }

        is CreateTrainingEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(CreateTrainingIntent.Reset)
        }

        is CreateTrainingEffect.CreatedTraining -> {
            navController.popBackStack()
            viewModel.handleIntent(CreateTrainingIntent.Reset)
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
                    title = stringResource(id = R.string.create_training)
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
                            labelText = stringResource(id = R.string.training_name),
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        DateDDMMYYYYTextField(
                            textState = date,
                            labelText = stringResource(id = R.string.date),
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        DateHHMMTextField(
                            textState = timeStart,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            labelText = stringResource(id = R.string.time_start),
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        DateHHMMTextField(
                            textState = timeEnd,
                            labelText = stringResource(id = R.string.time_end),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        CustomTextField(
                            textState = description,
                            labelText = stringResource(id = R.string.training_description),
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
                                parentNavController.navigate(Screen.FindExercise.route)
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.add_exercise),
                                color = FitLadyaTheme.colors.secondaryText
                            )
                        }
                    }
                    items(exercises.value.size) { index ->
                        val weight = rememberSaveable { mutableStateOf("") }
                        val sets = rememberSaveable { mutableStateOf("") }
                        val reps = rememberSaveable { mutableStateOf("") }

                        weightsState.add(weight)
                        setsState.add(sets)
                        repsState.add(reps)

                        EditableCustomExerciseCard(
                            exercise = exercises.value[index],
                            position = index,
                            weightState = weight,
                            setsState = sets,
                            repsState = reps
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
                    var isEmpty = false
                    weightsState.forEach { state ->
                        if (!isValidInt(state.value))
                            isEmpty = true
                    }
                    setsState.forEach { state ->
                        if (!isValidInt(state.value))
                            isEmpty = true
                    }
                    repsState.forEach { state ->
                        if (!isValidInt(state.value))
                            isEmpty = true
                    }
                    if (isEmpty) {
                        snackbarHostState.showError(coroutineScope, errorMessage)
                    } else if (description.value.isNotBlank() && trainingName.value.isNotBlank() &&
                        timeStart.value.text.length == 5 && timeEnd.value.text.length == 5
                        && date.value.text.length == 10
                    ) {
                        val createExercisesModels = weightsState.mapIndexed { ind, state ->
                            CreateExerciseModel(
                                exercises.value[ind].id,
                                setsState[ind].value.toInt(),
                                repsState[ind].value.toInt(),
                                false,
                                state.value.toInt()
                            )
                        }

                        viewModel.handleIntent(
                            CreateTrainingIntent.CreateTraining(
                                CreateTrainingModel(
                                    description.value,
                                    createExercisesModels,
                                    trainingName.value
                                ), date.value.text, timeStart.value.text, timeEnd.value.text
                            )
                        )
                    } else {
                        snackbarHostState.showError(coroutineScope, errorMessage)
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    color = FitLadyaTheme.colors.secondaryText
                )
            }
        }

    }
}