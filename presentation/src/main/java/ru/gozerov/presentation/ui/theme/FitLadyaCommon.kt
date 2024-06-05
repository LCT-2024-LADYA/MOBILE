package ru.gozerov.presentation.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class FitLadyaColors(
    val text: Color,
    val primary: Color,
    val primaryBackground: Color,
    val secondaryBackground: Color,
    val secondary: Color,
    val accent: Color,
    val error: Color,
    val errorText: Color,
    val fieldPrimaryText: Color,
    val secondaryText: Color,
    val buttonText: Color,
    val primaryBorder: Color,
    val card0: Color,
    val card1: Color,
    val card2: Color,
    val card3: Color
)

data class FitLadyaTypography(
    val heading: TextStyle,
    val body: TextStyle,
    val toolbar: TextStyle,
    val caption: TextStyle
)

data class FitLadyaShape(
    val padding: Dp,
    val cornersStyle: Shape
)

object FitLadyaTheme {
    val colors: FitLadyaColors
        @Composable
        get() = LocalFitLadyaColors.current

    val typography: FitLadyaTypography
        @Composable
        get() = LocalFitLadyaTypography.current

    val shapes: FitLadyaShape
        @Composable
        get() = LocalFitLadyaShape.current
}

enum class FitLadyaSize {
    Small, Medium, Big
}

enum class FitLadyaCorners {
    Flat, Rounded
}

val LocalFitLadyaColors = staticCompositionLocalOf<FitLadyaColors> {
    error("No colors provided")
}

val LocalFitLadyaTypography = staticCompositionLocalOf<FitLadyaTypography> {
    error("No font provided")
}

val LocalFitLadyaShape = staticCompositionLocalOf<FitLadyaShape> {
    error("No shapes provided")
}