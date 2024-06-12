package ru.gozerov.presentation.shared.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.presentation.R
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
                modifier = Modifier.padding(end = 8.dp),
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


@Composable
fun SearchTextField(
    textState: MutableState<String>,
    onValueChange: ((String) -> Unit)? = null,
    onFilter: () -> Unit,
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) { onFilter() }
                        .padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = null,
                    tint = FitLadyaTheme.colors.text
                )
                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    modifier = Modifier.padding(end = 16.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = FitLadyaTheme.colors.text
                )
            }
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


@Composable
fun MessageTextField(
    textState: MutableState<String>,
    onValueChange: ((String) -> Unit)? = null,
    placeholderText: String,
    onlySend: Boolean = false,
    onSend: () -> Unit,
    onAttach: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .clip(CircleShape)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = {
            onSend()
        }),
        value = textState.value,
        onValueChange = onValueChange ?: {
            textState.value = it
        },
        placeholder = {
            Text(
                text = placeholderText,
                color = FitLadyaTheme.colors.text.copy(alpha = 0.32f),
                fontSize = 16.sp
            )
        },
        trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!onlySend) {
                    Icon(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) { onAttach() },
                        painter = painterResource(id = R.drawable.ic_attach),
                        contentDescription = null,
                        tint = FitLadyaTheme.colors.fieldPrimaryText
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Icon(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) { onSend() },
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = FitLadyaTheme.colors.text
                )
            }

        },
        colors = TextFieldDefaults.colors(
            focusedLabelColor = FitLadyaTheme.colors.text,
            unfocusedLabelColor = FitLadyaTheme.colors.text,
            focusedContainerColor = FitLadyaTheme.colors.secondary,
            unfocusedContainerColor = FitLadyaTheme.colors.secondary,
            focusedIndicatorColor = FitLadyaTheme.colors.secondary,
            unfocusedIndicatorColor = FitLadyaTheme.colors.secondary,
            cursorColor = FitLadyaTheme.colors.fieldPrimaryText,
            focusedTextColor = FitLadyaTheme.colors.text,
            unfocusedTextColor = FitLadyaTheme.colors.text,
            disabledContainerColor = FitLadyaTheme.colors.secondary,
            disabledLabelColor = FitLadyaTheme.colors.text,
            disabledIndicatorColor = FitLadyaTheme.colors.secondary,
            disabledTextColor = FitLadyaTheme.colors.text,
        )
    )
}