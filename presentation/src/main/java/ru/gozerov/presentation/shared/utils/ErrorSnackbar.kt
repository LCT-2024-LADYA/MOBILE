package ru.gozerov.presentation.shared.utils

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun SnackbarHostState.showError(
    coroutineScope: CoroutineScope,
    message: String
) {
    coroutineScope.launch {
        showSnackbar(message = message)
    }
}
