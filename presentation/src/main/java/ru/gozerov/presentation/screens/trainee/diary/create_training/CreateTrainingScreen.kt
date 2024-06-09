package ru.gozerov.presentation.screens.trainee.diary.create_training

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateTrainingScreen() {

    val trainingName = remember { mutableStateOf("") }

    Scaffold(containerColor = FitLadyaTheme.colors.secondary) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                CustomTextField(
                    textState = trainingName,
                    labelText = stringResource(id = R.string.training_name),
                    modifier = Modifier.width(260.dp),
                )
            }
        }
    }
}