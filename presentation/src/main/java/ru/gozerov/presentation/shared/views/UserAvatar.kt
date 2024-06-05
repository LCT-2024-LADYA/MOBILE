package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun UserAvatar(size: Dp, padding: Dp = 16.dp) {
    Box(
        modifier = Modifier
            .size(size, size)
            .background(FitLadyaTheme.colors.secondary, CircleShape),
    ) {
        Image(
            modifier = Modifier
                .size(size, size)
                .clip(CircleShape)
                .padding(top = padding)
                .clip(CircleShape),
            painter = painterResource(id = R.drawable.ic_profile_man),
            contentDescription = null
        )
    }
}