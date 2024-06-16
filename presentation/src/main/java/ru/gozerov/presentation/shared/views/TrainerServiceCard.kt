import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.domain.models.CustomService
import ru.gozerov.domain.models.ScheduleService
import ru.gozerov.domain.utils.getAgeString
import ru.gozerov.domain.utils.parseDateToDDMMYYYY
import ru.gozerov.domain.utils.parseDateToHoursAndMinutes
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.views.UserAvatar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun TrainerServiceCard(
    service: CustomService,
    isTrainerApproved: Boolean?,
    onClick: (isPositive: Boolean) -> Unit,
    isEditable: Boolean = true,
    onPlan: () -> Unit,
    onBottomAction: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .background(
                    color = if (service.isPayed && isTrainerApproved == true && service.isClientApproved == true)
                        FitLadyaTheme.colors.secondary.copy(0.48f) else FitLadyaTheme.colors.secondary,
                    shape = RoundedCornerShape(16.dp)
                )
                .alpha(if (service.isPayed && isTrainerApproved == true && service.isClientApproved == true) 0.48f else 1f)
                .padding(all = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAvatar(
                    size = 48.dp,
                    photo = service.user.photoUrl,
                    background = FitLadyaTheme.colors.primaryBackground,
                    padding = 4.dp
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "${service.user.firstName} ${service.user.lastName}",
                        color = FitLadyaTheme.colors.text,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium

                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getAgeString(service.user.age),
                        color = FitLadyaTheme.colors.text.copy(alpha = 0.36f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                if ((isTrainerApproved != true || service.isClientApproved != true) && service.isPayed) {
                    ServiceChipItem(
                        text = stringResource(id = if (service.isClientApproved == true) R.string.executed else R.string.not_executed),
                        color = if (service.isClientApproved == true) FitLadyaTheme.colors.successColor else FitLadyaTheme.colors.accent,
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    ServiceChipItem(
                        text = stringResource(id = R.string.payed),
                        color = FitLadyaTheme.colors.successColor
                    )
                } else if (!service.isPayed) {
                    ServiceChipItem(
                        text = stringResource(id = R.string.not_payed),
                        color = FitLadyaTheme.colors.accent
                    )
                }
                if (isTrainerApproved == true && service.isClientApproved == true) {
                    ServiceChipItem(
                        text = stringResource(id = R.string.service_executed),
                        color = FitLadyaTheme.colors.successColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .background(
                        color = FitLadyaTheme.colors.primaryBackground,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
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
                    if (service.service.profile_access && service.isPayed && (service.isTrainerApproved != true || service.isClientApproved != true)) {
                        Box(contentAlignment = Alignment.CenterEnd) {
                            IconButton(
                                onClick = {
                                    onPlan()
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(32.dp),
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = FitLadyaTheme.colors.accent
                                )
                            }
                        }
                    }
                }
            }
            if ((isTrainerApproved != true || service.isClientApproved != true) && service.isPayed && isEditable) {
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        enabled = isTrainerApproved != false || service.isClientApproved != true,
                        border = BorderStroke(
                            1.dp,
                            if (isTrainerApproved != false) FitLadyaTheme.colors.primary
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
                            text = stringResource(R.string.not_executed),
                            fontWeight = FontWeight.Medium,
                            color = if (isTrainerApproved != false) FitLadyaTheme.colors.buttonText
                            else FitLadyaTheme.colors.buttonText.copy(alpha = 0.32f),
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        enabled = (service.isClientApproved != true && isTrainerApproved != true) || isTrainerApproved != true,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FitLadyaTheme.colors.primary,
                            disabledContainerColor = FitLadyaTheme.colors.primary.copy(alpha = 0.32f)
                        ),
                        onClick = {
                            onClick(true)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.executed),
                            fontWeight = FontWeight.Medium,
                            color = if (isTrainerApproved != true) FitLadyaTheme.colors.secondaryText
                            else FitLadyaTheme.colors.secondaryText.copy(alpha = 0.32f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
            if (isEditable && (!service.isPayed && service.isTrainerApproved != true && service.isClientApproved != true)) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(id = R.string.remove),
                        color = FitLadyaTheme.colors.accent,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            onBottomAction()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceChipItem(text: String, color: Color) {
    Text(
        modifier = Modifier
            .border(1.dp, color, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = text,
        color = FitLadyaTheme.colors.text,
        fontSize = 12.sp
    )
}



@Composable
fun TrainerServiceCard(
    service: ScheduleService,
    isTrainerApproved: Boolean?,
    onClick: (isPositive: Boolean) -> Unit,
    isEditable: Boolean = true,
    onPlan: () -> Unit,
    onBottomAction: () -> Unit
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
        if (service.isPayed && service.isTrainerApproved == true && service.isClientApproved == true) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FitLadyaTheme.colors.secondary.copy(alpha = 0.48f))
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(
                size = 48.dp,
                photo = service.user.photoUrl,
                background = FitLadyaTheme.colors.primaryBackground,
                padding = 4.dp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${service.user.firstName} ${service.user.lastName}",
                    color = FitLadyaTheme.colors.text,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium

                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = getAgeString(service.user.age),
                    color = FitLadyaTheme.colors.text.copy(alpha = 0.36f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            if ((isTrainerApproved != true || service.isClientApproved != true) && service.isPayed) {
                ServiceChipItem(
                    text = stringResource(id = if (service.isTrainerApproved == true) R.string.executed else R.string.not_executed),
                    color = if (service.isTrainerApproved == true) FitLadyaTheme.colors.successColor else FitLadyaTheme.colors.accent,
                )

                Spacer(modifier = Modifier.width(8.dp))
                ServiceChipItem(
                    text = stringResource(id = R.string.payed),
                    color = FitLadyaTheme.colors.successColor
                )
            } else if (!service.isPayed) {
                ServiceChipItem(
                    text = stringResource(id = R.string.not_payed),
                    color = FitLadyaTheme.colors.accent
                )
            }
            if (isTrainerApproved == true && service.isClientApproved == true) {
                ServiceChipItem(
                    text = stringResource(id = R.string.service_executed),
                    color = FitLadyaTheme.colors.successColor
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            ServiceChipItem(
                text = parseDateToDDMMYYYY(service.date),
                color = FitLadyaTheme.colors.primaryBorder
            )
            Spacer(modifier = Modifier.width(8.dp))
            ServiceChipItem(
                text = parseDateToHoursAndMinutes(service.date, service.timeStart),
                color = FitLadyaTheme.colors.primaryBorder
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .background(
                    color = FitLadyaTheme.colors.primaryBackground,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
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
                if (service.service.profile_access && service.isPayed && (service.isTrainerApproved != true || service.isClientApproved != true)) {
                    Box(contentAlignment = Alignment.CenterEnd) {
                        IconButton(
                            onClick = {
                                onPlan()
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(32.dp),
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.accent
                            )
                        }
                    }
                }
            }
        }
        if ((isTrainerApproved != true || service.isTrainerApproved != true) && service.isPayed && isEditable) {
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    enabled = isTrainerApproved != false || service.isClientApproved != true,
                    border = BorderStroke(
                        1.dp,
                        if (isTrainerApproved != false) FitLadyaTheme.colors.primary
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
                        text = stringResource(R.string.not_executed),
                        fontWeight = FontWeight.Medium,
                        color = if (isTrainerApproved != false) FitLadyaTheme.colors.buttonText
                        else FitLadyaTheme.colors.buttonText.copy(alpha = 0.32f),
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    enabled = (service.isClientApproved != true && isTrainerApproved != true) || isTrainerApproved != true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FitLadyaTheme.colors.primary,
                        disabledContainerColor = FitLadyaTheme.colors.primary.copy(alpha = 0.32f)
                    ),
                    onClick = {
                        onClick(true)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.executed),
                        fontWeight = FontWeight.Medium,
                        color = if (isTrainerApproved != true) FitLadyaTheme.colors.secondaryText
                        else FitLadyaTheme.colors.secondaryText.copy(alpha = 0.32f),
                        fontSize = 12.sp
                    )
                }
            }
        }
        if (isEditable) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.cancel_service),
                    color = FitLadyaTheme.colors.accent,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        onBottomAction()
                    }
                )
            }
        }
    }
}