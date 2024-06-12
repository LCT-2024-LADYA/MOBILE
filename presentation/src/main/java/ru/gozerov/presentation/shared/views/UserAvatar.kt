package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun UserAvatar(
    size: Dp,
    photo: Any?,
    padding: Dp = 16.dp,
    background: Color = FitLadyaTheme.colors.secondary
) {
    Box(
        modifier = Modifier
            .size(size, size)
            .background(background, CircleShape),
    ) {
        photo?.let { _ ->
            AsyncImage(
                modifier = Modifier.clip(CircleShape),
                model = photo,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } ?: Image(
            modifier = Modifier
                .size(size, size)
                .clip(CircleShape)
                .padding(top = padding)
                .clip(CircleShape),
            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.ic_person_dark else R.drawable.ic_profile_man),
            contentDescription = null
        )
    }
}