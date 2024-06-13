package ru.gozerov.presentation.ui.theme

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FitLadyaTheme(
    textSize: FitLadyaSize = FitLadyaSize.Medium,
    paddingSize: FitLadyaSize = FitLadyaSize.Medium,
    corners: FitLadyaCorners = FitLadyaCorners.Rounded,
    darkTheme: Boolean = isNightMode(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) baseDarkPalette else baseLightPalette

    val typography = FitLadyaTypography(
        heading = TextStyle(
            fontSize = when (textSize) {
                FitLadyaSize.Small -> 20.sp
                FitLadyaSize.Medium -> 24.sp
                FitLadyaSize.Big -> 28.sp
            },
            fontWeight = FontWeight.Bold
        ),
        body = TextStyle(
            fontSize = when (textSize) {
                FitLadyaSize.Small -> 14.sp
                FitLadyaSize.Medium -> 16.sp
                FitLadyaSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Normal
        ),
        toolbar = TextStyle(
            fontSize = when (textSize) {
                FitLadyaSize.Small -> 14.sp
                FitLadyaSize.Medium -> 16.sp
                FitLadyaSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Medium
        ),
        caption = TextStyle(
            fontSize = when (textSize) {
                FitLadyaSize.Small -> 10.sp
                FitLadyaSize.Medium -> 12.sp
                FitLadyaSize.Big -> 14.sp
            }
        )
    )

    val shapes = FitLadyaShape(
        padding = when (paddingSize) {
            FitLadyaSize.Small -> 12.dp
            FitLadyaSize.Medium -> 16.dp
            FitLadyaSize.Big -> 20.dp
        },
        cornersStyle = when (corners) {
            FitLadyaCorners.Flat -> RoundedCornerShape(0.dp)
            FitLadyaCorners.Rounded -> RoundedCornerShape(8.dp)
        }
    )

    CompositionLocalProvider(
        LocalFitLadyaColors provides colors,
        LocalFitLadyaTypography provides typography,
        LocalFitLadyaShape provides shapes,
        content = content
    )
}

@Composable
fun isNightMode(): Boolean {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("ladyaPrefs", Context.MODE_PRIVATE)
    val isDarkTheme = if (prefs.contains("theme")) {
        prefs.getBoolean("theme", false)
    } else isSystemInDarkTheme()
    return isDarkTheme
}

fun setTheme(context: Context, isDark: Boolean) {
    val prefs = context.getSharedPreferences("ladyaPrefs", Context.MODE_PRIVATE)
    prefs
        .edit()
        .putBoolean("theme", isDark)
        .apply()
    AppCompatDelegate.setDefaultNightMode(if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
}