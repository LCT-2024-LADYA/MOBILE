package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.domain.models.ClientInfo
import ru.gozerov.domain.models.PayedService
import ru.gozerov.domain.utils.getAgeString
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun ClientServiceCard(
    client: ClientInfo, service: PayedService,
    isTrainerApproved: Boolean?,
    isPayed: Boolean,
    onClick: (isPositive: Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = FitLadyaTheme.colors.secondary, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(
                size = 48.dp,
                photo = client.photoUrl,
                background = FitLadyaTheme.colors.primaryBackground,
                padding = 4.dp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${client.firstName} ${client.lastName}",
                    color = FitLadyaTheme.colors.text,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium

                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = getAgeString(client.age),
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
                        if (isPayed && (isTrainerApproved != true || service.isClientApproved != true)) {
                            Text(
                                text = stringResource(id = if (service.isClientApproved == true) R.string.executed else R.string.not_executed),
                                color = if (service.isClientApproved == true) FitLadyaTheme.colors.successColor else FitLadyaTheme.colors.accent,
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
            if (isTrainerApproved == true && service.isClientApproved == true && isPayed) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.service_executed),
                    color = FitLadyaTheme.colors.successColor,
                    fontSize = 12.sp
                )
            }
        }
        if (isTrainerApproved != true || service.isClientApproved != true || !isPayed) {
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    enabled = isTrainerApproved == true || !isPayed || isTrainerApproved == null,
                    border = BorderStroke(
                        1.dp,
                        if (isTrainerApproved == true || !isPayed || isTrainerApproved == null) FitLadyaTheme.colors.primary
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
                        color = if (isTrainerApproved == true || !isPayed || isTrainerApproved == null) FitLadyaTheme.colors.buttonText
                        else FitLadyaTheme.colors.buttonText.copy(alpha = 0.32f),
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    enabled = isTrainerApproved == false || !isPayed || isTrainerApproved == null,
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
                        color = if (isTrainerApproved == false || !isPayed || isTrainerApproved == null) FitLadyaTheme.colors.secondaryText
                        else FitLadyaTheme.colors.secondaryText.copy(alpha = 0.32f),
                        fontSize = 14.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.cancel_service),
            fontWeight = FontWeight.Medium,
            color = FitLadyaTheme.colors.accent
        )
    }
}