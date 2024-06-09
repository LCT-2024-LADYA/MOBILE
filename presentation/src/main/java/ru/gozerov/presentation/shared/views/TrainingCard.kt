package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.background
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
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

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
                    modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                    text = trainingCard.date,
                    color = FitLadyaTheme.colors.accent,
                    fontWeight = FontWeight.Medium
                )
                Box(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(end = 8.dp, top = 16.dp),
                    text = trainingCard.exerciseCount.toString(),
                    color = FitLadyaTheme.colors.text,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    modifier = Modifier.padding(end = 24.dp, top = 16.dp),
                    painter = painterResource(id = R.drawable.ic_training),
                    contentDescription = null,
                    tint = FitLadyaTheme.colors.fieldPrimaryText
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = trainingCard.name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = FitLadyaTheme.colors.text
            )
            Spacer(modifier = Modifier.height(8.dp))
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