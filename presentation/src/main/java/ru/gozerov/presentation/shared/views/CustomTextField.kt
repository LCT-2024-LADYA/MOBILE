package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    labelText: String,
    textState: MutableState<String>,
    onValueChange: ((String) -> Unit)? = null,
    isEnabled: Boolean = true,
    isIndicatorEnabled: Boolean = true,
    isError: Boolean = false,
    isRounded: Boolean = true,
    containerColor: Color = FitLadyaTheme.colors.secondary,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null
) {
    TextField(
        shape = RoundedCornerShape(
            topStart = if (isRounded) 4.dp else 0.dp,
            topEnd = if (isRounded) 4.dp else 0.dp
        ),
        isError = isError,
        enabled = isEnabled,
        label = { Text(text = labelText) },
        value = textState.value,
        onValueChange = onValueChange ?: { textState.value = it },
        modifier = modifier,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            unfocusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            focusedTextColor = FitLadyaTheme.colors.text,
            focusedContainerColor = containerColor,
            unfocusedTextColor = FitLadyaTheme.colors.text,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor = if (isIndicatorEnabled) FitLadyaTheme.colors.primary else containerColor,
            unfocusedIndicatorColor = if (isIndicatorEnabled) FitLadyaTheme.colors.primary else containerColor,
            errorCursorColor = FitLadyaTheme.colors.errorText,
            errorLabelColor = FitLadyaTheme.colors.errorText,
            errorTextColor = FitLadyaTheme.colors.text,
            errorContainerColor = containerColor,
            errorSupportingTextColor = FitLadyaTheme.colors.errorText,
            errorIndicatorColor = FitLadyaTheme.colors.error,
            cursorColor = FitLadyaTheme.colors.primary,
            disabledTextColor = FitLadyaTheme.colors.text,
            disabledLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            disabledSupportingTextColor = FitLadyaTheme.colors.error,
            disabledContainerColor = containerColor,
            disabledIndicatorColor = if (isIndicatorEnabled) FitLadyaTheme.colors.primary else containerColor,
            disabledTrailingIconColor = FitLadyaTheme.colors.text
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        supportingText = supportingText
    )
}

@Composable
fun DateHHMMTextField(
    modifier: Modifier = Modifier,
    labelText: String,
    textState: MutableState<TextFieldValue>,
    isEnabled: Boolean = true,
    isIndicatorEnabled: Boolean = true,
    isError: Boolean = false,
    isRounded: Boolean = true,
    containerColor: Color = FitLadyaTheme.colors.secondary,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null
) {
    TextField(
        shape = RoundedCornerShape(
            topStart = if (isRounded) 4.dp else 0.dp,
            topEnd = if (isRounded) 4.dp else 0.dp
        ),
        isError = isError,
        enabled = isEnabled,
        label = { Text(text = labelText) },
        value = textState.value,
        onValueChange = { value ->

            val filteredText = value.text.filter { it.isDigit() }.let {
                when (it.length) {
                    0, 1, 2 -> it
                    3, 4 -> it.substring(0, 2) + ":" + it.substring(2)
                    else -> it.substring(0, 2) + ":" + it.substring(2, 4)
                }
            }
            textState.value = value.copy(
                text = filteredText,
                selection = TextRange(if (filteredText.length > 3) value.text.length + 1 else 5)
            )
        },
        modifier = modifier,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            unfocusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            focusedTextColor = FitLadyaTheme.colors.text,
            focusedContainerColor = containerColor,
            unfocusedTextColor = FitLadyaTheme.colors.text,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor = if (isIndicatorEnabled) FitLadyaTheme.colors.primary else containerColor,
            unfocusedIndicatorColor = if (isIndicatorEnabled) FitLadyaTheme.colors.primary else containerColor,
            errorCursorColor = FitLadyaTheme.colors.errorText,
            errorLabelColor = FitLadyaTheme.colors.errorText,
            errorTextColor = FitLadyaTheme.colors.text,
            errorContainerColor = containerColor,
            errorSupportingTextColor = FitLadyaTheme.colors.errorText,
            errorIndicatorColor = FitLadyaTheme.colors.error,
            cursorColor = FitLadyaTheme.colors.primary,
            disabledTextColor = FitLadyaTheme.colors.text,
            disabledLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            disabledSupportingTextColor = FitLadyaTheme.colors.error,
            disabledContainerColor = containerColor,
            disabledIndicatorColor = if (isIndicatorEnabled) FitLadyaTheme.colors.primary else containerColor,
            disabledTrailingIconColor = FitLadyaTheme.colors.text
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        supportingText = supportingText
    )
}

@Composable
fun DateDDMMYYYYTextField(
    modifier: Modifier = Modifier,
    labelText: String,
    textState: MutableState<TextFieldValue>,
    isEnabled: Boolean = true,
    isIndicatorEnabled: Boolean = true,
    isError: Boolean = false,
    isRounded: Boolean = true,
    containerColor: Color = FitLadyaTheme.colors.secondary,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null
) {
    TextField(
        shape = RoundedCornerShape(
            topStart = if (isRounded) 4.dp else 0.dp,
            topEnd = if (isRounded) 4.dp else 0.dp
        ),
        isError = isError,
        enabled = isEnabled,
        label = { Text(text = labelText) },
        value = textState.value,
        onValueChange = { value ->
            val filteredText = value.text.filter { it.isDigit() }.let {
                when (it.length) {
                    in 0..2 -> it
                    in 3..4 -> it.substring(0, 2) + "." + it.substring(2)
                    in 5..8 -> it.substring(0, 2) + "." + it.substring(2, 4) + "." + it.substring(4)
                    else -> it.substring(0, 2) + "." + it.substring(2, 4) + "." + it.substring(4, 8)
                }
            }

            val cursorPosition = when {
                value.text.length <= 2 -> value.text.length
                value.text.length <= 4 -> value.text.length + 1
                value.text.length <= 8 -> value.text.length + 2
                else -> 10
            }

            textState.value = value.copy(
                text = filteredText,
                selection = TextRange(cursorPosition)
            )
        },
        modifier = modifier,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            unfocusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            focusedTextColor = FitLadyaTheme.colors.text,
            focusedContainerColor = containerColor,
            unfocusedTextColor = FitLadyaTheme.colors.text,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor = if (isIndicatorEnabled) FitLadyaTheme.colors.primary else containerColor,
            unfocusedIndicatorColor = if (isIndicatorEnabled) FitLadyaTheme.colors.primary else containerColor,
            errorCursorColor = FitLadyaTheme.colors.errorText,
            errorLabelColor = FitLadyaTheme.colors.errorText,
            errorTextColor = FitLadyaTheme.colors.text,
            errorContainerColor = containerColor,
            errorSupportingTextColor = FitLadyaTheme.colors.errorText,
            errorIndicatorColor = FitLadyaTheme.colors.error,
            cursorColor = FitLadyaTheme.colors.primary,
            disabledTextColor = FitLadyaTheme.colors.text,
            disabledLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            disabledSupportingTextColor = FitLadyaTheme.colors.error,
            disabledContainerColor = containerColor,
            disabledIndicatorColor = if (isIndicatorEnabled) FitLadyaTheme.colors.primary else containerColor,
            disabledTrailingIconColor = FitLadyaTheme.colors.text
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        supportingText = supportingText
    )
}