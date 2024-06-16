package ru.gozerov.presentation.screens.trainee.profile.statistics

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import ru.gozerov.domain.models.ProgressCard
import ru.gozerov.domain.utils.getCurrentDayInDDMMYYYY
import ru.gozerov.domain.utils.getDayPrevMonthInDDMMYYYY
import ru.gozerov.domain.utils.parseDateToDDMMYYYY
import ru.gozerov.presentation.R
import ru.gozerov.presentation.screens.trainee.profile.statistics.models.ClientStatisticsEffect
import ru.gozerov.presentation.screens.trainee.profile.statistics.models.ClientStatisticsIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.shared.views.SearchTextField
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClientStatisticsScreen(
    navController: NavController,
    viewModel: ClientStatisticsViewModel,
    contentPaddingValues: PaddingValues
) {
    val effect = viewModel.effect.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val searchState = remember { mutableStateOf("") }
    val underlineColor = FitLadyaTheme.colors.primaryBorder

    val startTimeState =
        rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(getDayPrevMonthInDDMMYYYY()))
        }
    val endTimeState =
        rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(getCurrentDayInDDMMYYYY()))
        }

    val progressItems = remember { mutableStateOf<LazyPagingItems<ProgressCard>?>(null) }

    LaunchedEffect(null) {
        viewModel.handleIntent(
            ClientStatisticsIntent.LoadStatistics(
                searchState.value,
                startTimeState.value.text,
                endTimeState.value.text
            )
        )
    }

    when (effect) {
        is ClientStatisticsEffect.None -> {}
        is ClientStatisticsEffect.LoadedStatistics -> {
            val data = effect.statistics.collectAsLazyPagingItems()
            if (data.itemCount > 0)
                progressItems.value = data
        }

        is ClientStatisticsEffect.Error -> {
            snackbarHostState.showError(coroutineScope = coroutineScope, effect.message)
        }
    }

    Scaffold(
        modifier = Modifier
            .padding(paddingValues = contentPaddingValues)
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            NavUpWithTitleToolbar(
                navController = navController,
                title = stringResource(id = R.string.show_progress)
            )
            Spacer(modifier = Modifier.height(20.dp))
            SearchTextField(
                textState = searchState,
                onValueChange = { value ->
                    searchState.value = value
                    if (startTimeState.value.text.length == 10 && endTimeState.value.text.length == 10) {
                        viewModel.handleIntent(
                            ClientStatisticsIntent.LoadStatistics(
                                searchState.value,
                                startTimeState.value.text,
                                endTimeState.value.text
                            )
                        )
                    }
                },
                placeholderText = stringResource(id = R.string.find_exercise),
                containerColor = FitLadyaTheme.colors.secondary
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = startTimeState.value,
                    onValueChange = { value ->
                        val filteredText = value.text.filter { it.isDigit() }.let {
                            when (it.length) {
                                in 0..2 -> it
                                in 3..4 -> it.substring(0, 2) + "." + it.substring(2)
                                in 5..8 -> it.substring(0, 2) + "." + it.substring(
                                    2,
                                    4
                                ) + "." + it.substring(4)

                                else -> it.substring(0, 2) + "." + it.substring(
                                    2,
                                    4
                                ) + "." + it.substring(4, 8)
                            }
                        }

                        val cursorPosition = when {
                            value.text.length <= 2 -> value.text.length
                            value.text.length <= 4 -> value.text.length + 1
                            value.text.length <= 8 -> value.text.length + 2
                            else -> 10
                        }

                        startTimeState.value = value.copy(
                            text = filteredText,
                            selection = TextRange(cursorPosition)
                        )
                    },
                    modifier = Modifier.width(100.dp),
                    textStyle = TextStyle(
                        color = FitLadyaTheme.colors.fieldPrimaryText,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(FitLadyaTheme.colors.primaryBorder),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    decorationBox = { innerTextField ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            innerTextField()
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(1.dp)
                                    .background(underlineColor)
                            )
                        }
                    }
                )
                Text(text = " - ", color = FitLadyaTheme.colors.text)
                BasicTextField(
                    value = endTimeState.value,
                    onValueChange = { value ->
                        val filteredText = value.text.filter { it.isDigit() }.let {
                            when (it.length) {
                                in 0..2 -> it
                                in 3..4 -> it.substring(0, 2) + "." + it.substring(2)
                                in 5..8 -> it.substring(0, 2) + "." + it.substring(
                                    2,
                                    4
                                ) + "." + it.substring(4)

                                else -> it.substring(0, 2) + "." + it.substring(
                                    2,
                                    4
                                ) + "." + it.substring(4, 8)
                            }
                        }

                        val cursorPosition = when {
                            value.text.length <= 2 -> value.text.length
                            value.text.length <= 4 -> value.text.length + 1
                            value.text.length <= 8 -> value.text.length + 2
                            else -> 10
                        }

                        endTimeState.value = value.copy(
                            text = filteredText,
                            selection = TextRange(cursorPosition)
                        )
                    },
                    modifier = Modifier.width(100.dp),
                    textStyle = TextStyle(
                        color = FitLadyaTheme.colors.fieldPrimaryText,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(FitLadyaTheme.colors.primaryBorder),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    decorationBox = { innerTextField ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            innerTextField()
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(1.dp)
                                    .background(underlineColor)
                            )
                        }
                    }
                )
                IconButton(onClick = {
                    if (startTimeState.value.text.length == 10 && endTimeState.value.text.length == 10) {
                        viewModel.handleIntent(
                            ClientStatisticsIntent.LoadStatistics(
                                searchState.value,
                                startTimeState.value.text,
                                endTimeState.value.text
                            )
                        )
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_renew),
                        contentDescription = null,
                        tint = FitLadyaTheme.colors.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                progressItems.value?.let { data ->
                    items(data.itemCount) { index ->
                        val progressCard = data[index]
                        progressCard?.let { progress ->
                            ProgressCard(progress)
                        }

                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }


        }

    }
}

