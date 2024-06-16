package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.models.TrainerTrainingCard
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.domain.models.TrainingPlan
import ru.gozerov.domain.models.TrainingPlanCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun SimpleTrainingCard(trainingCard: CustomTraining, onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick?.invoke() }
            .background(color = FitLadyaTheme.colors.primary, shape = RoundedCornerShape(18.dp))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
                .background(
                    color = FitLadyaTheme.colors.secondary,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .weight(1f),
                    text = trainingCard.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = FitLadyaTheme.colors.text
                )
                Row(
                    modifier = Modifier
                        .padding(end = 16.dp, top = 16.dp)
                        .border(
                            width = 1.dp,
                            color = FitLadyaTheme.colors.primaryBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = trainingCard.exercises.size.toString(),
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_training),
                        contentDescription = null,
                        tint = FitLadyaTheme.colors.fieldPrimaryText
                    )
                }
            }
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = trainingCard.description,
                fontSize = 12.sp,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.72f)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun SimpleTrainingCard(trainingCard: TrainerTrainingCard, onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick?.invoke() }
            .background(color = FitLadyaTheme.colors.primary, shape = RoundedCornerShape(18.dp))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
                .background(
                    color = FitLadyaTheme.colors.secondary,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .weight(1f),
                    text = trainingCard.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = FitLadyaTheme.colors.text
                )
                Row(
                    modifier = Modifier
                        .padding(end = 16.dp, top = 16.dp)
                        .border(
                            width = 1.dp,
                            color = FitLadyaTheme.colors.primaryBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = trainingCard.exercises.toString(),
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_training),
                        contentDescription = null,
                        tint = FitLadyaTheme.colors.fieldPrimaryText
                    )
                }
            }
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = trainingCard.description,
                fontSize = 12.sp,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.72f)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SimpleTrainingCardEditable(
    trainingCard: CustomTraining,
    onClick: (() -> Unit)? = null,
    onCopy: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick?.invoke() }
            .background(color = FitLadyaTheme.colors.primary, shape = RoundedCornerShape(18.dp))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
                .background(
                    color = FitLadyaTheme.colors.secondary,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .weight(1f),
                    text = trainingCard.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = FitLadyaTheme.colors.text
                )
                Row(
                    modifier = Modifier
                        .padding(end = 16.dp, top = 16.dp)
                        .border(
                            width = 1.dp,
                            color = FitLadyaTheme.colors.primaryBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = trainingCard.exercises.size.toString(),
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_training),
                        contentDescription = null,
                        tint = FitLadyaTheme.colors.fieldPrimaryText
                    )
                }
            }
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = trainingCard.description,
                fontSize = 12.sp,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.72f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.copy_training),
                    fontWeight = FontWeight.Medium,
                    color = FitLadyaTheme.colors.fieldPrimaryText,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onCopy()
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.remove),
                    fontWeight = FontWeight.Medium,
                    color = FitLadyaTheme.colors.accent,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onDelete()
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}


@Composable
fun SimpleTrainingCardEditable(
    trainingCard: TrainingCard,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { }
            .background(color = FitLadyaTheme.colors.primary, shape = RoundedCornerShape(18.dp))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
                .background(
                    color = FitLadyaTheme.colors.secondary,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .weight(1f),
                    text = trainingCard.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = FitLadyaTheme.colors.text
                )
                Row(
                    modifier = Modifier
                        .padding(end = 16.dp, top = 16.dp)
                        .border(
                            width = 1.dp,
                            color = FitLadyaTheme.colors.primaryBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = trainingCard.exercises.toString(),
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_training),
                        contentDescription = null,
                        tint = FitLadyaTheme.colors.fieldPrimaryText
                    )
                }
            }
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = trainingCard.description,
                fontSize = 12.sp,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.72f)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.remove),
                fontWeight = FontWeight.Medium,
                color = FitLadyaTheme.colors.accent,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onDelete()
                    }
            )
            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}

@Composable
fun SimpleTrainingCard(trainingCard: TrainingCard, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                onClick()
            }
            .background(color = FitLadyaTheme.colors.primary, shape = RoundedCornerShape(18.dp))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
                .background(
                    color = FitLadyaTheme.colors.secondary,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .weight(1f),
                    text = trainingCard.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = FitLadyaTheme.colors.text
                )
                Row(
                    modifier = Modifier
                        .padding(end = 16.dp, top = 16.dp)
                        .border(
                            width = 1.dp,
                            color = FitLadyaTheme.colors.primaryBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = trainingCard.exercises.toString(),
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_training),
                        contentDescription = null,
                        tint = FitLadyaTheme.colors.fieldPrimaryText
                    )
                }
            }
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = trainingCard.description,
                fontSize = 12.sp,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.72f)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun SimplePlanCard(plan: TrainingPlanCard, onClick: () -> Unit, onManage: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick.invoke() }
            .background(color = FitLadyaTheme.colors.primary, shape = RoundedCornerShape(18.dp))
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
                .background(
                    color = FitLadyaTheme.colors.secondary,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .weight(1f),
                    text = plan.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = FitLadyaTheme.colors.text
                )
                Row(
                    modifier = Modifier
                        .padding(end = 16.dp, top = 16.dp)
                        .border(
                            width = 1.dp,
                            color = FitLadyaTheme.colors.primaryBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = plan.trainings.toString(),
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_training_2),
                        contentDescription = null,
                        tint = FitLadyaTheme.colors.fieldPrimaryText
                    )
                }
            }
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = plan.description,
                fontSize = 12.sp,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.72f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                onClick = onManage
            ) {
                Text(
                    text = stringResource(id = R.string.manage_plan),
                    color = FitLadyaTheme.colors.secondaryText
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
