package ru.gozerov.presentation.shared.views


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
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
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
fun ColumnScope.Footer(weightNeeded: Boolean = true) {

    val showEasterEgg = remember { mutableStateOf(false) }
    val isButtonClickable = remember { mutableStateOf(true) }
    val clickCount = remember { mutableIntStateOf(0) }

    if (clickCount.intValue == 15) {
        showEasterEgg.value = true
        isButtonClickable.value = false
        FlyOutIconAnimation(showEasterEgg, clickCount, isButtonClickable)
    }

    val alpha = if (isSystemInDarkTheme()) 0.72f else 0.4f
    Box(
        modifier = if (weightNeeded) Modifier.weight(1f) else Modifier,
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
            modifier = if (isButtonClickable.value) Modifier.clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) { clickCount.intValue += 1 } else Modifier)
    }
}

@Composable
fun FlyOutIconAnimation(
    showAnimation: MutableState<Boolean>,
    clickCount: MutableState<Int>,
    isButtonClickable: MutableState<Boolean>
) {
    (0..20).forEach { _ ->
        val scaleAnimatable = remember { Animatable(3f) }
        val offsetAnimatable =
            remember { Animatable(Random.nextInt(-1000..1000).toFloat()) }

        LaunchedEffect(Unit) {
            val animationSpec: AnimationSpec<Float> = TweenSpec(durationMillis = 2000)
            offsetAnimatable.animateTo(
                targetValue = Random.nextInt(-1000..1000).toFloat(),
                animationSpec = animationSpec
            )
            scaleAnimatable.animateTo(
                targetValue = 100f,
                animationSpec = animationSpec
            )
            showAnimation.value = false
            clickCount.value = 0
            isButtonClickable.value = true
        }

        Image(
            painter = painterResource(id = R.drawable.butsa),
            contentDescription = "Favorite",
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scaleAnimatable.value,
                    scaleY = scaleAnimatable.value,
                    translationY = offsetAnimatable.value,
                    translationX = offsetAnimatable.value
                )
                .size(24.dp)
        )

    }
}
