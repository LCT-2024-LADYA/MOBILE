package ru.gozerov.presentation.screens.trainee.diary.create_training

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.gozerov.domain.models.Exercise
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.shared.views.ExerciseCard
import ru.gozerov.presentation.shared.views.NavUpToolbar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateTrainingScreen(navController: NavController) {

    val trainingName = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }
    val time = remember { mutableStateOf("") }

    val exercises = (0..4).map {
        Exercise(
            id = it,
            photos = listOf(
                "https://s3-alpha-sig.figma.com/img/2e34/f886/b5a779583ee84526f6e6056df16f49d5?Expires=1718582400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=lBg3M3B5rrJNdzQk0878VyrEb-6tE6mXhAEaJgpBq96WTa31sbg7zVXdt3nNLe51u4BtY-lPHaJQCic~ALImf-kkNRY8q9rbQW3yQ23otrKjT0xTu26tmCyVambij6EhJrPzR~4VV4JyGS5~1B78PeQmzF~LupXVUBke-sNdHCD64Szshbqxc3bKe17PKaON4e6pgukBjj9gfZ0Xe8gF8AbmPsAHdlzwk742makolE2GiMoDdM9-gu8~SLqqTAlmJAqEBDVtfUYtf4NDU0OyLe~b0xCGrJlP6rbJzXtarAAzLDG4hCSDP3hnc8pOV7HRTouc3UUwSRUdvha4Trx61w__",
                "https://s3-alpha-sig.figma.com/img/2e34/f886/b5a779583ee84526f6e6056df16f49d5?Expires=1718582400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=lBg3M3B5rrJNdzQk0878VyrEb-6tE6mXhAEaJgpBq96WTa31sbg7zVXdt3nNLe51u4BtY-lPHaJQCic~ALImf-kkNRY8q9rbQW3yQ23otrKjT0xTu26tmCyVambij6EhJrPzR~4VV4JyGS5~1B78PeQmzF~LupXVUBke-sNdHCD64Szshbqxc3bKe17PKaON4e6pgukBjj9gfZ0Xe8gF8AbmPsAHdlzwk742makolE2GiMoDdM9-gu8~SLqqTAlmJAqEBDVtfUYtf4NDU0OyLe~b0xCGrJlP6rbJzXtarAAzLDG4hCSDP3hnc8pOV7HRTouc3UUwSRUdvha4Trx61w__"
            ),
            name = "Жим штанги лежа",
            tags = listOf("Начинающий", "Базовое", "Грудь", "Штанга"),
            weight = 50.0,
            setsCount = 4,
            repsCount = 8
        )
    }

    Scaffold(containerColor = FitLadyaTheme.colors.primaryBackground) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                NavUpToolbar(navController = navController)
                Spacer(modifier = Modifier.height(8.dp))
                CustomTextField(
                    textState = trainingName,
                    labelText = stringResource(id = R.string.training_name),
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(
                    textState = date,
                    labelText = stringResource(id = R.string.date),
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(
                    textState = time,
                    labelText = stringResource(id = R.string.time),
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                    onClick = {

                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.add_exercise),
                        color = FitLadyaTheme.colors.secondaryText
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(exercises.size) { index ->
                        ExerciseCard(exercise = exercises[index], position = index)
                    }
                    item {
                        Spacer(modifier = Modifier.height(56.dp))
                    }
                }
            }
            Button(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                onClick = {

                }
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    color = FitLadyaTheme.colors.secondaryText
                )
            }
        }
    }
}