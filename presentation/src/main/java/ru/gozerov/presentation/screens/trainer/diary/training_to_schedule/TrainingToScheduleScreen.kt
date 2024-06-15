package ru.gozerov.presentation.screens.trainer.diary.training_to_schedule

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainingPlan
import ru.gozerov.domain.utils.compareDates
import ru.gozerov.domain.utils.convertLocalDateDateToUTC
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.models.TrainingToScheduleEffect
import ru.gozerov.presentation.screens.trainer.diary.training_to_schedule.models.TrainingToScheduleIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.shared.views.SimpleTrainingCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainingToScheduleScreen(
    startDate: Int,
    endDate: Int,
    month: Int,
    trainingPlan: TrainingPlan,
    scheduledTrainings: List<ScheduledTraining>,
    navController: NavController,
    paddingValues: PaddingValues,
    backRoute: String,
    viewModel: TrainingToScheduleViewModel
) {
    val effect = viewModel.effect.collectAsState().value

    val currentDate = LocalDate.now()
    val monthState = remember {
        mutableStateOf(
            MonthState(
                YearMonth.of(currentDate.year, month),
                YearMonth.of(currentDate.year, 1),
                YearMonth.of(currentDate.year, 12)
            )
        )
    }

    val currentTrainingPosition = remember { mutableIntStateOf(0) }

    val primaryColor = FitLadyaTheme.colors.primary
    val todayDateBackground = FitLadyaTheme.colors.secondary
    val todayDateBackgroundState = remember { mutableStateOf(todayDateBackground) }

    val selectedDay = rememberSaveable { mutableIntStateOf(-1) }
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    val calendarState = rememberSelectableCalendarState(
        monthState = monthState.value,
        initialSelectionMode = SelectionMode.Period
    )

    calendarState.selectionState.selectionMode = SelectionMode.Single

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var isAttachAvailable: Boolean by remember { mutableStateOf(false) }

    SideEffect {
        viewModel.handleIntent(TrainingToScheduleIntent.GetLastAddedTraining)
    }

    when (effect) {
        is TrainingToScheduleEffect.None -> {}
        is TrainingToScheduleEffect.LastAddedTraining -> {
            if (effect.id == trainingPlan.trainings[currentTrainingPosition.intValue].id) {
                if (currentTrainingPosition.intValue < trainingPlan.trainings.size - 1) {
                    currentTrainingPosition.intValue += 1
                } else {
                    navController.popBackStack(backRoute, false)
                }
            }
        }

        is TrainingToScheduleEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
        }
    }
    Scaffold(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        containerColor = FitLadyaTheme.colors.primaryBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NavUpWithTitleToolbar(
                navController = navController,
                title = stringResource(id = R.string.training_plan_attaching)
            )
            Text(
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 12.dp
                ),
                maxLines = 2,
                text = trainingPlan.name,
                fontSize = 22.sp,
                color = FitLadyaTheme.colors.text
            )
            Text(
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 12.dp
                ),
                maxLines = 2,
                text = trainingPlan.description,
                fontSize = 14.sp,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 12.dp
                ),
                text = stringResource(
                    id = R.string.training_is,
                    currentTrainingPosition.intValue + 1,
                    trainingPlan.trainings.size
                ),
                fontSize = 16.sp,
                color = FitLadyaTheme.colors.fieldPrimaryText,
                fontWeight = FontWeight.Medium
            )
            SimpleTrainingCard(trainingCard = trainingPlan.trainings[currentTrainingPosition.intValue]) { /*Not clickable in that context*/ }

            SelectableCalendar(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                firstDayOfWeek = DayOfWeek.MONDAY,
                monthHeader = { },
                daysOfWeekHeader = {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "ПН",
                            color = FitLadyaTheme.colors.text,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "ВТ",
                            color = FitLadyaTheme.colors.text,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "СР",
                            color = FitLadyaTheme.colors.text,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "ЧТ",
                            color = FitLadyaTheme.colors.text,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "ПТ",
                            color = FitLadyaTheme.colors.text,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "СБ",
                            color = FitLadyaTheme.colors.text,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "ВС",
                            color = FitLadyaTheme.colors.text,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                calendarState = calendarState,
                dayContent = { dayState ->
                    Column(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(4.dp)
                            .background(
                                color = if (dayState.date.dayOfMonth == selectedDay.intValue && dayState.date.month.value == month)
                                    FitLadyaTheme.colors.primary else FitLadyaTheme.colors.primaryBackground,
                                shape = CircleShape
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                if (dayState.date.month.value == month && dayState.date.dayOfMonth > startDate && dayState.date.dayOfMonth < endDate) {
                                    selectedDay.intValue = dayState.date.dayOfMonth
                                    selectedDate.value = dayState.date
                                    todayDateBackgroundState.value = primaryColor
                                    isAttachAvailable = true
                                }
                            },
                            fontSize = 16.sp,
                            text = dayState.date.dayOfMonth.toString(),
                            color = FitLadyaTheme.colors.text.copy(alpha = if (dayState.date.month == monthState.value.currentMonth.month && dayState.date.dayOfMonth > startDate && dayState.date.dayOfMonth < endDate) 1f else 0.32f),
                            textAlign = TextAlign.Center
                        )
                        if (scheduledTrainings.filter { training ->
                                compareDates(
                                    training.date,
                                    convertLocalDateDateToUTC(dayState.date)
                                ) == 0
                            }.size == 1) {
                            val circleCount = scheduledTrainings.first { training ->
                                training.date == convertLocalDateDateToUTC(dayState.date)
                            }.ids.size
                            Row(modifier = Modifier.padding(top = 4.dp)) {
                                (0 until circleCount).forEach { _ ->
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 1.dp)
                                            .size(4.dp)
                                            .background(
                                                color = FitLadyaTheme.colors.accent,
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                        } else {
                            Box(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                if (isAttachAvailable) {
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 24.dp)
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                        onClick = {
                            selectedDate.value?.let {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "id",
                                    trainingPlan.trainings[currentTrainingPosition.intValue].id
                                )
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "date", selectedDate.value
                                )
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "backRoute", Screen.TrainingToSchedule.route
                                )
                                navController.navigate(Screen.CreateTraining.route)
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.attach),
                            color = FitLadyaTheme.colors.secondaryText
                        )
                    }
                }
            }

        }
    }
}