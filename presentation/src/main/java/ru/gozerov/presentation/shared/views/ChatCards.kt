package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.TrainerCard
import ru.gozerov.domain.models.UserCard
import ru.gozerov.domain.utils.getAgeString
import ru.gozerov.domain.utils.parseDateToHoursAndMinutes
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@Composable
internal fun SimpleChatCard(chatCard: ChatCard, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .background(color = FitLadyaTheme.colors.primaryBackground)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {

            UserAvatar(size = 60.dp, photo = chatCard.photoUrl, padding = 6.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .width(240.dp)
            ) {
                Text(
                    text = "${chatCard.firstName} ${chatCard.lastName}",
                    color = FitLadyaTheme.colors.text,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium

                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = chatCard.lastMessage,
                    color = FitLadyaTheme.colors.text.copy(alpha = 0.36f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }

        Column(
            modifier = Modifier
                .width(72.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = parseDateToHoursAndMinutes(chatCard.timeLastMessage),
                color = FitLadyaTheme.colors.text.copy(alpha = 0.36f),
            )
        }
    }
}

@Composable
internal fun SimpleChatTrainerCard(
    trainer: TrainerCard,
    sex: String,
    onProfileClick: () -> Unit,
    onTextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(color = FitLadyaTheme.colors.secondary, shape = RoundedCornerShape(16.dp))
            .padding(all = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            UserAvatar(
                size = 128.dp,
                photo = trainer.photoUrl,
                background = FitLadyaTheme.colors.primaryBackground,
                padding = 8.dp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Spacer(modifier = Modifier.height(8.dp))
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
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Column {
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
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                        Column(
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
                                text = sex,
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
                }
            }

        }
        Spacer(modifier = Modifier.height(20.dp))

        trainer.specializations.forEach { specialization ->
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
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

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
            onClick = {
                onProfileClick()
            }
        ) {
            Text(
                text = stringResource(id = R.string.see_profile),
                color = FitLadyaTheme.colors.secondaryText
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            border = BorderStroke(1.dp, FitLadyaTheme.colors.primary),
            colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.secondary),
            onClick = {
                onTextClick()
            }
        ) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                style = FitLadyaTheme.typography.body,
                text = stringResource(R.string.to_text_trainer),
                fontWeight = FontWeight.Medium,
                color = FitLadyaTheme.colors.buttonText,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
internal fun SimpleChatUserCard(user: UserCard, onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = FitLadyaTheme.colors.primaryBackground)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onProfileClick()
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserAvatar(size = 60.dp, photo = user.photoUrl, padding = 8.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "${user.firstName} ${user.lastName}",
                color = FitLadyaTheme.colors.text,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium

            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = getAgeString(user.age),
                color = FitLadyaTheme.colors.text.copy(alpha = 0.36f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}