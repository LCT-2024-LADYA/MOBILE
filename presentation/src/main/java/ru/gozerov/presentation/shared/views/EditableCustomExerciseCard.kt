package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.gozerov.domain.models.Exercise
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditableCustomExerciseCard(
    exercise: Exercise,
    weightState: MutableState<String>,
    setsState: MutableState<String>,
    repsState: MutableState<String>,
    position: Int,
    onRemove: () -> Unit
) {
    val underlineColor = FitLadyaTheme.colors.primaryBorder

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
                    BasicTextField(
                        value = weightState.value,
                        onValueChange = { value -> weightState.value = value },
                        modifier = Modifier.width(32.dp),
                        textStyle = TextStyle(
                            color = FitLadyaTheme.colors.accent,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        ),
                        cursorBrush = SolidColor(FitLadyaTheme.colors.primaryBorder),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        decorationBox = { innerTextField ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                innerTextField()
                                Box(
                                    modifier = Modifier
                                        .width(56.dp)
                                        .height(1.dp)
                                        .background(underlineColor)
                                )
                            }
                        }
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
                    BasicTextField(
                        value = setsState.value,
                        onValueChange = { value -> setsState.value = value },
                        modifier = Modifier.width(24.dp),
                        textStyle = TextStyle(
                            color = FitLadyaTheme.colors.accent,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        ),
                        cursorBrush = SolidColor(FitLadyaTheme.colors.primaryBorder),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        decorationBox = { innerTextField ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                innerTextField()
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(1.dp)
                                        .background(underlineColor)
                                )
                            }
                        }
                    )

                    Text(
                        text = stringResource(id = R.string.x),
                        color = FitLadyaTheme.colors.text,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )

                    BasicTextField(
                        value = repsState.value,
                        onValueChange = { value -> repsState.value = value },
                        modifier = Modifier.width(24.dp),
                        textStyle = TextStyle(
                            color = FitLadyaTheme.colors.accent,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        ),
                        cursorBrush = SolidColor(FitLadyaTheme.colors.primaryBorder),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        decorationBox = { innerTextField ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                innerTextField()
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(1.dp)
                                        .background(underlineColor)
                                )
                            }
                        }
                    )

                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier.clickable(indication = null, interactionSource = remember {
                    MutableInteractionSource()
                }) {
                    onRemove()
                },
                fontWeight = FontWeight.Medium,
                text = stringResource(id = R.string.remove_exercise),
                color = FitLadyaTheme.colors.errorText
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}