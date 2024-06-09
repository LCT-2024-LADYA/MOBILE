package ru.gozerov.presentation.screens.trainee.chat.list.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.TrainerInfo
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.views.UserAvatar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@Composable
internal fun SimpleChatCard(chatCard: ChatCard) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = FitLadyaTheme.colors.primaryBackground)
            .padding(horizontal = 16.dp),
    ) {
        UserAvatar(size = 60.dp, photo = chatCard.userPhoto, padding = 8.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = chatCard.username,
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
        Spacer(modifier = Modifier.width(32.dp))
        Column {
            Text(
                text = chatCard.time,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.36f),
            )
            Box(
                modifier = Modifier.background(
                    color = FitLadyaTheme.colors.primary,
                    shape = CircleShape
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chatCard.unreadCount.toString(),
                    fontWeight = FontWeight.Medium,
                    color = FitLadyaTheme.colors.primaryBackground,
                )
            }
        }
    }
}

@Composable
internal fun SimpleChatTrainerCard(trainer: TrainerInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = FitLadyaTheme.colors.primaryBackground)
            .padding(horizontal = 16.dp),
    ) {
        UserAvatar(size = 60.dp, photo = trainer.photoUrl, padding = 8.dp)
        Spacer(modifier = Modifier.width(12.dp))
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
                text = trainer.roles.firstOrNull()?.name ?: stringResource(id = R.string.trainer),
                color = FitLadyaTheme.colors.text.copy(alpha = 0.36f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(48.dp))

        Box(
            modifier = Modifier.background(
                color = FitLadyaTheme.colors.primary,
                shape = CircleShape
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.experience_is, trainer.experience),
                fontWeight = FontWeight.Medium,
                color = FitLadyaTheme.colors.accent,
            )
        }

    }
}