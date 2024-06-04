package ru.gozerov.presentation.screens.login.verify_email

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.gozerov.presentation.screens.login.verify_email.models.VerifyEmailIntent
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VerifyEmailScreen(accessToken: String, vkId: Long, viewModel: VerifyEmailViewModel) {

    val email = remember { mutableStateOf("") }

    Scaffold(
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) { contentPadding ->
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            TextField(value = email.value, onValueChange = { value ->
                email.value = value
            })
            Button(
                onClick = {
                    viewModel.handleIntent(
                        VerifyEmailIntent.VerifyEmail(
                            accessToken,
                            vkId,
                            email.value
                        )
                    )
                }
            ) {
                Text(text = "ИДИ НАХУЙ")
            }
        }
    }

}