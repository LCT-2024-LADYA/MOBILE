package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun ColumnScope.Footer(modifier: Modifier = Modifier) {
    val alpha = if (isSystemInDarkTheme()) 0.72f else 0.4f
    Box(
        modifier = Modifier.weight(1f),
        contentAlignment = Alignment.BottomCenter
    ) {
        val footerText = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = FitLadyaTheme.colors.primary.copy(alpha = alpha),
                    fontWeight = FontWeight.Medium,

                    )
            ) {
                append(stringResource(id = R.string.made_by))
            }
            withStyle(
                style = SpanStyle(
                    color = FitLadyaTheme.colors.primary.copy(alpha),
                    fontFamily = FontFamily(Font(R.font.russo_one)),
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(stringResource(id = R.string.mirea_service_ladya))
            }
        }

        Text(
            text = footerText,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}