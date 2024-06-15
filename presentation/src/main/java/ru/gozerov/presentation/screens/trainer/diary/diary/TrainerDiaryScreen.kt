package ru.gozerov.presentation.screens.trainer.diary.diary

import TrainerServiceCard
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.CustomService
import ru.gozerov.domain.models.ScheduleService
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainerTrainingCard
import ru.gozerov.domain.utils.compareDates
import ru.gozerov.domain.utils.convertDateToDDMMYYYYNullable
import ru.gozerov.domain.utils.convertLocalDateDateToUTC
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.diary.diary.mapMonthToRu
import ru.gozerov.presentation.screens.trainer.diary.diary.models.TrainerDiaryEffect
import ru.gozerov.presentation.screens.trainer.diary.diary.models.TrainerDiaryIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.SimpleTrainingCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainerDiaryScreen(
    contentPaddingValues: PaddingValues,
    navController: NavController,
    viewModel: TrainerDiaryViewModel
) {
    val effect = viewModel.effect.collectAsState().value


    val trainerTrainings = remember { mutableStateOf<LazyPagingItems<TrainerTrainingCard>?>(null) }

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
    val selectedDay = rememberSaveable { mutableIntStateOf(-1) }
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

    val dayServices = rememberSaveable { mutableStateOf<List<ScheduleService>>(emptyList()) }
    val scheduledTrainings = remember { mutableStateOf<List<ScheduledTraining>>(emptyList()) }

    val showEmpty = rememberSaveable { mutableStateOf(false) }

    val tabs = listOf(stringResource(R.string.schedule), stringResource(R.string.my_trainings))
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    LaunchedEffect(null) {
        viewModel.handleIntent(TrainerDiaryIntent.GetSchedule(monthState.value.currentMonth.monthValue))
    }

    when (effect) {
        is TrainerDiaryEffect.None -> {}
        is TrainerDiaryEffect.LoadedServices -> {
            dayServices.value = effect.services
            viewModel.handleIntent(TrainerDiaryIntent.Reset)
        }

        is TrainerDiaryEffect.LoadedSchedule -> {
            scheduledTrainings.value = effect.services
            viewModel.handleIntent(TrainerDiaryIntent.LoadTrainings)
        }

        is TrainerDiaryEffect.LoadedTrainings -> {
            trainerTrainings.value = effect.trainings.collectAsLazyPagingItems()
        }

        is TrainerDiaryEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(TrainerDiaryIntent.Reset)
        }

        is TrainerDiaryEffect.RemovedService -> {
            val newTrainings = dayServices.value.toMutableList()
            newTrainings.removeIf { training -> training.id == effect.id }
            dayServices.value = newTrainings
            viewModel.handleIntent(TrainerDiaryIntent.GetSchedule(monthState.value.currentMonth.monthValue))
        }
    }

    Scaffold(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxSize(),
        containerColor = FitLadyaTheme.colors.primaryBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FitLadyaTheme.colors.secondary)
        ) {
            Text(
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = 24.dp,
                    bottom = 16.dp
                ),
                text = stringResource(R.string.training_diary),
                fontSize = 16.sp,
                color = FitLadyaTheme.colors.text
            )

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    val currentTabPosition = tabPositions[pagerState.currentPage]
                    Box(
                        Modifier
                            .tabIndicatorOffset(currentTabPosition)
                            .height(3.dp)
                            .padding(horizontal = 64.dp)
                            .background(
                                color = FitLadyaTheme.colors.primary,
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                            )
                    )
                },
                divider = {}) {
                tabs.forEach { tab ->
                    Tab(
                        modifier = Modifier.background(FitLadyaTheme.colors.secondary),
                        selected = tabs.indexOf(tab) == pagerState.currentPage,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(tabs.indexOf(tab))
                            }
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = tab,
                            fontWeight = FontWeight.Medium,
                            color = if (tabs[pagerState.currentPage] == tab) FitLadyaTheme.colors.fieldPrimaryText else FitLadyaTheme.colors.text.copy(
                                alpha = 0.48f
                            )
                        )
                    }
                }
            }
            HorizontalPager(
                modifier = Modifier
                    .weight(1f)
                    .background(FitLadyaTheme.colors.primaryBackground),
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { page ->
                if (page == 0) {
                    Column {


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

                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
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
                                                    val date =
                                                        convertLocalDateDateToUTC(dayState.date)
                                                    val ids =
                                                        scheduledTrainings.value.firstOrNull { training ->
                                                            training.date == date
                                                        }
                                                    if (ids != null)
                                                        ids.let { t ->
                                                            showEmpty.value = false
                                                            viewModel.handleIntent(
                                                                TrainerDiaryIntent.GetServicesAtDate(
                                                                    t.ids
                                                                )
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
                                            val circleCount =
                                                scheduledTrainings.value.first { training ->
                                                    training.date == convertLocalDateDateToUTC(
                                                        dayState.date
                                                    )
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
                                                    selectedDay.intValue = dayState.date.dayOfMonth
                                                    selectedDate.value = dayState.date
                                                    val date =
                                                        convertLocalDateDateToUTC(dayState.date)
                                                    val ids =
                                                        scheduledTrainings.value.firstOrNull { training ->
                                                            training.date == date
                                                        }
                                                    if (ids != null)
                                                        ids.let { t ->
                                                            showEmpty.value = false
                                                            viewModel.handleIntent(
                                                                TrainerDiaryIntent.GetServicesAtDate(
                                                                    t.ids
                                                                )
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
                                            }) {
                                            val circleCount =
                                                scheduledTrainings.value.first { training ->
                                                    training.date == convertLocalDateDateToUTC(
                                                        dayState.date
                                                    )
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (!showEmpty.value) {
                                LazyColumn(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(dayServices.value.size) { index ->
                                        TrainerServiceCard(
                                            service = dayServices.value[index],
                                            isTrainerApproved = dayServices.value[index].isTrainerApproved,
                                            onClick = { },
                                            isEditable = true,
                                            onPlan = {
                                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                                    "id",
                                                    dayServices.value[index].userId
                                                )
                                                navController.navigate(Screen.CreatePlan.route)
                                            },
                                            onBottomAction = {
                                                viewModel.handleIntent(
                                                    TrainerDiaryIntent.RemoveScheduleService(
                                                        dayServices.value[index].id
                                                    )
                                                )
                                            })
                                    }
                                    item {
                                        Spacer(modifier = Modifier.height(48.dp))
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
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "date",
                                            convertDateToDDMMYYYYNullable(selectedDate.value)
                                        )
                                        navController.navigate(Screen.CreateService.route)
                                    }
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.add_service),
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
                                        text = stringResource(id = R.string.no_services),
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
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                "date",
                                                convertDateToDDMMYYYYNullable(selectedDate.value)
                                            )
                                            navController.navigate(Screen.CreateService.route)
                                        }
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.add),
                                            color = FitLadyaTheme.colors.secondaryText
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = FitLadyaTheme.colors.primaryBackground)
                                .padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            trainerTrainings.value?.let { pagingData ->
                                items(pagingData.itemCount) { index ->
                                    val card = trainerTrainings.value!![index]
                                    card?.let { training ->
                                        SimpleTrainingCard(trainingCard = training) {
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                "id",
                                                training.id
                                            )
                                            navController.navigate(Screen.TrainerTrainingDetails.route)
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
        }
    }
}