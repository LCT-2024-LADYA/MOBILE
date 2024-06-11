package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun SimpleTrainingCard(trainingCard: CustomTraining) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
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
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp).weight(1f),
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
fun SimpleTrainingCard(trainingCard: TrainingCard) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
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
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp).weight(1f),
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