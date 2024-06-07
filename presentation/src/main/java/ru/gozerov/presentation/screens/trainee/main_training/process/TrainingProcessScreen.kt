package ru.gozerov.presentation.screens.trainee.main_training.process

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.Exercise
import ru.gozerov.domain.models.Training
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.views.ChipItem
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainingProcessScreen(
    navController: NavController,
    contentPaddingValues: PaddingValues,
    training: Training
) {
    val coroutineScope = rememberCoroutineScope()

    val currentPosition = remember { mutableIntStateOf(0) }
    val currentSet = remember { mutableIntStateOf(1) }

    val snackbarHostState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(pageCount = { training.exerciseCount} )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            currentSet.intValue = 1
            currentPosition.intValue = page
        }
    }

    Scaffold(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = training.name,
                fontSize = 16.sp,
                color = FitLadyaTheme.colors.text
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.padding(
                    horizontal = 32.dp
                )
            ) {
                Text(
                    text = training.date,
                    modifier = Modifier
                        .border(
                            1.dp,
                            FitLadyaTheme.colors.secondaryBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Medium,
                    color = FitLadyaTheme.colors.text
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = training.time,
                    modifier = Modifier
                        .border(
                            1.dp,
                            FitLadyaTheme.colors.secondaryBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Medium,
                    color = FitLadyaTheme.colors.text
                )
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    modifier = Modifier
                        .border(
                            1.dp,
                            FitLadyaTheme.colors.secondaryBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = training.exerciseCount.toString(),
                        fontWeight = FontWeight.Medium,
                        color = FitLadyaTheme.colors.text
                    )
                    Icon(
                        modifier = Modifier.padding(start = 4.dp),
                        painter = painterResource(id = R.drawable.ic_training),
                        contentDescription = null,
                        tint = FitLadyaTheme.colors.accent
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalPager(state = pagerState) { page ->
                val imagePagerState =
                    rememberPagerState(pageCount = { training.exercises[page].photos.size })
                ProcessExerciseCard(
                    imagePagerState = imagePagerState,
                    exercise = training.exercises[page],
                    set = currentSet.intValue,
                    position = page,
                    onNextSet = {
                        if (currentSet.intValue < training.exercises[page].setsCount)
                            currentSet.intValue += 1
                        else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    coroutineScope = coroutineScope,
                    onSkip = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                )
            }

            Row(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val size = if (pagerState.currentPage == iteration) 10.dp else 8.dp
                    val color =
                        if (pagerState.currentPage == iteration) FitLadyaTheme.colors.accent else FitLadyaTheme.colors.primaryBorder
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(size)
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProcessExerciseCard(
    imagePagerState: PagerState,
    exercise: Exercise,
    set: Int,
    position: Int,
    onNextSet: () -> Unit,
    onSkip: () -> Unit,
    coroutineScope: CoroutineScope
) {
    val underLineColor = FitLadyaTheme.colors.primaryBorder

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FitLadyaTheme.colors.secondary)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            HorizontalPager(state = imagePagerState) { page ->
                AsyncImage(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .height(228.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            }
                        ) {
                            val newPage =
                                if (imagePagerState.currentPage < imagePagerState.pageCount - 1) imagePagerState.currentPage + 1 else 0
                            coroutineScope.launch {
                                imagePagerState.animateScrollToPage(newPage)
                            }
                        },
                    model = exercise.photos[page],
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(imagePagerState.pageCount) { iteration ->
                    val color =
                        if (imagePagerState.currentPage == iteration) FitLadyaTheme.colors.text else FitLadyaTheme.colors.text.copy(
                            alpha = 0.32f
                        )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(100))
                            .background(color)
                            .height(2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
        ) {

            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = exercise.name,
                    color = FitLadyaTheme.colors.text,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier.weight(0.2f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = stringResource(id = R.string.exercise_pos_is, position + 1),
                        color = FitLadyaTheme.colors.accent,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            FlowRow(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                exercise.tags.forEach { text ->
                    ChipItem(text = text)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id = R.string.set_is, set, exercise.setsCount),
                color = FitLadyaTheme.colors.fieldPrimaryText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.working_weight_is),
                fontWeight = FontWeight.Medium,
                color = FitLadyaTheme.colors.text
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = exercise.weight.toString(),
                    color = FitLadyaTheme.colors.fieldPrimaryText,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .drawBehind {
                            val strokeWidthPx = 1.dp.toPx()
                            val verticalOffset = size.height
                            drawLine(
                                color = underLineColor,
                                strokeWidth = strokeWidthPx,
                                start = Offset(0f, verticalOffset),
                                end = Offset(size.width, verticalOffset)
                            )
                        }
                        .padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.kg),
                    color = FitLadyaTheme.colors.text
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.sets_and_reps),
                fontWeight = FontWeight.Medium,
                color = FitLadyaTheme.colors.text
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row {

                Text(
                    text = exercise.setsCount.toString(),
                    color = FitLadyaTheme.colors.fieldPrimaryText,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .drawBehind {
                            val strokeWidthPx = 1.dp.toPx()
                            val verticalOffset = size.height
                            drawLine(
                                color = underLineColor,
                                strokeWidth = strokeWidthPx,
                                start = Offset(0f, verticalOffset),
                                end = Offset(size.width, verticalOffset)
                            )
                        }
                        .padding(horizontal = 8.dp)
                )

                Text(
                    text = stringResource(id = R.string.x),
                    color = FitLadyaTheme.colors.text,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )

                Text(
                    text = exercise.repsCount.toString(),
                    color = FitLadyaTheme.colors.fieldPrimaryText,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .drawBehind {
                            val strokeWidthPx = 1.dp.toPx()
                            val verticalOffset = size.height
                            drawLine(
                                color = underLineColor,
                                strokeWidth = strokeWidthPx,
                                start = Offset(0f, verticalOffset),
                                end = Offset(size.width, verticalOffset)
                            )
                        }
                        .padding(horizontal = 8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
            onClick = {
                onNextSet()
            }
        ) {
            Text(
                text = stringResource(id = R.string.next_set),
                color = FitLadyaTheme.colors.secondaryText
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(id = R.string.skip),
                color = FitLadyaTheme.colors.accent,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }
                ) { onSkip() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}