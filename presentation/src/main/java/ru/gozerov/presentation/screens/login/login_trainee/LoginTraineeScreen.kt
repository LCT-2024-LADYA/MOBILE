package ru.gozerov.presentation.screens.login.login_trainee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.login.login_trainee.models.LoginTraineeEffect
import ru.gozerov.presentation.screens.login.login_trainee.models.LoginTraineeIntent
import ru.gozerov.presentation.shared.utils.isValidEmail
import ru.gozerov.presentation.shared.utils.isValidPassword
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.shared.views.Footer
import ru.gozerov.presentation.shared.views.LadyaLogo
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@Composable
fun LoginTraineeScreen(
    navController: NavController,
    viewModel: LoginTraineeViewModel
) {
    val effect = viewModel.effect.collectAsState().value
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    var isPasswordVisible: Boolean by remember { mutableStateOf(true) }

    val isEmailError = if (emailState.value.isBlank()) false else !isValidEmail(emailState.value)

    val visibilityImage = if (isPasswordVisible)
        painterResource(id = R.drawable.ic_visibility_24)
    else
        painterResource(id = R.drawable.ic_visibility_off_24)

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val incorrectEmailMessage = stringResource(id = R.string.incorrect_email)
    val incorrectPasswordMessage = stringResource(id = R.string.incorrect_password)

    when (effect) {
        is LoginTraineeEffect.None -> {}
        is LoginTraineeEffect.SuccessLogin -> {
            viewModel.handleIntent(LoginTraineeIntent.Reset)
        }

        is LoginTraineeEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(LoginTraineeIntent.Reset)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(120.dp))
            LadyaLogo()

            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = stringResource(id = R.string.log_in_account),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = FitLadyaTheme.colors.text
            )
            Spacer(modifier = Modifier.height(32.dp))

            CustomTextField(
                labelText = stringResource(id = R.string.input_mail),
                textState = emailState,
                modifier = Modifier.width(260.dp),
                isError = isEmailError
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                textState = passwordState,
                labelText = stringResource(id = R.string.input_password),
                modifier = Modifier.width(260.dp),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(painter = visibilityImage, null, tint = FitLadyaTheme.colors.text)
                    }
                }
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                modifier = Modifier.size(260.dp, 40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                onClick = {
                    if (!isValidEmail(emailState.value)) {
                        snackbarHostState.showError(coroutineScope, incorrectEmailMessage)
                    } else if (!isValidPassword(passwordState.value)) {
                        snackbarHostState.showError(coroutineScope, incorrectPasswordMessage)
                    } else {
                        viewModel.handleIntent(
                            LoginTraineeIntent.Login(
                                emailState.value,
                                passwordState.value
                            )
                        )
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    color = FitLadyaTheme.colors.secondaryText
                )
            }
            val loginText = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append(stringResource(id = R.string.no_account))
                }
                pushStringAnnotation(
                    tag = "click",
                    annotation = stringResource(id = R.string.login)
                )
                withStyle(
                    style = SpanStyle(
                        color = FitLadyaTheme.colors.accent,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append(stringResource(id = R.string.sign_up))
                }
                pop()
            }

            Spacer(modifier = Modifier.height(12.dp))
            ClickableText(
                text = loginText,
                onClick = { offset ->
                    loginText.getStringAnnotations(tag = "click", start = offset, end = offset)
                        .firstOrNull()?.let {
                            navController.navigate(Screen.Register.route) {
                                launchSingleTop = true
                                popUpTo(Screen.LoginTrainee.route) { inclusive = true }
                            }
                        }
                },
            )

            Footer()
        }
    }
}