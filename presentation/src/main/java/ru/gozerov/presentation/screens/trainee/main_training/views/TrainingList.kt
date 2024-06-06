package ru.gozerov.presentation.screens.trainee.main_training.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.domain.models.Exercise
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.views.ExerciseCard
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun TrainingList() {
    Box(modifier = Modifier.fillMaxSize()) {
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
                    text = "Тренировка груди от Геркулесова Евгения",
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
                        text = "11.09.2001",
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
                        text = "14:48",
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
                            text = "8",
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
                    text = "Легендарный тренер Евгений Геркулесов подготовил тренеровку груди состоящую из: бегит, пресс качат, анжуманя, турник, штанга, пресидат, гантелии паднимат, блок палка железо тягат",
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
            val exercise = Exercise(
                id = 1,
                photoUrl = "https://s3-alpha-sig.figma.com/img/2e34/f886/b5a779583ee84526f6e6056df16f49d5?Expires=1718582400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=lBg3M3B5rrJNdzQk0878VyrEb-6tE6mXhAEaJgpBq96WTa31sbg7zVXdt3nNLe51u4BtY-lPHaJQCic~ALImf-kkNRY8q9rbQW3yQ23otrKjT0xTu26tmCyVambij6EhJrPzR~4VV4JyGS5~1B78PeQmzF~LupXVUBke-sNdHCD64Szshbqxc3bKe17PKaON4e6pgukBjj9gfZ0Xe8gF8AbmPsAHdlzwk742makolE2GiMoDdM9-gu8~SLqqTAlmJAqEBDVtfUYtf4NDU0OyLe~b0xCGrJlP6rbJzXtarAAzLDG4hCSDP3hnc8pOV7HRTouc3UUwSRUdvha4Trx61w__",
                name = "Жим штанги лежа",
                tags = listOf("Начинающий", "Базовое", "Грудь", "Штанга"),
                weight = 50.0,
                setsCount = 4,
                repsCount = 8
            )
            items(5) {
                ExerciseCard(exercise = exercise, position = it)
            }
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
        Button(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .fillMaxWidth()
                .height(48.dp)
                .align(Alignment.BottomCenter),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
            onClick = { }
        ) {
            Text(
                text = stringResource(id = R.string.start_training),
                color = FitLadyaTheme.colors.secondaryText
            )
        }
    }

}