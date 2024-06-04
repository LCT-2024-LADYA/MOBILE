package ru.gozerov.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.presentation.R

@Composable
fun FitLadyaTheme(
    textSize: FitLadyaSize = FitLadyaSize.Medium,
    paddingSize: FitLadyaSize = FitLadyaSize.Medium,
    corners: FitLadyaCorners = FitLadyaCorners.Rounded,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) baseDarkPalette else baseLightPalette

    val mPlusFontFamily = FontFamily(
        Font(R.font.mplus)
    )

    val typography = FitLadyaTypography(
        heading = TextStyle(
            fontSize = when (textSize) {
                FitLadyaSize.Small -> 20.sp
                FitLadyaSize.Medium -> 24.sp
                FitLadyaSize.Big -> 28.sp
            },
            fontWeight = FontWeight.Bold,
            fontFamily = mPlusFontFamily
        ),
        body = TextStyle(
            fontSize = when (textSize) {
                FitLadyaSize.Small -> 14.sp
                FitLadyaSize.Medium -> 16.sp
                FitLadyaSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Normal,
            fontFamily = mPlusFontFamily
        ),
        toolbar = TextStyle(
            fontSize = when (textSize) {
                FitLadyaSize.Small -> 14.sp
                FitLadyaSize.Medium -> 16.sp
                FitLadyaSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Medium,
            fontFamily = mPlusFontFamily
        ),
        caption = TextStyle(
            fontSize = when (textSize) {
                FitLadyaSize.Small -> 10.sp
                FitLadyaSize.Medium -> 12.sp
                FitLadyaSize.Big -> 14.sp
            },
            fontFamily = mPlusFontFamily
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