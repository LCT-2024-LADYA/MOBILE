package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@Composable
fun SearchTextField(
    textState: MutableState<String>,
    onValueChange: ((String) -> Unit)? = null,
    placeholderText: String,
    containerColor: Color
) {
    OutlinedTextField(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .clip(CircleShape)
            .fillMaxWidth(),
        value = textState.value,
        onValueChange = onValueChange ?: {
            textState.value = it
        },
        singleLine = true,
        placeholder = {
            Text(
                text = placeholderText,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.64f),
                fontSize = 16.sp
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = FitLadyaTheme.colors.text
            )
        },
        colors = TextFieldDefaults.colors(
            focusedLabelColor = FitLadyaTheme.colors.text,
            unfocusedLabelColor = FitLadyaTheme.colors.text,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor = containerColor,
            unfocusedIndicatorColor = containerColor,
            cursorColor = FitLadyaTheme.colors.fieldPrimaryText,
            focusedTextColor = FitLadyaTheme.colors.text,
            unfocusedTextColor = FitLadyaTheme.colors.text,
            disabledContainerColor = containerColor,
            disabledLabelColor = FitLadyaTheme.colors.text,
            disabledIndicatorColor = containerColor,
            disabledTextColor = FitLadyaTheme.colors.text,
        )
    )
}

class TimeVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 5) text.text.substring(0, 5) else text.text
        val annotatedString = AnnotatedString.Builder().apply {
            append(trimmed.padEnd(5, '_'))
        }.toAnnotatedString()

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 4) return offset + 1
                return 5
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 4) return offset - 1
                return 4
            }
        }

        return TransformedText(annotatedString, offsetMapping)
    }
}