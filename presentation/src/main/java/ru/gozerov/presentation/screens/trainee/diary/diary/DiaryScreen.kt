package ru.gozerov.presentation.screens.trainee.diary.diary

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.utils.compareDates
import ru.gozerov.domain.utils.convertLocalDateDateToUTC
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.diary.diary.models.DiaryEffect
import ru.gozerov.presentation.screens.trainee.diary.diary.models.DiaryIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.SimpleTrainingCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DiaryScreen(
    viewModel: DiaryViewModel,
    contentPaddingValues: PaddingValues,
    navController: NavController
) {
    val effect = viewModel.effect.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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
    val calendarState = rememberSelectableCalendarState(monthState = monthState.value)
    calendarState.selectionState.selectionMode = SelectionMode.Single

    val primaryColor = FitLadyaTheme.colors.primary
    val todayDateBackground = FitLadyaTheme.colors.secondary
    val todayDateBackgroundState = remember { mutableStateOf(todayDateBackground) }
    val selectedDay = remember { mutableIntStateOf(-1) }

    val dayTrainings = remember { mutableStateOf<List<CustomTraining>>(emptyList()) }
    val scheduledTrainings = remember { mutableStateOf<List<ScheduledTraining>>(emptyList()) }

    val showEmpty = remember { mutableStateOf(false) }

    LaunchedEffect(null) {
        viewModel.handleIntent(DiaryIntent.GetSchedule(monthState.value.currentMonth.monthValue))
    }

    when (effect) {
        is DiaryEffect.None -> {}
        is DiaryEffect.LoadedTrainings -> {
            dayTrainings.value = effect.trainings

        }

        is DiaryEffect.LoadedSchedule -> {
            scheduledTrainings.value = effect.trainings
        }

        is DiaryEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(DiaryIntent.Reset)
        }
    }

    Scaffold(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxSize(),
        containerColor = FitLadyaTheme.colors.primaryBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 24.dp,
                    bottom = 16.dp
                ),
                text = stringResource(R.string.training_diary),
                fontSize = 22.sp,
                color = FitLadyaTheme.colors.text
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
                                .size(48.dp)
                                .padding(4.dp)
                                .background(
                                    color = if (selectedDay.intValue == dayState.date.dayOfMonth && dayState.date.month == monthState.value.currentMonth.month) FitLadyaTheme.colors.primary else FitLadyaTheme.colors.secondary,
                                    shape = CircleShape
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    if (dayState.date.month == monthState.value.currentMonth.month) {
                                        selectedDay.intValue = dayState.date.dayOfMonth
                                        todayDateBackgroundState.value = primaryColor
                                        val date = convertLocalDateDateToUTC(dayState.date)
                                        val ids = scheduledTrainings.value.firstOrNull { training ->
                                            training.date == date
                                        }
                                        if (ids != null)
                                            ids.let { t ->
                                                showEmpty.value = false
                                                viewModel.handleIntent(
                                                    DiaryIntent.GetTrainingsAtDate(t.ids)
                                                )
                                            }
                                        else {
                                            showEmpty.value = true
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
                                (0 until circleCount).forEach { _ ->
                                    Row(modifier = Modifier.padding(top = 4.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .background(
                                                    color = FitLadyaTheme.colors.accent,
                                                    shape = CircleShape
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(4.dp)
                                .background(
                                    color = if (selectedDay.intValue == dayState.date.dayOfMonth && dayState.date.month == monthState.value.currentMonth.month) FitLadyaTheme.colors.primary else FitLadyaTheme.colors.primaryBackground,
                                    shape = CircleShape
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    if (dayState.date.month == monthState.value.currentMonth.month) {
                                        selectedDay.intValue = dayState.date.dayOfMonth
                                        val date = convertLocalDateDateToUTC(dayState.date)
                                        val ids = scheduledTrainings.value.firstOrNull { training ->
                                            training.date == date
                                        }
                                        if (ids != null)
                                            ids.let { t ->
                                                showEmpty.value = false
                                                viewModel.handleIntent(
                                                    DiaryIntent.GetTrainingsAtDate(t.ids)
                                                )
                                            }
                                        else {
                                            showEmpty.value = true
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
                                })
                                Row(modifier = Modifier.padding(top = 4.dp)) {
                                    val circleCount = scheduledTrainings.value.first { training ->
                                        training.date == convertLocalDateDateToUTC(dayState.date)
                                    }.ids.size
                                    (0 until circleCount).forEach { _ ->
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .background(
                                                    color = FitLadyaTheme.colors.accent,
                                                    shape = CircleShape
                                                )
                                        )
                                    }
                                }
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (!showEmpty.value) {
                    LazyColumn(
                        modifier = Modifier.align(Alignment.TopCenter),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(dayTrainings.value.size) { index ->
                            SimpleTrainingCard(trainingCard = dayTrainings.value[index])
                        }
                    }
                    Button(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 32.dp, vertical = 16.dp)
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                        onClick = {
                            navController.navigate(Screen.FindTraining.route)
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.change_training),
                            color = FitLadyaTheme.colors.secondaryText
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_trainings_today),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = FitLadyaTheme.colors.text
                        )
                        Button(
                            modifier = Modifier
                                .padding(horizontal = 32.dp, vertical = 24.dp)
                                .fillMaxWidth()
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                            onClick = {
                                navController.navigate(Screen.FindTraining.route)
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.add_training),
                                color = FitLadyaTheme.colors.secondaryText
                            )
                        }
                    }
                }
            }
        }
    }
}

fun YearMonth.mapMonthToRu(): String {
    return when (this.month.value) {
        1 -> "январь"
        2 -> "февраль"
        3 -> "март"
        4 -> "апрель"
        5 -> "май"
        6 -> "июнь"
        7 -> "июль"
        8 -> "август"
        9 -> "сентябрь"
        10 -> "октябрь"
        11 -> "ноябрь"
        12 -> "декабрь"
        else -> error("unknown month")
    }
}