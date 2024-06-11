package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.gozerov.domain.models.CustomExercise
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomExerciseCard(exercise: CustomExercise, position: Int) {
    val underLineColor = FitLadyaTheme.colors.primaryBorder

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FitLadyaTheme.colors.secondary)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = exercise.name,
                color = FitLadyaTheme.colors.text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Box(
                modifier = Modifier.weight(0.2f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = stringResource(id = R.string.exercise_pos_is, position + 1),
                    color = FitLadyaTheme.colors.fieldPrimaryText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        FlowRow(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            exercise.tags.forEach { text ->
                ChipItem(text = text)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            AsyncImage(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .size(140.dp, 96.dp),
                model = exercise.photos.firstOrNull(),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column(
                modifier = Modifier.padding(start = 8.dp, end = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.working_weight_is),
                    fontWeight = FontWeight.Medium,
                    color = FitLadyaTheme.colors.text
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = exercise.weight.toString(),
                        color = FitLadyaTheme.colors.accent,
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

                Spacer(modifier = Modifier.height(4.dp))
                Row {

                    Text(
                        text = exercise.sets.toString(),
                        color = FitLadyaTheme.colors.accent,
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
                        text = exercise.reps.toString(),
                        color = FitLadyaTheme.colors.accent,
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

        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}