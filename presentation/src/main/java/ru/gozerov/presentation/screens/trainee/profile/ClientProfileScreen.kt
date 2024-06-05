package ru.gozerov.presentation.screens.trainee.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.rememberModalBottomSheetState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.gozerov.presentation.R
import ru.gozerov.presentation.screens.trainee.profile.edit.EditProfileView
import ru.gozerov.presentation.shared.utils.isValidAge
import ru.gozerov.presentation.shared.utils.isValidEmail
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.shared.views.ProfileToolbar
import ru.gozerov.presentation.shared.views.UserAvatar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClientProfileScreen(navController: NavController) {
    val availableSex =
        listOf(stringResource(id = R.string.sex_man), stringResource(id = R.string.sex_woman))
    // val effect = viewModel.effect.collectAsState().value

    val ageState = remember { mutableStateOf("18") }
    val sexState = remember { mutableStateOf("Мужской") }

    val emailState = remember { mutableStateOf("ozeroffgrisha@gmail.com") }
    val isEmailError = if (emailState.value.isBlank()) false else !isValidEmail(emailState.value)


    var isExpanded: Boolean by remember { mutableStateOf(false) }
    val iconDropdown =
        if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val sexInteractionSource = remember { mutableStateOf(MutableInteractionSource()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val incorrectEmailMessage = stringResource(id = R.string.incorrect_email)
    val incorrectAgeMessage = stringResource(id = R.string.incorrect_age)

    /*
        when (effect) {
            is LoginTrainerEffect.None -> {}
            is LoginTrainerEffect.SuccessLogin -> {
                viewModel.handleIntent(LoginTrainerIntent.Navigate)
            }

            is LoginTrainerEffect.Error -> {
                snackbarHostState.showError(coroutineScope, effect.message)
            }

        }*/

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            EditProfileView()
        }
    ) {
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

                ProfileToolbar {
                    navController.popBackStack()
                }

                Spacer(modifier = Modifier.height(16.dp))
                UserAvatar(size = 128.dp)

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Самара Бутсович",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FitLadyaTheme.colors.text
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                sheetState.show()
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            tint = FitLadyaTheme.colors.text
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))

                CustomTextField(
                    isError = !isValidAge(ageState.value),
                    labelText = stringResource(id = R.string.age),
                    textState = ageState,
                    modifier = Modifier.width(260.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box {

                    CustomTextField(
                        textState = sexState,
                        isEnabled = false,
                        labelText = stringResource(id = R.string.sex),
                        modifier = Modifier
                            .width(260.dp)
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
                            .width(260.dp)
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

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    textState = emailState,
                    isError = isEmailError,
                    labelText = stringResource(id = R.string.email),
                    modifier = Modifier.width(260.dp),
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    modifier = Modifier.size(260.dp, 40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                    onClick = {
                        if (!isValidEmail(emailState.value)) {
                            snackbarHostState.showError(coroutineScope, incorrectEmailMessage)
                        } else if (!isValidAge(ageState.value)) {
                            snackbarHostState.showError(coroutineScope, incorrectAgeMessage)
                        } else {
                            // viewModel.handleIntent()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.save_changes),
                        color = FitLadyaTheme.colors.secondaryText
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row {
                    ProfileActionCard(
                        text = stringResource(id = R.string.favorites),
                        painter = painterResource(id = R.drawable.ic_hear_outlined),
                        iconTint = FitLadyaTheme.colors.accent
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ProfileActionCard(
                        text = stringResource(id = R.string.progress),
                        painter = painterResource(id = R.drawable.ic_statistics),
                        iconTint = FitLadyaTheme.colors.primary
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        Text(
                            text = stringResource(id = R.string.logout),
                            fontWeight = FontWeight.Medium,
                            color = FitLadyaTheme.colors.text
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            contentDescription = null,
                            tint = FitLadyaTheme.colors.accent
                        )
                    }
                }

            }
        }
    }


}

@Composable
fun ProfileActionCard(
    text: String,
    painter: Painter,
    iconTint: Color
) {
    Column(
        modifier = Modifier
            .size(128.dp, 104.dp)
            .background(FitLadyaTheme.colors.secondary, RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painter,
            contentDescription = null,
            tint = iconTint
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            color = FitLadyaTheme.colors.text
        )
    }
}