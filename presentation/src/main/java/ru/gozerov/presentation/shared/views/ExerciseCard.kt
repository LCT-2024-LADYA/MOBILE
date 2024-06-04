package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.gozerov.domain.models.Exercise
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExerciseCard(exercise: Exercise, position: Int) {
    val underLineColor = FitLadyaTheme.colors.secondaryBackground

    Card(
        modifier = Modifier
            .padding(16.dp)
            .width(358.dp),
        colors = CardDefaults.cardColors(containerColor = FitLadyaTheme.colors.secondary)
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .size(326.dp, 224.dp),
            model = exercise.photoUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = exercise.name,
                color = FitLadyaTheme.colors.text,
                style = FitLadyaTheme.typography.heading
            )
            Box(
                modifier = Modifier.weight(0.2f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = stringResource(id = R.string.exercise_pos_is, position + 1),
                    style = FitLadyaTheme.typography.heading
                )
            }
        }

        FlowRow(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            exercise.tags.forEachIndexed { index, text ->
                ChipItem(text = text, index = index)
            }
        }

        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.working_weight_is),
                style = FitLadyaTheme.typography.body
            )
            Text(
                text = exercise.weight.toString(),
                style = FitLadyaTheme.typography.body,
                modifier = Modifier
                    .padding(start = 8.dp)
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
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.sets_and_reps),
                style = FitLadyaTheme.typography.body
            )
            Text(
                text = exercise.setsCount.toString(),
                style = FitLadyaTheme.typography.body,
                modifier = Modifier
                    .padding(start = 8.dp)
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
                style = FitLadyaTheme.typography.body,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )

            Text(
                text = exercise.repsCount.toString(),
                style = FitLadyaTheme.typography.body,
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
}

@Composable
fun ChipItem(text: String, index: Int) {
    val color = when (index) {
        0 -> FitLadyaTheme.colors.card0
        1 -> FitLadyaTheme.colors.card1
        2 -> FitLadyaTheme.colors.card2
        3 -> FitLadyaTheme.colors.card3
        else -> FitLadyaTheme.colors.card3
    }
    Text(
        modifier = Modifier
            .background(color, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = text,
        style = FitLadyaTheme.typography.body,
        fontSize = 14.sp
    )
}