@Composable
fun ProgressCard(progressCard: ProgressCard) {
    if (progressCard.progresses.isNotEmpty()) {
        val dates = mutableListOf("")
        dates.addAll(progressCard.progresses.map { parseDateToDDMMYYYY(it.date) })
        dates.add("")

        val points = mutableListOf<Point>()
        points.add(Point(0f, progressCard.progresses[0].weight.toFloat()))
        progressCard.progresses.forEachIndexed { i, part ->
            points.add(Point(i.toFloat(), part.weight.toFloat()))
        }
        points.add(
            Point(
                progressCard.progresses.size.toFloat(),
                progressCard.progresses.last().weight.toFloat()
            )
        )

        val xAxisData = AxisData.Builder()
            .axisStepSize(100.dp)
            .backgroundColor(FitLadyaTheme.colors.secondary)
            .axisLabelColor(FitLadyaTheme.colors.text.copy(alpha = 0.48f))
            .axisLineColor(FitLadyaTheme.colors.secondary)
            .axisLabelFontSize(12.sp)
            .steps(points.size - 1)
            .labelData { i -> dates[i] }
            .axisOffset(48.dp)
            .labelAndAxisLinePadding(16.dp)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(points.size)
            .backgroundColor(FitLadyaTheme.colors.secondary)
            .axisLineColor(FitLadyaTheme.colors.secondary)
            .build()
        val lineChartData = LineChartData(
            containerPaddingEnd = 32.dp,
            paddingRight = 0.dp,

            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = points,
                        LineStyle(
                            lineType = LineType.Straight(),
                            color = FitLadyaTheme.colors.primary,
                        ),
                        IntersectionPoint(
                            color = FitLadyaTheme.colors.fieldPrimaryText,
                            radius = 4.dp
                        ),
                        SelectionHighlightPoint(color = FitLadyaTheme.colors.fieldPrimaryText),
                        ShadowUnderLine(alpha = 0f),
                        SelectionHighlightPopUp(
                            backgroundColor = FitLadyaTheme.colors.primaryBackground,
                            backgroundAlpha = 1f,
                            backgroundCornerRadius = CornerRadius(16f),
                            labelSize = 14.sp,
                            labelColor = FitLadyaTheme.colors.text,
                            popUpLabel = { _, _ ->
                                "   соси хуй    "
                            }
                        )
                    )
                ),
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(
                enableHorizontalLines = false,
                color = FitLadyaTheme.colors.primaryBorder
            ),
            backgroundColor = FitLadyaTheme.colors.secondary
        )
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
                .background(
                    color = FitLadyaTheme.colors.secondary,
                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                )
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                text = progressCard.name,
                fontWeight = FontWeight.Medium,
                color = FitLadyaTheme.colors.text
            )
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    lineChartData = lineChartData
                )
            }
        }

    }
}