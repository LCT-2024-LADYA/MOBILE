package ru.gozerov.presentation.screens.trainee.diary.create_training

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    isTrainer: Boolean,
    trainingId: Int?,
    trainingDate: String?,
    parentNavController: NavController,
    navController: NavController,
    contentPaddingValues: PaddingValues,
    viewModel: CreateTrainingViewModel,
    backRoute: String
) {
    val effect = viewModel.effect.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val trainingName = rememberSaveable { mutableStateOf("") }

    val sureTrainingName = rememberSaveable { mutableStateOf(trainingName.value) }
    val date = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(trainingDate ?: ""))
    }
    val timeStart =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val timeEnd =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val description = rememberSaveable { mutableStateOf("") }

    val exercises = remember { mutableStateOf(listOf<Exercise>()) }

    val errorMessage = stringResource(id = R.string.incorrect_data)

    LaunchedEffect(null) {
        if (exercises.value.isEmpty()) {
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
        is CreateTrainingEffect.RemovedExercise -> {
            val newExercises = exercises.value.toMutableList()
            val exerciseToRemove = newExercises.first { exercise -> exercise.id == effect.id }
            newExercises.remove(exerciseToRemove)
            val pos = exercises.value.indexOf(exerciseToRemove)
            viewModel.weights.removeAt(pos)
            viewModel.sets.removeAt(pos)
            viewModel.reps.removeAt(pos)
            exercises.value = newExercises
            newExercises.removeIf { exercise -> exercise.id == effect.id }
            viewModel.handleIntent(CreateTrainingIntent.Reset)
        }

        is CreateTrainingEffect.AddedExercises -> {
            val diff = effect.exercises.size - exercises.value.size
            repeat(diff) {
                if (viewModel.weights.size < effect.exercises.size) {
                    viewModel.weights.add("0")
                    viewModel.sets.add("")
                    viewModel.reps.add("")
                }
            }
            exercises.value = effect.exercises
            viewModel.handleIntent(CreateTrainingIntent.Reset)
        }

        is CreateTrainingEffect.LoadedTraining -> {
            trainingName.value = effect.training.name
            description.value = effect.training.description
            val diff = effect.training.exercises.size - exercises.value.size
            repeat(diff) {

                viewModel.weights.add("0")
                viewModel.sets.add("")
                viewModel.reps.add("")
            }
            exercises.value = effect.training.exercises.map { exercise -> exercise.toExercise() }
            viewModel.handleIntent(CreateTrainingIntent.Reset)
        }

        is CreateTrainingEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(CreateTrainingIntent.Reset)
        }

        is CreateTrainingEffect.CreatedTraining -> {
            navController.popBackStack(backRoute, false)
            viewModel.handleIntent(CreateTrainingIntent.NextTraining(trainingId))
            viewModel.handleIntent(CreateTrainingIntent.Reset)
        }
    }

    var showPopup: Boolean by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.padding(contentPaddingValues),
        containerColor = FitLadyaTheme.colors.primaryBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {

            if (showPopup) {
                Dialog(onDismissRequest = { showPopup = false }) {
                    Card(
                        modifier = Modifier
                            .background(
                                color = FitLadyaTheme.colors.secondary,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .fillMaxWidth()
                            .height(420.dp),
                        colors = CardDefaults.cardColors(containerColor = FitLadyaTheme.colors.secondary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(64.dp))
                            Text(
                                text = stringResource(id = R.string.make_public),
                                fontWeight = FontWeight.Medium,
                                color = FitLadyaTheme.colors.text,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            CustomTextField(
                                textState = sureTrainingName,
                                labelText = stringResource(id = R.string.training_name),
                                modifier = Modifier
                                    .padding(horizontal = 32.dp)
                                    .fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(32.dp))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp)
                                    .height(40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                                onClick = {
                                    if (sureTrainingName.value.isNotBlank()) {
                                        val createExercisesModels =
                                            viewModel.weights.mapIndexed { ind, state ->
                                                CreateExerciseModel(
                                                    exercises.value[ind].id,
                                                    step = ind,
                                                    reps = viewModel.reps[ind].toInt(),
                                                    sets = viewModel.sets[ind].toInt(),
                                                    weight = viewModel.weights[ind].toInt()
                                                )
                                            }


                                        viewModel.handleIntent(
                                            CreateTrainingIntent.CreateTrainerTraining(
                                                CreateTrainingModel(
                                                    description.value,
                                                    createExercisesModels,
                                                    trainingName.value
                                                ),
                                                true,
                                                date.value.text,
                                                timeStart.value.text,
                                                timeEnd.value.text
                                            )
                                        )
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(id = R.string.to_moderate),
                                    color = FitLadyaTheme.colors.secondaryText
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                modifier = Modifier
                                    .padding(horizontal = 32.dp, vertical = 16.dp)
                                    .fillMaxWidth()
                                    .height(40.dp),
                                border = BorderStroke(2.dp, FitLadyaTheme.colors.primary),
                                colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.secondary),
                                onClick = {
                                    if (sureTrainingName.value.isNotBlank()) {
                                        val createExercisesModels =
                                            viewModel.weights.mapIndexed { ind, state ->
                                                CreateExerciseModel(
                                                    exercises.value[ind].id,
                                                    step = ind,
                                                    reps = viewModel.reps[ind].toInt(),
                                                    sets = viewModel.sets[ind].toInt(),
                                                    weight = viewModel.weights[ind].toInt()
                                                )
                                            }


                                        viewModel.handleIntent(
                                            CreateTrainingIntent.CreateTrainerTraining(
                                                CreateTrainingModel(
                                                    description.value,
                                                    createExercisesModels,
                                                    trainingName.value
                                                ),
                                                false,
                                                date.value.text,
                                                timeStart.value.text,
                                                timeEnd.value.text
                                            )
                                        )
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.skip),
                                    fontWeight = FontWeight.Medium,
                                    color = FitLadyaTheme.colors.buttonText,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = stringResource(R.string.cancel),
                                fontWeight = FontWeight.Medium,
                                color = FitLadyaTheme.colors.accent,
                                fontSize = 14.sp,
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    showPopup = false
                                    sureTrainingName.value = ""
                                }
                            )
                        }
                    }
                }
            }

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
                        if (!isTrainer) {
                            Spacer(modifier = Modifier.height(12.dp))
                            DateDDMMYYYYTextField(
                                textState = date,
                                labelText = stringResource(id = R.string.date),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        }
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
                        EditableCustomExerciseCard(
                            exercise = exercises.value[index],
                            position = index,
                            onWeightChange = { value ->
                                viewModel.weights[index] = value
                            },
                            onSetsChange = { value ->
                                viewModel.sets[index] = value
                            },
                            onRepsChange = { value ->
                                viewModel.reps[index] = value
                            },
                            weight = viewModel.weights[index],
                            sets = viewModel.sets[index],
                            reps = viewModel.reps[index],
                        ) {
                            viewModel.handleIntent(CreateTrainingIntent.RemoveExercise(exercises.value[index].id))
                        }
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
                    viewModel.weights.forEach { state ->
                        if (!isValidInt(state))
                            isEmpty = true
                    }
                    viewModel.sets.forEach { state ->
                        if (!isValidInt(state))
                            isEmpty = true
                    }
                    viewModel.reps.forEach { state ->
                        if (!isValidInt(state))
                            isEmpty = true
                    }
                    if (isTrainer && !isEmpty) {
                        showPopup = true
                    } else
                        if (isEmpty) {
                            snackbarHostState.showError(coroutineScope, errorMessage)
                        } else if (description.value.isNotBlank() && trainingName.value.isNotBlank() &&
                            timeStart.value.text.length == 5 && timeEnd.value.text.length == 5
                            && date.value.text.length == 10 && exercises.value.isNotEmpty()
                        ) {
                            val createExercisesModels = viewModel.weights.mapIndexed { ind, state ->
                                CreateExerciseModel(
                                    exercises.value[ind].id,
                                    step = ind,
                                    reps = viewModel.reps[ind].toInt(),
                                    sets = viewModel.sets[ind].toInt(),
                                    weight = viewModel.weights[ind].toInt()
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