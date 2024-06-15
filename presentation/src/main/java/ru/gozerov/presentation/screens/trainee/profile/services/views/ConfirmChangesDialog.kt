package ru.gozerov.presentation.screens.trainee.profile.services.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun ConfirmChangesDialog(
    isPositive: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (isPositive: Boolean) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = FitLadyaTheme.colors.secondary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Text(
                    text = stringResource(id = R.string.are_you_sure),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = FitLadyaTheme.colors.text
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                    onClick = {
                        onConfirm(isPositive)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.change),
                        fontWeight = FontWeight.Medium,
                        color = FitLadyaTheme.colors.secondaryText
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    border = BorderStroke(width = 1.dp, color = FitLadyaTheme.colors.primary),
                    colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.secondary),
                    onClick = onDismiss
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        fontWeight = FontWeight.Medium,
                        color = FitLadyaTheme.colors.buttonText
                    )
                }
            }
        }
    }
}