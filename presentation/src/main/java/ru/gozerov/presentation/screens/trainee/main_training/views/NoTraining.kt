package ru.gozerov.presentation.screens.trainee.main_training.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun NoTraining() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_trainings),
            color = FitLadyaTheme.colors.primary,
            fontFamily = FontFamily(Font(R.font.russo_one)),
            fontSize = 36.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.chill_out),
            fontWeight = FontWeight.Medium,
            color = FitLadyaTheme.colors.text
        )
        Spacer(modifier = Modifier.height(64.dp))
        Button(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .width(264.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
            onClick = { }
        ) {
            Text(
                text = stringResource(id = R.string.new_training),
                color = FitLadyaTheme.colors.secondaryText
            )
        }
    }
}