package ru.gozerov.presentation.screens.trainee.chat.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.gozerov.domain.models.TrainerInfo
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun ChatScreen(navController: NavController, trainer: TrainerInfo) {

    val snackbarHostState = remember { SnackbarHostState() }

    val messages = (0..3).map { "Я уже месяц поддерживаю свой режим питания" }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.secondary
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row {
                IconButton(
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

                Column {
                    Text(
                        text = "${trainer.firstName} ${trainer.lastName}",
                        color = FitLadyaTheme.colors.text,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium

                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.online),
                        color = FitLadyaTheme.colors.primary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(color = FitLadyaTheme.colors.primaryBackground)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(messages.size) {

                    }
                }
            }

        }
    }
}

@Composable
fun MeMessageCard(text: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .background(color = FitLadyaTheme.colors.primary, shape = RoundedCornerShape(8.dp))
        ) {
            Text(
                text = text,
                color = FitLadyaTheme.colors.primaryBackground,
                fontSize = 16.sp
            )
            Row {
                Text(
                    text = "14:48",
                    color = FitLadyaTheme.colors.primaryBackground.copy(alpha = 0.36f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_read_message),
                    contentDescription = null,
                    tint = FitLadyaTheme.colors.accent
                )
            }
        }
    }
}

@Composable
fun UserMessageCard(text: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .background(
                    color = FitLadyaTheme.colors.secondary,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                text = text,
                color = FitLadyaTheme.colors.primaryBackground,
                fontSize = 16.sp
            )
            Text(
                text = "14:48",
                color = FitLadyaTheme.colors.primaryBackground.copy(alpha = 0.36f)
            )
        }
    }
}