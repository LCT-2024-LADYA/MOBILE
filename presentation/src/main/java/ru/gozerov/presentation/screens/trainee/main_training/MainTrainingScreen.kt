package ru.gozerov.presentation.screens.trainee.main_training

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ru.gozerov.domain.models.Exercise
import ru.gozerov.domain.models.Training
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.navigation.trainee.TraineeBottomNavBarItem
import ru.gozerov.presentation.screens.trainee.main_training.process.TrainingProcessScreen
import ru.gozerov.presentation.screens.trainee.main_training.views.MainTraining
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainTrainingScreen(
    navController: NavController,
    contentPaddingValues: PaddingValues
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val currentTraining = remember {
        mutableStateOf(
            Training(
                id = 0,
                name = "Тренировка груди от Геркулесова Евгения",
                date = "11.09.2001",
                time = "14:48",
                exerciseCount = 5,
                description = "Легендарный тренер Евгений Геркулесов подготовил тренеровку груди состоящую из: бегит, пресс качат, анжуманя, турник, штанга, пресидат, гантелии паднимат, блок палка железо тягат",
                exercises = (0..4).map {
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
            )
        )
    }

    Scaffold(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        MainTraining(
            training = currentTraining.value,
            onStartClicked = {
                navController.currentBackStackEntry?.savedStateHandle?.set("training", currentTraining.value)
                navController.navigate(Screen.TrainingProcess.route)
            }
        )
        //NoTraining()
    }
}