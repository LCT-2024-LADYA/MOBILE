package ru.gozerov.presentation.screens.trainee.diary.find_training

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
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
import ru.gozerov.presentation.screens.trainee.diary.find_training.models.FindTrainingEffect
import ru.gozerov.presentation.screens.trainee.diary.find_training.models.FindTrainingIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.NavUpToolbar
import ru.gozerov.presentation.shared.views.SearchTextField
import ru.gozerov.presentation.shared.views.SimpleTrainingCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FindTrainingScreen(
    navController: NavController,
    contentPaddingValues: PaddingValues,
    viewModel: FindTrainingViewModel
) {
    val effect = viewModel.effect.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val tabs = listOf(stringResource(R.string.all_trainings), stringResource(R.string.favorites))
    val selectedTab = remember { mutableIntStateOf(0) }

    val searchState = remember { mutableStateOf("") }

    val allTrainings = remember { mutableStateOf<LazyPagingItems<TrainingCard>?>(null) }
    val userTrainings = remember { mutableStateOf<LazyPagingItems<TrainingCard>?>(null) }

    LaunchedEffect(null) {
        viewModel.handleIntent(FindTrainingIntent.GetTrainings)
    }

    when (effect) {
        is FindTrainingEffect.None -> {}
        is FindTrainingEffect.AllFoundTrainings -> {
            allTrainings.value = effect.allFlow.collectAsLazyPagingItems()
            userTrainings.value = effect.userFlow.collectAsLazyPagingItems()
        }

        is FindTrainingEffect.LoadedAllTrainings -> {
            allTrainings.value = effect.flow.collectAsLazyPagingItems()
        }

        is FindTrainingEffect.LoadedUserTrainings -> {
            userTrainings.value = effect.flow.collectAsLazyPagingItems()
        }

        is FindTrainingEffect.Error -> {
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
                                viewModel.handleIntent(FindTrainingIntent.FindInAllTrainings(value))
                            else
                                viewModel.handleIntent(FindTrainingIntent.FindInUserTrainings(value))
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
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "id",
                                            training.id
                                        )
                                        navController.navigate(Screen.CreateTraining.route)
                                    }
                                }
                            }
                        }
                    } else {
                        userTrainings.value?.let { pagingData ->
                            items(pagingData.itemCount) { index ->
                                val card = userTrainings.value!![index]
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
            IconButton(
                modifier = Modifier
                    .padding(24.dp)
                    .size(60.dp)
                    .align(Alignment.BottomEnd)
                    .background(FitLadyaTheme.colors.primary, CircleShape),
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.remove<Int>("id")
                    navController.navigate(Screen.CreateTraining.route)
                }
            ) {
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = FitLadyaTheme.colors.secondaryText
                )
            }
        }
    }
}