package ru.gozerov.presentation.screens.login.register_trainee

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.login.register_trainee.models.RegisterEffect
import ru.gozerov.presentation.screens.login.register_trainee.models.RegisterIntent
import ru.gozerov.presentation.shared.utils.isValidAge
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.shared.views.LadyaLogo
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterProfileScreen(
    navController: NavController,
    viewModel: RegisterViewModel,
    email: String,
    password: String
) {
    val availableSex =
        listOf(stringResource(id = R.string.sex_man), stringResource(id = R.string.sex_woman))

    val effect = viewModel.effect.collectAsState().value

    val firstNameState = remember { mutableStateOf("") }
    val lastNameState = remember { mutableStateOf("") }

    val ageState = remember { mutableStateOf("") }
    val sexState = remember { mutableStateOf("Мужской") }

    var isExpanded: Boolean by remember { mutableStateOf(false) }
    val iconDropdown =
        if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val sexInteractionSource = remember { mutableStateOf(MutableInteractionSource()) }


    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val notEnoughDataMessage = stringResource(id = R.string.not_enough_data)

    when (effect) {
        is RegisterEffect.None -> {}

        is RegisterEffect.SuccessLoginTrainee -> {
            viewModel.handleIntent(RegisterIntent.Navigate)
            navController.navigate(Screen.ClientProfile.route)
        }

        is RegisterEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(48.dp))
            LadyaLogo()

            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = stringResource(id = R.string.fit_profile),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = FitLadyaTheme.colors.text
            )
            Spacer(modifier = Modifier.height(32.dp))

            CustomTextField(
                modifier = Modifier.width(280.dp),
                labelText = stringResource(id = R.string.name),
                textState = firstNameState
            )
            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                labelText = stringResource(id = R.string.lastname),
                textState = lastNameState
            )
            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                isError = !isValidAge(ageState.value),
                labelText = stringResource(id = R.string.age),
                textState = ageState,
                modifier = Modifier.width(280.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box {

                CustomTextField(
                    textState = sexState,
                    isEnabled = false,
                    labelText = stringResource(id = R.string.sex),
                    modifier = Modifier
                        .width(280.dp)
                        .clickable(
                            interactionSource = sexInteractionSource.value,
                            indication = null
                        ) {
                            isExpanded = !isExpanded
                        },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isExpanded = !isExpanded
                            }
                        ) {
                            Icon(
                                imageVector = iconDropdown,
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.accent
                            )
                        }
                    }
                )

                DropdownMenu(
                    modifier = Modifier
                        .width(280.dp)
                        .background(FitLadyaTheme.colors.secondary)
                        .padding(horizontal = 4.dp),
                    expanded = isExpanded,
                    onDismissRequest = {
                        isExpanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = availableSex[0],
                                color = FitLadyaTheme.colors.text
                            )
                        },
                        onClick = {
                            sexState.value = availableSex[0]
                            isExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = availableSex[1],
                                color = FitLadyaTheme.colors.text
                            )
                        },
                        onClick = {
                            sexState.value = availableSex[1]
                            isExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier.size(260.dp, 40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                onClick = {
                    if (firstNameState.value.isNotBlank() && lastNameState.value.isNotBlank() && isValidAge(
                            ageState.value
                        )
                    ) {
                        viewModel.handleIntent(
                            RegisterIntent.Register(
                                email,
                                password,
                                firstNameState.value,
                                lastNameState.value,
                                ageState.value.toInt(),
                                if (sexState.value == availableSex[0]) 1 else 2
                            )
                        )
                    } else {
                        snackbarHostState.showError(coroutineScope, notEnoughDataMessage)
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up),
                    color = FitLadyaTheme.colors.secondaryText
                )
            }
        }
    }
}