package ru.gozerov.presentation.screens.trainer.diary.diary_plan

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.domain.models.TrainingPlan
import ru.gozerov.domain.utils.compareDates
import ru.gozerov.domain.utils.convertLocalDateDateToUTC
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.diary.diary.mapMonthToRu
import ru.gozerov.presentation.screens.trainer.diary.diary_plan.models.SchedulePlanIntent
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SchedulePlanScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: SchedulePlanViewModel,
    trainingPlan: TrainingPlan
) {
    val currentDate = LocalDate.now()
    val monthState = remember {
        mutableStateOf(
            MonthState(
                YearMonth.now(),
                YearMonth.of(currentDate.year, 1),
                YearMonth.of(currentDate.year, 12)
            )
        )
    }

    LaunchedEffect(null) {
        viewModel.handleIntent(SchedulePlanIntent.ClearLastAddedTraining)
    }

    val firstSelectedDay = rememberSaveable { mutableIntStateOf(-1) }
    val secondSelectedDay = rememberSaveable { mutableIntStateOf(-1) }

    val calendarState = rememberSelectableCalendarState(
        monthState = monthState.value,
        initialSelectionMode = SelectionMode.Period
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var isPeriodSelected: Boolean by rememberSaveable { mutableStateOf(false) }

    val scheduledTrainings = remember {
        mutableStateOf<List<ScheduledTraining>>(
            listOf(
                ScheduledTraining(
                    "2024-06-15T00:00:00Z",
                    listOf(1, 2)
                )
            )
        )
    }

    Scaffold(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        containerColor = FitLadyaTheme.colors.primaryBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            NavUpWithTitleToolbar(
                navController = navController,
                title = stringResource(id = R.string.plan_attaching)
            )
        }
    ) {

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 24.dp,
                    bottom = 12.dp
                ),
                text = trainingPlan.name,
                fontSize = 22.sp,
                color = FitLadyaTheme.colors.text
            )
            Text(
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 24.dp
                ),
                text = trainingPlan.description,
                fontSize = 14.sp,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.8f)
            )

            SelectableCalendar(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),

                firstDayOfWeek = DayOfWeek.MONDAY,
                monthHeader = {
                    Row(
                        modifier = Modifier
                            .padding()
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(modifier = Modifier.size(48.dp), onClick = {
                            if (calendarState.monthState.currentMonth.month.value > 1)
                                calendarState.monthState.currentMonth =
                                    calendarState.monthState.currentMonth.minusMonths(1)

                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.primary
                            )
                        }

                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                text = it.currentMonth.mapMonthToRu(),
                                color = FitLadyaTheme.colors.text
                            )
                        }
                        IconButton(modifier = Modifier.size(48.dp), onClick = {
                            if (calendarState.monthState.currentMonth.month.value < 12)
                                calendarState.monthState.currentMonth =
                                    calendarState.monthState.currentMonth.plusMonths(1)
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.primary
                            )
                        }
                    }

                },
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
                    if (dayState.date == currentDate) {
                        Column(
                            modifier = Modifier
                                .size(56.dp)
                                .padding(
                                    bottom = 8.dp,
                                    start = if (firstSelectedDay.intValue == dayState.date.dayOfMonth && dayState.date.month == monthState.value.currentMonth.month) 4.dp else 0.dp,
                                    end = if (secondSelectedDay.intValue == dayState.date.dayOfMonth && dayState.date.month == monthState.value.currentMonth.month) 4.dp else 0.dp
                                )
                                .background(
                                    color = if ((firstSelectedDay.intValue == dayState.date.dayOfMonth || secondSelectedDay.intValue == dayState.date.dayOfMonth)
                                        && dayState.date.month == monthState.value.currentMonth.month
                                    )
                                        FitLadyaTheme.colors.primary else if
                                                                                  (isPeriodSelected && firstSelectedDay.intValue < dayState.date.dayOfMonth && secondSelectedDay.intValue > dayState.date.dayOfMonth && dayState.date.month == monthState.value.currentMonth.month)
                                        FitLadyaTheme.colors.secondaryBorder else FitLadyaTheme.colors.secondary,
                                    shape = if ((firstSelectedDay.intValue == dayState.date.dayOfMonth || secondSelectedDay.intValue == dayState.date.dayOfMonth)
                                        && dayState.date.month == monthState.value.currentMonth.month
                                    ) if (firstSelectedDay.intValue == dayState.date.dayOfMonth) RoundedCornerShape(
                                        topStart = 64.dp,
                                        bottomStart = 64.dp
                                    ) else RoundedCornerShape(
                                        topEnd = 64.dp,
                                        bottomEnd = 64.dp
                                    ) else if (isPeriodSelected && (dayState.date.dayOfMonth > firstSelectedDay.intValue && dayState.date.dayOfMonth < secondSelectedDay.intValue)) RectangleShape else CircleShape
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    if (dayState.date.month == monthState.value.currentMonth.month) {
                                        if (firstSelectedDay.intValue == -1 || dayState.date.dayOfMonth < firstSelectedDay.intValue) {
                                            firstSelectedDay.intValue = dayState.date.dayOfMonth
                                        } else if (secondSelectedDay.intValue == -1) {
                                            secondSelectedDay.intValue = dayState.date.dayOfMonth
                                            isPeriodSelected = true
                                        } else if (secondSelectedDay.intValue != -1) {
                                            isPeriodSelected = false
                                            firstSelectedDay.intValue = dayState.date.dayOfMonth
                                            secondSelectedDay.intValue = -1
                                        }
                                    }
                                },
                                fontSize = 16.sp,
                                text = dayState.date.dayOfMonth.toString(),
                                color = FitLadyaTheme.colors.text.copy(alpha = if (dayState.date.month == monthState.value.currentMonth.month) 1f else 0.32f),
                                textAlign = TextAlign.Center
                            )
                            if (scheduledTrainings.value.filter { training ->
                                    compareDates(
                                        training.date,
                                        convertLocalDateDateToUTC(dayState.date)
                                    ) == 0
                                }.size == 1) {
                                val circleCount = scheduledTrainings.value.first { training ->
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
                    } else {
                        Column(
                            modifier = Modifier
                                .size(56.dp)
                                .padding(
                                    bottom = 8.dp,
                                    start = if (firstSelectedDay.intValue == dayState.date.dayOfMonth && dayState.date.month == monthState.value.currentMonth.month) 4.dp else 0.dp,
                                    end = if (secondSelectedDay.intValue == dayState.date.dayOfMonth && dayState.date.month == monthState.value.currentMonth.month) 4.dp else 0.dp
                                )
                                .background(
                                    color = if ((firstSelectedDay.intValue == dayState.date.dayOfMonth || secondSelectedDay.intValue == dayState.date.dayOfMonth)
                                        && dayState.date.month == monthState.value.currentMonth.month
                                    )
                                        FitLadyaTheme.colors.primary else if
                                                                                  (isPeriodSelected && firstSelectedDay.intValue < dayState.date.dayOfMonth && secondSelectedDay.intValue > dayState.date.dayOfMonth && dayState.date.month == monthState.value.currentMonth.month)
                                        FitLadyaTheme.colors.secondaryBorder else FitLadyaTheme.colors.primaryBackground,
                                    shape = if ((firstSelectedDay.intValue == dayState.date.dayOfMonth || secondSelectedDay.intValue == dayState.date.dayOfMonth)
                                        && dayState.date.month == monthState.value.currentMonth.month
                                    ) if (firstSelectedDay.intValue == dayState.date.dayOfMonth) RoundedCornerShape(
                                        topStart = 64.dp,
                                        bottomStart = 64.dp
                                    ) else RoundedCornerShape(
                                        topEnd = 64.dp,
                                        bottomEnd = 64.dp
                                    ) else RectangleShape
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    if (dayState.date.month == monthState.value.currentMonth.month) {
                                        if (firstSelectedDay.intValue == -1 || dayState.date.dayOfMonth < firstSelectedDay.intValue) {
                                            firstSelectedDay.intValue = dayState.date.dayOfMonth
                                        } else if (secondSelectedDay.intValue == -1) {
                                            secondSelectedDay.intValue = dayState.date.dayOfMonth
                                            isPeriodSelected = true
                                        } else if (secondSelectedDay.intValue != -1) {
                                            isPeriodSelected = false
                                            firstSelectedDay.intValue = dayState.date.dayOfMonth
                                            secondSelectedDay.intValue = -1
                                        }

                                    }
                                },
                                fontSize = 16.sp,
                                text = dayState.date.dayOfMonth.toString(),
                                color = FitLadyaTheme.colors.text.copy(alpha = if (dayState.date.month == monthState.value.currentMonth.month) 1f else 0.32f),
                                textAlign = TextAlign.Center
                            )
                            if (scheduledTrainings.value.any { training ->
                                    training.date == convertLocalDateDateToUTC(dayState.date)
                                }) {
                                val circleCount = scheduledTrainings.value.first { training ->
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
                }
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                if (isPeriodSelected) {
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 24.dp)
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "start",
                                firstSelectedDay.intValue
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "end",
                                secondSelectedDay.intValue
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "month",
                                monthState.value.currentMonth.month.value
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "plan",
                                trainingPlan
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "trainings",
                                scheduledTrainings.value
                            )
                            navController.navigate(Screen.TrainingToSchedule.route)
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.next),
                            color = FitLadyaTheme.colors.secondaryText
                        )
                    }
                }
            }

        }
    }

}