package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun ProfileToolbar(onNavUpClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterStart),
            onClick = onNavUpClick
        ) {
            Icon(
                modifier = Modifier
                    .size(48.dp),
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                contentDescription = null,
                tint = FitLadyaTheme.colors.text
            )
        }
        Text(
            text = stringResource(id = R.string.profile),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = FitLadyaTheme.colors.text,
            modifier = Modifier.align(Alignment.Center)
        )
    }

}