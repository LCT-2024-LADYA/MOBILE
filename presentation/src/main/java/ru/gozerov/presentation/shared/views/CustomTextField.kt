package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    labelText: String,
    textState: MutableState<String>,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null
) {
    TextField(
        isError = isError,
        label = { Text(text = labelText) },
        value = textState.value,
        onValueChange = { textState.value = it },
        modifier = modifier,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            unfocusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
            focusedTextColor = FitLadyaTheme.colors.text,
            focusedContainerColor = FitLadyaTheme.colors.secondary,
            unfocusedTextColor = FitLadyaTheme.colors.text,
            unfocusedContainerColor = FitLadyaTheme.colors.secondary,
            focusedIndicatorColor = FitLadyaTheme.colors.primary,
            unfocusedIndicatorColor = FitLadyaTheme.colors.primary,
            errorCursorColor = FitLadyaTheme.colors.errorText,
            errorLabelColor = FitLadyaTheme.colors.errorText,
            errorTextColor = FitLadyaTheme.colors.text,
            errorContainerColor = FitLadyaTheme.colors.secondary,
            errorSupportingTextColor = FitLadyaTheme.colors.errorText,
            errorIndicatorColor = FitLadyaTheme.colors.error,
            cursorColor = FitLadyaTheme.colors.primary
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        supportingText = supportingText
    )
}