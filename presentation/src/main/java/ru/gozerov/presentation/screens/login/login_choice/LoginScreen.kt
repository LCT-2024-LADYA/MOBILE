package ru.gozerov.presentation.screens.login.login_choice

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.login.login_choice.models.LoginEffect
import ru.gozerov.presentation.screens.login.login_choice.models.LoginIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController
) {
    val effect = viewModel.effect.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val errorMessage = stringResource(id = R.string.error)
    val snackbarHostState = remember { SnackbarHostState() }

    when (effect) {
        is LoginEffect.None -> {}
        is LoginEffect.SuccessLogin -> {
            viewModel.handleIntent(LoginIntent.Navigate)
            navController.navigate(Screen.VerifyEmail.route + "/${effect.token}/${effect.vkId}")
        }

        is LoginEffect.Error -> {
            snackbarHostState.showError(coroutineScope, errorMessage)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.ic_ddx_logo),
                        contentDescription = null
                    )
                    Image(
                        modifier = Modifier.padding(vertical = 20.dp),
                        painter = painterResource(id = R.drawable.ic_clear_logo),
                        contentDescription = null
                    )
                    Row {
                        Image(
                            modifier = Modifier.padding(end = 16.dp, top = 4.dp),
                            painter = painterResource(id = R.drawable.ic_chess_rook),
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(R.string.ladyafit),
                            fontSize = 36.sp,
                            fontFamily = FontFamily(Font(R.font.russo_one)),
                        )

                    }
                }
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Button(
                        modifier = Modifier.size(200.dp, 48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),

                        onClick = {
                            viewModel.handleIntent(LoginIntent.LoginThroughVK)
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 8.dp),
                            style = FitLadyaTheme.typography.body,
                            text = stringResource(id = R.string.login),
                            color = FitLadyaTheme.colors.secondary,
                            fontSize = 14.sp
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_vk_16),
                            contentDescription = null,
                            tint = FitLadyaTheme.colors.secondary
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .size(200.dp, 48.dp),
                        border = BorderStroke(2.dp, FitLadyaTheme.colors.primary),
                        colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primaryBackground),
                        onClick = {
                            navController.navigate(Screen.LoginTrainer.route)
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 8.dp),
                            style = FitLadyaTheme.typography.body,
                            text = stringResource(R.string.login_as_trainer),
                            color = FitLadyaTheme.colors.primary,
                            fontSize = 14.sp
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chess_14),
                            contentDescription = null,
                            tint = FitLadyaTheme.colors.primary
                        )
                    }
                }

            }
        }
    }
}