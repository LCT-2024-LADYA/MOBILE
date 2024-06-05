package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    labelText: String,
    textState: MutableState<String>,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    containerColor: Color = FitLadyaTheme.colors.secondary,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null
) {
    TextField(
        isError = isError,
        enabled = isEnabled,
        label = { Text(text = labelText) },
        value = textState.value,
        onValueChange = { textState.value = it },
        modifier = modifier,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            unfocusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            focusedTextColor = FitLadyaTheme.colors.text,
            focusedContainerColor = containerColor,
            unfocusedTextColor = FitLadyaTheme.colors.text,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor = FitLadyaTheme.colors.primary,
            unfocusedIndicatorColor = FitLadyaTheme.colors.primary,
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
            disabledIndicatorColor = FitLadyaTheme.colors.primary,
            disabledTrailingIconColor = FitLadyaTheme.colors.text
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        supportingText = supportingText
    )
}