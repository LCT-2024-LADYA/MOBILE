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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.utils.parseDateToDDMMYYYY
import ru.gozerov.domain.utils.parseDateToHoursAndMinutes
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.views.CustomExerciseCard
import ru.gozerov.presentation.shared.views.NavUpToolbar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainingDetailsScreen(navController: NavController, training: CustomTraining) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            NavUpToolbar(navController = navController)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = training.name,
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
                            text = parseDateToDDMMYYYY(training.date),
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
                            text = "${
                                parseDateToHoursAndMinutes(
                                    training.date,
                                    training.timeStart
                                )
                            } - ${parseDateToHoursAndMinutes(training.date, training.timeEnd)}",
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
                                text = training.exercises.size.toString(),
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
                        text = training.description,
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
                items(training.exercises.size) { index ->
                    CustomExerciseCard(exercise = training.exercises[index], position = index)
                }
                item {
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }
        }
    }
}