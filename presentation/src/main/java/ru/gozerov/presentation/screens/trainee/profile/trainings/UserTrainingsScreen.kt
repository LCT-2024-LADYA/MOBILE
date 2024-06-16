package ru.gozerov.presentation.screens.trainee.profile.trainings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.screens.trainee.profile.trainings.models.UserTrainingEffect
import ru.gozerov.presentation.screens.trainee.profile.trainings.models.UserTrainingsIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.shared.views.SearchTextField
import ru.gozerov.presentation.shared.views.SimpleTrainingCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserTrainingScreen(
    navController: NavController,
    contentPaddingValues: PaddingValues,
    viewModel: UserTrainingsViewModel
) {
    val effect = viewModel.effect.collectAsState().value

    val searchState = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val userTrainings = remember { mutableStateOf<LazyPagingItems<TrainingCard>?>(null) }

    LaunchedEffect(null) {
        viewModel.handleIntent(UserTrainingsIntent.LoadTrainings(""))
    }

    when (effect) {
        is UserTrainingEffect.None -> {}
        is UserTrainingEffect.LoadedTrainings -> {
            val data = effect.trainings.collectAsLazyPagingItems()
            userTrainings.value = data
        }

        is UserTrainingEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(UserTrainingsIntent.Reset)
        }
    }

    Scaffold(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxSize(),
        containerColor = FitLadyaTheme.colors.primaryBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Column(modifier = Modifier.background(FitLadyaTheme.colors.secondary)) {
                    NavUpWithTitleToolbar(
                        navController = navController,
                        stringResource(id = R.string.trainings)
                    )

                    SearchTextField(
                        textState = searchState,
                        placeholderText = stringResource(R.string.fing_training),
                        onValueChange = { value ->
                            searchState.value = value
                            viewModel.handleIntent(UserTrainingsIntent.LoadTrainings(value))
                        },
                        containerColor = FitLadyaTheme.colors.primaryBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = FitLadyaTheme.colors.primaryBackground)
                            .padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        userTrainings.value?.let { pagingData ->
                            items(pagingData.itemCount) { index ->
                                val card = userTrainings.value!![index]
                                card?.let { training ->
                                    SimpleTrainingCard(trainingCard = training) {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}