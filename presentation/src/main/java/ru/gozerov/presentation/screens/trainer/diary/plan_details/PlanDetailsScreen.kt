package ru.gozerov.presentation.screens.trainer.diary.plan_details

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import ru.gozerov.domain.models.TrainingPlan
import ru.gozerov.domain.models.TrainingPlanCard
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.NavUpToolbar
import ru.gozerov.presentation.shared.views.SimpleTrainingCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlanDetailsScreen(
    contentPaddingValues: PaddingValues,
    planCard: TrainingPlanCard,
    navController: NavController,
    viewModel: PlanDetailsViewModel
) {
    val planState = remember { mutableStateOf<TrainingPlan?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(null) {
        try {
            planState.value = viewModel.getPlan(planCard.id)
        } catch (e: Exception) {
            snackbarHostState.showError(this, "Error")
        }
    }

    Scaffold(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FitLadyaTheme.colors.primaryBackground)
        ) {
            NavUpToolbar(navController = navController)
            planState.value?.let { plan ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = plan.name,
                    color = FitLadyaTheme.colors.text,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = plan.description,
                    color = FitLadyaTheme.colors.text,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(plan.trainings.size) { index ->
                        SimpleTrainingCard(trainingCard = plan.trainings[index]) { }
                    }
                }
            }
        }
    }

}