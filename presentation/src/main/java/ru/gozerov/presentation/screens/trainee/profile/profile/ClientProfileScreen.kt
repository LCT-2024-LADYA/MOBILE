package ru.gozerov.presentation.screens.trainee.profile.profile

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.profile.edit.EditProfileView
import ru.gozerov.presentation.screens.trainee.profile.profile.models.ClientProfileEffect
import ru.gozerov.presentation.screens.trainee.profile.profile.models.ClientProfileIntent
import ru.gozerov.presentation.shared.utils.isValidAge
import ru.gozerov.presentation.shared.utils.isValidEmail
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.shared.views.ProfileToolbar
import ru.gozerov.presentation.shared.views.UserAvatar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme
import ru.gozerov.presentation.ui.theme.isNightMode
import ru.gozerov.presentation.ui.theme.setTheme

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClientProfileScreen(
    parentNavController: NavController,
    navController: NavController,
    viewModel: ClientProfileViewModel,
    contentPaddingValues: PaddingValues
) {

    LaunchedEffect(null) {
        viewModel.handleIntent(ClientProfileIntent.GetInfo)
    }

    val context = LocalContext.current

    val availableSex =
        listOf(stringResource(id = R.string.sex_man), stringResource(id = R.string.sex_woman))
    val effect = viewModel.effect.collectAsState().value

    val firstNameState = remember { mutableStateOf("") }
    val lastNameState = remember { mutableStateOf("") }
    val photoState = remember { mutableStateOf<Any?>(null) }
    val newPhotoState = remember { mutableStateOf<Uri?>(null) }

    val ageState = remember { mutableStateOf("") }
    val sexState = remember { mutableStateOf("") }

    val emailState = remember { mutableStateOf("") }
    val isEmailError = if (emailState.value.isBlank()) false else !isValidEmail(emailState.value)


    var isExpanded: Boolean by remember { mutableStateOf(false) }
    val iconDropdown =
        if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val sexInteractionSource = remember { mutableStateOf(MutableInteractionSource()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val errorMessage = stringResource(id = R.string.incorrect_data)
    val incorrectEmailMessage = stringResource(id = R.string.incorrect_email)
    val incorrectAgeMessage = stringResource(id = R.string.incorrect_age)


    val nightMode = isNightMode()
    val isDarkTheme = remember { mutableStateOf(nightMode) }

    when (effect) {
        is ClientProfileEffect.None -> {}
        is ClientProfileEffect.LoadedProfile -> {
            photoState.value = effect.clientInfo.photoUrl
            firstNameState.value = effect.clientInfo.firstName
            lastNameState.value = effect.clientInfo.lastName
            emailState.value = effect.clientInfo.email
            ageState.value = effect.clientInfo.age.toString()
            sexState.value = availableSex[effect.clientInfo.sex - 1]
            viewModel.handleIntent(ClientProfileIntent.Reset)
        }

        is ClientProfileEffect.SuccessfulInfoUpdate -> {
            viewModel.handleIntent(ClientProfileIntent.Reset)
            snackbarHostState.showError(
                coroutineScope,
                stringResource(R.string.success_info_update)
            )
        }

        is ClientProfileEffect.SuccessfulPhotoUpdate -> {
            viewModel.handleIntent(ClientProfileIntent.Reset)
            photoState.value = newPhotoState.value
            snackbarHostState.showError(
                coroutineScope,
                stringResource(R.string.success_photo_update)
            )
        }

        is ClientProfileEffect.RemovedPhoto -> {
            photoState.value = null
        }

        is ClientProfileEffect.Error -> {
            viewModel.handleIntent(ClientProfileIntent.Reset)
            snackbarHostState.showError(coroutineScope, effect.message)
        }

        ClientProfileEffect.Logout -> {
            viewModel.handleIntent(ClientProfileIntent.Reset)
            parentNavController.navigate(Screen.ChoiceLogin.route) {
                popUpTo(Screen.TraineeTabs.route) {
                    inclusive = true
                }
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        modifier = Modifier.padding(contentPaddingValues),
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            EditProfileView(
                firstNameState = firstNameState,
                lastNameState = lastNameState,
                photo = photoState.value,
                onPhotoSelected = { uri ->
                    uri?.let { notNullUri ->
                        newPhotoState.value = uri
                        viewModel.handleIntent(ClientProfileIntent.UpdatePhoto(notNullUri))
                    }
                },
                onRemovePhotoClick = {
                    viewModel.handleIntent(ClientProfileIntent.RemovePhoto)
                },
                onSaveClicked = {
                    if (!isValidEmail(emailState.value)) {
                        snackbarHostState.showError(coroutineScope, incorrectEmailMessage)
                    } else if (!isValidAge(ageState.value)) {
                        snackbarHostState.showError(coroutineScope, incorrectAgeMessage)
                    } else if (firstNameState.value.isNotBlank() && lastNameState.value.isNotBlank()) {
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                        viewModel.handleIntent(
                            ClientProfileIntent.UpdateInfo(
                                ageState.value.toInt(),
                                availableSex.indexOf(sexState.value) + 1,
                                emailState.value,
                                firstNameState.value,
                                lastNameState.value
                            )
                        )
                    } else {
                        snackbarHostState.showError(coroutineScope, errorMessage)
                    }
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = FitLadyaTheme.colors.primaryBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                ProfileToolbar {
                    navController.popBackStack()
                }

                Spacer(modifier = Modifier.height(8.dp))
                UserAvatar(size = 128.dp, photoState.value)

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${firstNameState.value} ${lastNameState.value}",
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
                Spacer(modifier = Modifier.height(12.dp))

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
                        } else if (firstNameState.value.isNotBlank() && lastNameState.value.isNotBlank()) {
                            viewModel.handleIntent(
                                ClientProfileIntent.UpdateInfo(
                                    ageState.value.toInt(),
                                    availableSex.indexOf(sexState.value) + 1,
                                    emailState.value,
                                    firstNameState.value,
                                    lastNameState.value
                                )
                            )
                        } else {
                            snackbarHostState.showError(coroutineScope, errorMessage)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.save_changes),
                        color = FitLadyaTheme.colors.secondaryText
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    ProfileActionCard(
                        text = stringResource(id = R.string.trainings),
                        painter = painterResource(id = R.drawable.ic_hear_outlined),
                        iconTint = FitLadyaTheme.colors.accent
                    ) {

                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    ProfileActionCard(
                        text = stringResource(id = R.string.progress),
                        painter = painterResource(id = R.drawable.ic_statistics),
                        iconTint = FitLadyaTheme.colors.primary
                    ) {
                        navController.navigate(Screen.ClientStatisticsScreen.route)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                ProfileActionCard(
                    text = stringResource(id = R.string.services),
                    painter = painterResource(id = R.drawable.ic_cash),
                    iconTint = FitLadyaTheme.colors.cashColor
                ) {
                    parentNavController.navigate(Screen.ClientService.route)
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.dark_theme),
                        color = FitLadyaTheme.colors.text,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = FitLadyaTheme.colors.primary,
                            checkedTrackColor = FitLadyaTheme.colors.fieldPrimaryText,
                            checkedBorderColor = FitLadyaTheme.colors.fieldPrimaryText,
                            uncheckedThumbColor = FitLadyaTheme.colors.primary,
                            uncheckedTrackColor = FitLadyaTheme.colors.secondary,
                            uncheckedBorderColor = FitLadyaTheme.colors.primary
                        ),
                        checked = isDarkTheme.value,
                        onCheckedChange = { value ->
                            isDarkTheme.value = value
                            setTheme(context, isDarkTheme.value)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        viewModel.handleIntent(ClientProfileIntent.Logout)
                    },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.logout),
                        fontWeight = FontWeight.Medium,
                        color = FitLadyaTheme.colors.text,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = null,
                        tint = FitLadyaTheme.colors.accent
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

            }
        }
    }


}

@Composable
fun ProfileActionCard(
    width: Dp = 128.dp,
    text: String,
    painter: Painter,
    iconTint: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .size(width, 104.dp)
            .background(FitLadyaTheme.colors.secondary, RoundedCornerShape(12.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
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