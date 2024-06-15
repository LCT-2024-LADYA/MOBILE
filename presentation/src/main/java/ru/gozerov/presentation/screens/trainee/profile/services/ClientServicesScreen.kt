package ru.gozerov.presentation.screens.trainee.profile.services

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.gozerov.domain.models.PayedService
import ru.gozerov.domain.models.TrainerCard
import ru.gozerov.domain.models.TrainerService
import ru.gozerov.domain.models.UserCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.screens.trainee.profile.services.views.ConfirmChangesDialog
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.shared.views.UserAvatar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClientServicesScreen(navController: NavController) {

    val snackbarHostState = remember { SnackbarHostState() }

    val trainer = TrainerCard(0, null, "name", "name", emptyList(), 100, 1, null, emptyList(), 10)
    val service = PayedService(0, TrainerService(0, "aaaa", 1000, true), false, null, null, 0, 0, UserCard(0, null, "aaa", "aaaa", 1, 100))
    val isClientApproved = remember { mutableStateOf(service.isClientApproved) }
    var isPayed: Boolean by remember { mutableStateOf(service.isPayed) }

    var showDialog: Boolean by remember { mutableStateOf(false) }
    var onPositiveClicked: Boolean by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        if (showDialog) {
            ConfirmChangesDialog(
                isPositive = onPositiveClicked,
                onDismiss = {
                    showDialog = false
                },
                onConfirm = { isPositive ->
                    if (isPositive) {
                        if (!isPayed) {
                            isPayed = true
                        } else {
                            isClientApproved.value = true
                        }
                    } else {
                        if (!isPayed) {

                        } else {
                            isClientApproved.value = false
                        }
                    }
                    showDialog = false
                }
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavUpWithTitleToolbar(
                navController = navController,
                title = stringResource(id = R.string.services)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    ServiceCard(
                        trainer = trainer,
                        service = service,
                        isClientApproved = isClientApproved.value,
                        isPayed = isPayed,
                        onClick = { isPositive ->
                            onPositiveClicked = isPositive
                            showDialog = true
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun ServiceCard(
    trainer: TrainerCard,
    service: PayedService,
    isClientApproved: Boolean?,
    isPayed: Boolean,
    onClick: (isPositive: Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(
                color = FitLadyaTheme.colors.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(all = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(
                size = 48.dp,
                photo = trainer.photoUrl,
                background = FitLadyaTheme.colors.primaryBackground,
                padding = 4.dp
            )
            Spacer(modifier = Modifier.width(16.dp))
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
                    text = trainer.roles.firstOrNull()?.name
                        ?: stringResource(id = R.string.trainer),
                    color = FitLadyaTheme.colors.text.copy(alpha = 0.36f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .background(
                    color = FitLadyaTheme.colors.primaryBackground,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = service.service.name,
                        color = FitLadyaTheme.colors.text
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.price_is, service.service.price),
                        color = FitLadyaTheme.colors.text.copy(alpha = 0.48f),
                        fontSize = 12.sp
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Column {
                        if (isPayed && (isClientApproved != true || service.isTrainerApproved != true)) {
                            Text(
                                text = stringResource(id = if (service.isTrainerApproved == true) R.string.executed else R.string.not_executed),
                                color = if (service.isTrainerApproved == true) FitLadyaTheme.colors.successColor else FitLadyaTheme.colors.accent,
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(id = R.string.payed),
                                color = FitLadyaTheme.colors.successColor,
                                fontSize = 12.sp
                            )
                        } else if (!isPayed) {
                            Text(
                                text = stringResource(id = R.string.not_payed),
                                color = FitLadyaTheme.colors.accent,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
            if (isClientApproved == true && service.isTrainerApproved == true && isPayed) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.service_executed),
                    color = FitLadyaTheme.colors.successColor,
                    fontSize = 12.sp
                )
            }
        }
        if (isClientApproved != true || service.isTrainerApproved != true || !isPayed) {
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    enabled = isClientApproved == true || !isPayed || isClientApproved == null,
                    border = BorderStroke(
                        1.dp,
                        if (isClientApproved == true || !isPayed || isClientApproved == null) FitLadyaTheme.colors.primary
                        else FitLadyaTheme.colors.primary.copy(alpha = 0.32f)
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FitLadyaTheme.colors.secondary,
                        disabledContainerColor = FitLadyaTheme.colors.secondary
                    ),
                    onClick = {
                        onClick(false)
                    }
                ) {
                    Text(
                        text = stringResource(if (isPayed) R.string.not_executed else R.string.cancel),
                        fontWeight = FontWeight.Medium,
                        color = if (isClientApproved == true || !isPayed || isClientApproved == null) FitLadyaTheme.colors.buttonText
                        else FitLadyaTheme.colors.buttonText.copy(alpha = 0.32f),
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    enabled = isClientApproved == false || !isPayed || isClientApproved == null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FitLadyaTheme.colors.primary,
                        disabledContainerColor = FitLadyaTheme.colors.primary.copy(alpha = 0.32f)
                    ),
                    onClick = {
                        onClick(true)
                    }
                ) {
                    Text(
                        text = stringResource(id = if (isPayed) R.string.executed else R.string.pay),
                        fontWeight = FontWeight.Medium,
                        color = if (isClientApproved == false || !isPayed || isClientApproved == null) FitLadyaTheme.colors.secondaryText
                        else FitLadyaTheme.colors.secondaryText.copy(alpha = 0.32f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}