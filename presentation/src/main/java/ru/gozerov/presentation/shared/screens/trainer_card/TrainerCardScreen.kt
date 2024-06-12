package ru.gozerov.presentation.shared.screens.trainer_card

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ru.gozerov.domain.models.TrainerInfo
import ru.gozerov.domain.utils.getAgeString
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.shared.screens.trainer_card.models.TrainerCardEffect
import ru.gozerov.presentation.shared.screens.trainer_card.models.TrainerCardIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.utils.toChatCard
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainerCardScreen(
    trainerId: Int,
    navController: NavController,
    viewModel: TrainerCardViewModel
) {
    val effect = viewModel.effect.collectAsState().value

    val availableSex =
        listOf(stringResource(id = R.string.sex_man_d), stringResource(id = R.string.sex_woman_d))

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val underLineColor = FitLadyaTheme.colors.accent

    val trainerState = remember { mutableStateOf<TrainerInfo?>(null) }

    when (effect) {
        is TrainerCardEffect.None -> {}
        is TrainerCardEffect.LoadedProfile -> {
            trainerState.value = effect.trainer
        }

        is TrainerCardEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(TrainerCardIntent.Reset)
        }
    }

    LaunchedEffect(null) {
        viewModel.handleIntent(TrainerCardIntent.LoadProfile(trainerId))
    }

    val trainer = trainerState.value

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        trainer?.let {
            Column(modifier = Modifier.fillMaxSize()) {
                NavUpWithTitleToolbar(
                    navController = navController,
                    title = "${trainer.firstName} ${trainer.lastName}"
                )
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {

                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .height(360.dp)
                            .background(
                                FitLadyaTheme.colors.avatarBackground,
                                RoundedCornerShape(16.dp)
                            ),
                    ) {
                        trainer.photoUrl?.let { _ ->
                            AsyncImage(
                                modifier = Modifier.clip(CircleShape),
                                model = trainer.photoUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        } ?: Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(360.dp)
                                .padding(top = 56.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            painter = painterResource(id = R.drawable.ic_profile_man),
                            contentDescription = null
                        )
                    }
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = "${trainer.firstName} ${trainer.lastName}",
                        color = FitLadyaTheme.colors.text,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = trainer.roles.firstOrNull()?.name
                            ?: stringResource(id = R.string.trainer),
                        color = FitLadyaTheme.colors.text.copy(alpha = 0.64f),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row {
                        Column(
                            modifier = Modifier.padding(start = 32.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.age),
                                color = FitLadyaTheme.colors.text.copy(alpha = 0.64f),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(id = R.string.sex),
                                color = FitLadyaTheme.colors.text.copy(alpha = 0.64f),
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(id = R.string.experience),
                                color = FitLadyaTheme.colors.text.copy(alpha = 0.64f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            modifier = Modifier.padding(start = 32.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = getAgeString(trainer.age),
                                color = FitLadyaTheme.colors.text,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.End
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = availableSex[trainer.sex - 1],
                                color = FitLadyaTheme.colors.text,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.End
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = getAgeString(trainer.experience),
                                color = FitLadyaTheme.colors.text,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "“", fontSize = 32.sp, color = FitLadyaTheme.colors.accent)
                        Spacer(modifier = Modifier.width(10.dp))
                        trainer.quote?.let { quote ->
                            Text(
                                text = quote,
                                color = FitLadyaTheme.colors.text,
                                modifier = Modifier
                                    .drawBehind {
                                        val strokeWidthPx = 2.dp.toPx()
                                        val verticalOffset = size.height
                                        drawLine(
                                            color = underLineColor,
                                            strokeWidth = strokeWidthPx,
                                            start = Offset(0f, 0f),
                                            end = Offset(0f, verticalOffset)
                                        )
                                    }
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "trainer",
                                trainer.toChatCard()
                            )
                            navController.navigate(Screen.ClientChat.route)
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.to_text_trainer),
                            color = FitLadyaTheme.colors.secondaryText
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = stringResource(id = R.string.specializations),
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    trainer.specializations.forEach { specialization ->
                        Row(
                            modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_point_1),
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = specialization.name,
                                color = FitLadyaTheme.colors.text,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = stringResource(id = R.string.services),
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    trainer.services.forEach { service ->
                        Row(
                            modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_point_1),
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.accent
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier.widthIn(max = 250.dp),
                                text = service.name,
                                color = FitLadyaTheme.colors.text,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Box(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = service.price.toString(),
                                    color = FitLadyaTheme.colors.accent,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = stringResource(id = R.string.sport_achievements),
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    trainer.achievements.forEach { achievement ->
                        Row(
                            modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_point_1),
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = achievement.name,
                                color = FitLadyaTheme.colors.text,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp),
                        text = stringResource(id = R.string.trainer_schedule),
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 32.dp)
                    ) {
                        Text(
                            text = "пн - вт",
                            color = FitLadyaTheme.colors.text
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "8:00 - 19:00",
                            color = FitLadyaTheme.colors.accent,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}