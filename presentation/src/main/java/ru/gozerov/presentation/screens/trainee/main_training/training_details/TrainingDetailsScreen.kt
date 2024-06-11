package ru.gozerov.presentation.screens.trainee.main_training.training_details

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.gozerov.domain.models.Exercise
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.views.ExerciseCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainingDetailsScreen(navController: NavController) {

    val snackbarHostState = remember { SnackbarHostState() }

    val trainingName = remember { mutableStateOf("Тренировка груди от Геркулесова Евгения") }
    val trainingDate = remember { mutableStateOf("11.09.2001") }
    val trainingTime = remember { mutableStateOf("14:48") }
    val exerciseCount = remember { mutableIntStateOf(0) }
    val trainingDescription =
        remember { mutableStateOf("Легендарный тренер Евгений Геркулесов подготовил тренеровку груди состоящую из: бегит, пресс качат, анжуманя, турник, штанга, пресидат, гантелии паднимат, блок палка железо тягат") }
    val currentExercises = remember {
        mutableStateOf(
            (0..4).map {
                Exercise(
                    id = 1,
                    photos = listOf(
                        "https://s3-alpha-sig.figma.com/img/2e34/f886/b5a779583ee84526f6e6056df16f49d5?Expires=1718582400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=lBg3M3B5rrJNdzQk0878VyrEb-6tE6mXhAEaJgpBq96WTa31sbg7zVXdt3nNLe51u4BtY-lPHaJQCic~ALImf-kkNRY8q9rbQW3yQ23otrKjT0xTu26tmCyVambij6EhJrPzR~4VV4JyGS5~1B78PeQmzF~LupXVUBke-sNdHCD64Szshbqxc3bKe17PKaON4e6pgukBjj9gfZ0Xe8gF8AbmPsAHdlzwk742makolE2GiMoDdM9-gu8~SLqqTAlmJAqEBDVtfUYtf4NDU0OyLe~b0xCGrJlP6rbJzXtarAAzLDG4hCSDP3hnc8pOV7HRTouc3UUwSRUdvha4Trx61w__",
                        "https://s3-alpha-sig.figma.com/img/2e34/f886/b5a779583ee84526f6e6056df16f49d5?Expires=1718582400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=lBg3M3B5rrJNdzQk0878VyrEb-6tE6mXhAEaJgpBq96WTa31sbg7zVXdt3nNLe51u4BtY-lPHaJQCic~ALImf-kkNRY8q9rbQW3yQ23otrKjT0xTu26tmCyVambij6EhJrPzR~4VV4JyGS5~1B78PeQmzF~LupXVUBke-sNdHCD64Szshbqxc3bKe17PKaON4e6pgukBjj9gfZ0Xe8gF8AbmPsAHdlzwk742makolE2GiMoDdM9-gu8~SLqqTAlmJAqEBDVtfUYtf4NDU0OyLe~b0xCGrJlP6rbJzXtarAAzLDG4hCSDP3hnc8pOV7HRTouc3UUwSRUdvha4Trx61w__"
                    ),
                    name = "Жим штанги лежа",
                    tags = listOf("Начинающий", "Базовое", "Грудь", "Штанга"),
                )
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(16.dp))
            IconButton(
                modifier = Modifier.padding(start = 16.dp),
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(6.dp),
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = FitLadyaTheme.colors.text
                )
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        modifier = Modifier
                            .padding(start = 32.dp)
                            .width(200.dp),
                        text = stringResource(id = R.string.next_training),
                        color = FitLadyaTheme.colors.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 32.dp)
                            .fillMaxWidth(),
                        color = FitLadyaTheme.colors.secondaryBorder
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = trainingName.value,
                        fontSize = 22.sp,
                        color = FitLadyaTheme.colors.text
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 32.dp
                        )
                    ) {
                        Text(
                            text = trainingDate.value,
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    FitLadyaTheme.colors.secondaryBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Medium,
                            color = FitLadyaTheme.colors.text
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = trainingTime.value,
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    FitLadyaTheme.colors.secondaryBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Medium,
                            color = FitLadyaTheme.colors.text
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    FitLadyaTheme.colors.secondaryBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = exerciseCount.intValue.toString(),
                                fontWeight = FontWeight.Medium,
                                color = FitLadyaTheme.colors.text
                            )
                            Icon(
                                modifier = Modifier.padding(start = 4.dp),
                                painter = painterResource(id = R.drawable.ic_training),
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.accent
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = trainingDescription.value,
                        color = FitLadyaTheme.colors.text
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 32.dp)
                            .fillMaxWidth(),
                        color = FitLadyaTheme.colors.secondaryBorder
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                }
                items(currentExercises.value.size) { index ->
                   // ExerciseCard(exercise = currentExercises.value[index], position = index)
                }
                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }
    }
}