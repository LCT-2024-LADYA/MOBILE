package ru.gozerov.presentation.screens.trainee.diary.select_training

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.diary.select_training.models.SelectTrainingEffect
import ru.gozerov.presentation.screens.trainee.diary.select_training.models.SelectTrainingIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.NavUpToolbar
import ru.gozerov.presentation.shared.views.SearchTextField
import ru.gozerov.presentation.shared.views.SimpleTrainingCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SelectTrainingScreen(
    navController: NavController,
    contentPaddingValues: PaddingValues,
    viewModel: SelectTrainingViewModel
) {
    val effect = viewModel.effect.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val tabs = listOf(stringResource(R.string.all_trainings), stringResource(R.string.my_trainings))
    val selectedTab = rememberSaveable { mutableIntStateOf(0) }

    val searchState = remember { mutableStateOf("") }

    val allTrainings = remember { mutableStateOf<LazyPagingItems<TrainingCard>?>(null) }
    val trainerTrainings = remember { mutableStateOf<LazyPagingItems<TrainingCard>?>(null) }

    LaunchedEffect(null) {
        viewModel.handleIntent(SelectTrainingIntent.GetTrainings)
    }

    when (effect) {
        is SelectTrainingEffect.None -> {}
        is SelectTrainingEffect.AllFoundTrainings -> {
            allTrainings.value = effect.allFlow.collectAsLazyPagingItems()
            trainerTrainings.value = effect.trainerFlow.collectAsLazyPagingItems()
        }

        is SelectTrainingEffect.LoadedAllTrainings -> {
            allTrainings.value = effect.flow.collectAsLazyPagingItems()
        }

        is SelectTrainingEffect.LoadedTrainerTrainings -> {
            trainerTrainings.value = effect.flow.collectAsLazyPagingItems()
        }

        is SelectTrainingEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
        }
    }

    Scaffold(
        modifier = Modifier.padding(contentPaddingValues),
        containerColor = FitLadyaTheme.colors.primaryBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                Column(modifier = Modifier.background(FitLadyaTheme.colors.secondary)) {
                    NavUpToolbar(navController = navController)

                    SearchTextField(
                        textState = searchState,
                        placeholderText = stringResource(R.string.fing_training),
                        onValueChange = { value ->
                            searchState.value = value
                            if (selectedTab.intValue == 0)
                                viewModel.handleIntent(SelectTrainingIntent.FindInAllTrainings(value))
                            else
                                viewModel.handleIntent(
                                    SelectTrainingIntent.FindInTrainerTrainings(
                                        value
                                    )
                                )
                        },
                        containerColor = FitLadyaTheme.colors.primaryBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    TabRow(selectedTabIndex = selectedTab.intValue, indicator = { tabPositions ->
                        val currentTabPosition = tabPositions[selectedTab.intValue]
                        Box(
                            Modifier
                                .tabIndicatorOffset(currentTabPosition)
                                .height(3.dp)
                                .padding(horizontal = 44.dp)
                                .background(
                                    color = FitLadyaTheme.colors.primary,
                                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                                )
                        )
                    }, divider = {}) {
                        tabs.forEach { tab ->
                            Tab(
                                modifier = Modifier.background(FitLadyaTheme.colors.secondary),
                                selected = tabs.indexOf(tab) == selectedTab.intValue,
                                onClick = {
                                    selectedTab.intValue = tabs.indexOf(tab)
                                }
                            ) {
                                Text(
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    text = tab,
                                    fontWeight = FontWeight.Medium,
                                    color = if (tabs[selectedTab.intValue] == tab) FitLadyaTheme.colors.fieldPrimaryText else FitLadyaTheme.colors.text.copy(
                                        alpha = 0.48f
                                    )
                                )
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = FitLadyaTheme.colors.primaryBackground)
                        .padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (selectedTab.intValue == 0) {
                        allTrainings.value?.let { pagingData ->
                            items(pagingData.itemCount) { index ->
                                val card = allTrainings.value!![index]
                                card?.let { training ->
                                    SimpleTrainingCard(trainingCard = training) {
                                        viewModel.handleIntent(
                                            SelectTrainingIntent.AddTraining(
                                                training
                                            )
                                        )
                                        navController.popBackStack()
                                    }
                                }
                            }
                        }
                    } else {
                        trainerTrainings.value?.let { pagingData ->
                            items(pagingData.itemCount) { index ->
                                val card = trainerTrainings.value!![index]
                                card?.let { training ->
                                    SimpleTrainingCard(trainingCard = training) {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "id",
                                            training.id
                                        )
                                        navController.navigate(Screen.CreateTraining.route)
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}