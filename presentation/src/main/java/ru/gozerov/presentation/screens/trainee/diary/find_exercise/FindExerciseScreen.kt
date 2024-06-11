package ru.gozerov.presentation.screens.trainee.diary.find_exercise

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ru.gozerov.domain.models.Exercise
import ru.gozerov.presentation.R
import ru.gozerov.presentation.screens.trainee.diary.find_exercise.models.FindExerciseEffect
import ru.gozerov.presentation.screens.trainee.diary.find_exercise.models.FindExerciseIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.ExerciseCard
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.shared.views.SearchTextField
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FindExerciseScreen(
    navController: NavController,
    viewModel: FindExerciseViewModel
) {
    val effect = viewModel.effect.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val searchText = remember { mutableStateOf("") }
    val exercises = remember { mutableStateOf<LazyPagingItems<Exercise>?>(null) }

    when (effect) {
        is FindExerciseEffect.None -> {}
        is FindExerciseEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(FindExerciseIntent.Reset)
        }

        is FindExerciseEffect.LoadedExercises -> {
            exercises.value = effect.exercises.collectAsLazyPagingItems()
        }

        is FindExerciseEffect.Exit -> {
            navController.popBackStack()
            viewModel.handleIntent(FindExerciseIntent.Reset)
        }
    }

    LaunchedEffect(null) {
        viewModel.handleIntent(FindExerciseIntent.SearchExercise(""))
    }

    Scaffold(
        containerColor = FitLadyaTheme.colors.primaryBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { _ ->
        Column {
            NavUpWithTitleToolbar(
                navController = navController,
                title = stringResource(id = R.string.choose_exercise)
            )
            Spacer(modifier = Modifier.height(8.dp))
            SearchTextField(
                textState = searchText,
                onValueChange = { value ->
                    searchText.value = value
                    viewModel.handleIntent(FindExerciseIntent.SearchExercise(value))
                },
                placeholderText = stringResource(id = R.string.find_exercise),
                containerColor = FitLadyaTheme.colors.secondary
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                exercises.value?.let { pagingItems ->
                    items(pagingItems.itemCount) { index ->
                        val item = pagingItems[index]
                        item?.let { exercise ->
                            ExerciseCard(exercise = exercise) {
                                viewModel.handleIntent(FindExerciseIntent.AddExercise(exercise))
                            }
                        }
                    }
                }
            }
        }
    }
}