package ru.gozerov.presentation.screens.trainer.diary.paste_training

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
import ru.gozerov.domain.utils.convertDateToDDMMYYYY
import ru.gozerov.domain.utils.convertLocalDateDateToUTC
import ru.gozerov.presentation.R
import ru.gozerov.presentation.screens.trainee.diary.diary.mapMonthToRu
import ru.gozerov.presentation.screens.trainer.diary.paste_training.models.PasteTrainingEffect
import ru.gozerov.presentation.screens.trainer.diary.paste_training.models.PasteTrainingIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.DateHHMMTextField
import ru.gozerov.presentation.shared.views.SimpleTrainingCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PasteTrainingScreen(
    contentPaddingValues: PaddingValues,
    scheduledTrainings: List<ScheduledTraining>,
    training: CustomTraining,
    navController: NavController,
    viewModel: PasteTrainingViewModel
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
    val timeStart =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val timeEnd =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val calendarState = rememberSelectableCalendarState(monthState = monthState.value)
    calendarState.selectionState.selectionMode = SelectionMode.Single

    val primaryColor = FitLadyaTheme.colors.primary
    val todayDateBackground = FitLadyaTheme.colors.secondary
    val todayDateBackgroundState = remember { mutableStateOf(todayDateBackground) }
    val selectedDay = rememberSaveable { mutableIntStateOf(-1) }
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

    when (effect) {
        is PasteTrainingEffect.None -> {}
        is PasteTrainingEffect.Pasted -> {
            navController.popBackStack()
            viewModel.handleIntent(PasteTrainingIntent.Reset)
        }

        is PasteTrainingEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
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
            Spacer(modifier = Modifier.height(24.dp))
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
                                        selectedDate.value = dayState.date
                                        todayDateBackgroundState.value = primaryColor
                                    }
                                },
                                fontSize = 16.sp,
                                text = dayState.date.dayOfMonth.toString(),
                                color = FitLadyaTheme.colors.text.copy(alpha = if (dayState.date.month == monthState.value.currentMonth.month) 1f else 0.32f),
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
                                        selectedDate.value = dayState.date
                                        selectedDay.intValue = dayState.date.dayOfMonth
                                    }
                                },
                                fontSize = 16.sp,
                                text = dayState.date.dayOfMonth.toString(),
                                color = FitLadyaTheme.colors.text.copy(alpha = if (dayState.date.month == monthState.value.currentMonth.month) 1f else 0.32f),
                                textAlign = TextAlign.Center
                            )
                            if (scheduledTrainings.any { training ->
                                    training.date == convertLocalDateDateToUTC(dayState.date)
                                }) {
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
                }
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (selectedDay.intValue == -1) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = stringResource(id = R.string.choose_the_day),
                            fontSize = 22.sp,
                            color = FitLadyaTheme.colors.text
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        SimpleTrainingCard(trainingCard = training) { /*Not clickable*/ }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(id = R.string.cancel),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = FitLadyaTheme.colors.accent,
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                navController.popBackStack()
                            }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    } else {
                        Text(
                            text = stringResource(
                                id = R.string.are_you_sure_pasting,
                                selectedDay.intValue,
                                monthState.value.currentMonth.monthValue.mapMonthToRu(),
                                currentDate.year
                            ),
                            textAlign = TextAlign.Center,
                            color = FitLadyaTheme.colors.text,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 64.dp)
                        )
                        Spacer(modifier = Modifier.height(32.dp))

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
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OutlinedButton(
                                modifier = Modifier
                                    .size(160.dp, 40.dp),
                                border = BorderStroke(1.dp, FitLadyaTheme.colors.primary),
                                colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primaryBackground),
                                onClick = {
                                    navController.popBackStack()
                                }
                            ) {
                                Text(
                                    modifier = Modifier.padding(end = 8.dp),
                                    text = stringResource(R.string.back),
                                    fontWeight = FontWeight.Medium,
                                    color = FitLadyaTheme.colors.buttonText,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                modifier = Modifier.size(160.dp, 40.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),

                                onClick = {
                                    selectedDate.value?.let { date ->
                                        if (timeStart.value.text.isNotBlank() && timeEnd.value.text.isNotBlank())
                                            viewModel.handleIntent(
                                                PasteTrainingIntent.ScheduleTraining(
                                                    training.trainingId,
                                                    convertDateToDDMMYYYY(date),
                                                    timeStart.value.text,
                                                    timeEnd.value.text,
                                                    training.exercises
                                                )
                                            )
                                    }
                                }
                            ) {
                                Text(
                                    style = FitLadyaTheme.typography.body,
                                    text = stringResource(id = R.string.paste),
                                    fontWeight = FontWeight.Medium,
                                    color = FitLadyaTheme.colors.secondaryText,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}


fun Int.mapMonthToRu(): String {
    return when (this) {
        1 -> "января"
        2 -> "февраля"
        3 -> "марта"
        4 -> "апреля"
        5 -> "мая"
        6 -> "июня"
        7 -> "июля"
        8 -> "августа"
        9 -> "сентября"
        10 -> "октября"
        11 -> "ноября"
        12 -> "декабря"
        else -> error("unknown month")
    }
}