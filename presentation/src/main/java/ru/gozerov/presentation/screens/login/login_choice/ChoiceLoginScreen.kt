package ru.gozerov.presentation.screens.login.login_choice

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.shared.views.Footer
import ru.gozerov.presentation.shared.views.LadyaLogo
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChoiceLoginScreen(
    navController: NavController
) {
    val snackbarHostState = remember { SnackbarHostState() }

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
                    LadyaLogo()
                }
            }

            Spacer(modifier = Modifier.height(72.dp))
            Box(modifier = Modifier.fillMaxWidth()) {

                Icon(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp, bottom = 20.dp),
                    painter = painterResource(id = R.drawable.ic_chess_king),
                    contentDescription = null,
                    tint = FitLadyaTheme.colors.primary.copy(alpha = 0.24f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        modifier = Modifier.size(260.dp, 40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),

                        onClick = {
                            navController.navigate(Screen.Register.route)
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 8.dp),
                            style = FitLadyaTheme.typography.body,
                            text = stringResource(id = R.string.i_am_trainee),
                            fontWeight = FontWeight.Medium,
                            color = FitLadyaTheme.colors.secondaryText,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .size(260.dp, 40.dp),
                        border = BorderStroke(2.dp, FitLadyaTheme.colors.primary),
                        colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primaryBackground),
                        onClick = {
                            navController.navigate(Screen.LoginTrainer.route)
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 8.dp),
                            style = FitLadyaTheme.typography.body,
                            text = stringResource(R.string.i_am_trainer),
                            fontWeight = FontWeight.Medium,
                            color = FitLadyaTheme.colors.buttonText,
                            fontSize = 14.sp
                        )
                    }
                }
                Icon(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 36.dp),
                    painter = painterResource(id = R.drawable.ic_chess_bishop),
                    contentDescription = null,
                    tint = FitLadyaTheme.colors.primary.copy(alpha = 0.24f)
                )

            }

            Footer()
        }
    }
}