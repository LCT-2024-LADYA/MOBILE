package ru.gozerov.presentation.screens.login.login_trainer

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.gozerov.presentation.screens.login.login_trainer.models.LoginTrainerIntent
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginTrainerScreen(viewModel: LoginTrainerViewModel) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = email.value, onValueChange = { value ->
                email.value = value
            })
            TextField(value = password.value, onValueChange = { value ->
                password.value = value
            })
            Button(
                onClick = {
                    viewModel.handleIntent(LoginTrainerIntent.Login(email.value, password.value))
                }
            ) {
                Text(text = "ИДИ НАХУЙ")
            }
        }
    }

}