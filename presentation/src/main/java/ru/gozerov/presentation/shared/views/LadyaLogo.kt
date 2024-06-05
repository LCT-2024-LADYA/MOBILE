package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun ColumnScope.LadyaLogo() {
    val ddxLogo =
        painterResource(id = if (isSystemInDarkTheme()) R.drawable.ic_ddx_logo_dark else R.drawable.ic_ddx_logo)

    Image(
        modifier = Modifier.width(240.dp),
        painter = ddxLogo,
        contentDescription = null
    )
    Image(
        modifier = Modifier.padding(vertical = 20.dp),
        painter = painterResource(id = R.drawable.ic_clear_logo),
        contentDescription = null
    )
    Row {
        Icon(
            modifier = Modifier.padding(end = 16.dp, top = 4.dp),
            painter = painterResource(id = R.drawable.ic_chess_rook),
            tint = FitLadyaTheme.colors.text,
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.ladyafit),
            fontSize = 36.sp,
            color = FitLadyaTheme.colors.text,
            fontWeight = FontWeight.W200,
            fontFamily = FontFamily(Font(R.font.russo_one)),
        )

    }
}