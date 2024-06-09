package ru.gozerov.presentation.screens.trainee.diary.find_exercise

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.gozerov.domain.models.Exercise
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.views.ExerciseCard
import ru.gozerov.presentation.shared.views.NavUpToolbar
import ru.gozerov.presentation.shared.views.SearchTextField
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FindExerciseScreen(navController: NavController) {

    val searchText = remember { mutableStateOf("") }

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
        Column {
            NavUpToolbar(navController = navController)
            Spacer(modifier = Modifier.height(8.dp))
            SearchTextField(
                textState = searchText,
                placeholderText = stringResource(id = R.string.find_exercise),
                containerColor = FitLadyaTheme.colors.secondary
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(exercises.size) { index ->
                    ExerciseCard(exercise = exercises[index], position = index)
                }
            }
        }


    }
}