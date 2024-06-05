package ru.gozerov.presentation.screens.trainer

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.gozerov.presentation.R
import ru.gozerov.presentation.screens.trainee.profile.ProfileActionCard
import ru.gozerov.presentation.screens.trainee.profile.edit.EditProfileView
import ru.gozerov.presentation.shared.utils.isValidAge
import ru.gozerov.presentation.shared.utils.isValidEmail
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.shared.views.ProfileToolbar
import ru.gozerov.presentation.shared.views.UserAvatar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainerProfileScreen(navController: NavController) {
    val availableSex =
        listOf(stringResource(id = R.string.sex_man), stringResource(id = R.string.sex_woman))

    val availableRole = remember { mutableStateOf(listOf<String>()) }
    // val effect = viewModel.effect.collectAsState().value

    val ageState = remember { mutableStateOf("18") }
    val sexState = remember { mutableStateOf("Мужской") }
    val roleState = remember { mutableStateOf("") }

    val specializationState = remember { mutableStateOf("") }

    val emailState = remember { mutableStateOf("ozeroffgrisha@gmail.com") }
    val isEmailError = if (emailState.value.isBlank()) false else !isValidEmail(emailState.value)


    var isSexExpanded: Boolean by remember { mutableStateOf(false) }
    val iconSexDropdown =
        if (isSexExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val sexInteractionSource = remember { mutableStateOf(MutableInteractionSource()) }

    var isRoleExpanded: Boolean by remember { mutableStateOf(false) }
    val iconRoleDropdown =
        if (isRoleExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
    val roleInteractionSource = remember { mutableStateOf(MutableInteractionSource()) }

    var isSpecializationExpanded: Boolean by remember { mutableStateOf(false) }

    val roles = remember { mutableStateOf(listOf("Role1", "Role2", "Role3", "Role4", "Role5")) }
    val tags = remember { mutableStateOf(listOf<String>()) }
    val focusRequester = FocusRequester()
    val containerColor = FitLadyaTheme.colors.secondary

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
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    isSpecializationExpanded = false
                },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = FitLadyaTheme.colors.primaryBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState()),
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
                Spacer(modifier = Modifier.height(24.dp))

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

                CustomTextField(
                    labelText = stringResource(id = R.string.age),
                    textState = ageState,
                    modifier = Modifier.width(260.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

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
                                        isSexExpanded = !isSexExpanded
                                    },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            isSexExpanded = !isSexExpanded
                                        }
                                    ) {
                                        Icon(
                                            imageVector = iconSexDropdown,
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
                                expanded = isSexExpanded,
                                onDismissRequest = {
                                    isSexExpanded = false
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
                                        isSexExpanded = false
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
                                        isSexExpanded = false
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

                        Box {

                            CustomTextField(
                                textState = roleState,
                                isEnabled = false,
                                labelText = stringResource(id = R.string.role),
                                modifier = Modifier
                                    .width(260.dp)
                                    .clickable(
                                        interactionSource = roleInteractionSource.value,
                                        indication = null
                                    ) {
                                        isRoleExpanded = !isRoleExpanded
                                    },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            isRoleExpanded = !isRoleExpanded
                                        }
                                    ) {
                                        Icon(
                                            imageVector = iconRoleDropdown,
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
                                expanded = isRoleExpanded,
                                onDismissRequest = {
                                    isRoleExpanded = false
                                }
                            ) {
                                availableRole.value.forEach { role ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = role,
                                                color = FitLadyaTheme.colors.text
                                            )
                                        },
                                        onClick = {
                                            sexState.value = role
                                            isRoleExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }


                        FlowRow(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            tags.value.forEach { text ->
                                ChipItem(text = text)
                            }
                        }
                    }

                    if (specializationState.value.isNotBlank() && isSpecializationExpanded) {
                        Column(
                            modifier = Modifier
                                .width(260.dp)
                                .background(FitLadyaTheme.colors.secondary)
                                .padding(horizontal = 4.dp),
                        ) {
                            roles.value.forEach { role ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = role,
                                            color = FitLadyaTheme.colors.text
                                        )
                                    },
                                    onClick = {
                                        val newTags = tags.value.toMutableList()
                                        newTags.add(role)
                                        val newRoles =  roles.value.toMutableList()
                                        newRoles.remove(role)
                                        roles.value = newRoles
                                        tags.value = newTags
                                        isSpecializationExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                TextField(
                    modifier = Modifier
                        .width(260.dp)
                        .focusRequester(focusRequester),
                    label = { Text(text = stringResource(id = R.string.specializations)) },
                    value = specializationState.value,
                    onValueChange = { text ->
                        isSpecializationExpanded = true
                        val notFilteredRoles = roles.value
                        roles.value =
                            notFilteredRoles.filter { it.lowercase().contains(text.lowercase()) }
                        specializationState.value = text
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
                        unfocusedLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
                        focusedTextColor = FitLadyaTheme.colors.text,
                        focusedContainerColor = containerColor,
                        unfocusedTextColor = FitLadyaTheme.colors.text,
                        unfocusedContainerColor = containerColor,
                        focusedIndicatorColor = FitLadyaTheme.colors.primary,
                        unfocusedIndicatorColor = FitLadyaTheme.colors.primary,
                        errorCursorColor = FitLadyaTheme.colors.errorText,
                        errorLabelColor = FitLadyaTheme.colors.errorText,
                        errorTextColor = FitLadyaTheme.colors.text,
                        errorContainerColor = containerColor,
                        errorSupportingTextColor = FitLadyaTheme.colors.errorText,
                        errorIndicatorColor = FitLadyaTheme.colors.error,
                        cursorColor = FitLadyaTheme.colors.primary,
                        disabledTextColor = FitLadyaTheme.colors.text,
                        disabledLabelColor = FitLadyaTheme.colors.fieldPrimaryText,
                        disabledSupportingTextColor = FitLadyaTheme.colors.error,
                        disabledContainerColor = containerColor,
                        disabledIndicatorColor = FitLadyaTheme.colors.primary,
                        disabledTrailingIconColor = FitLadyaTheme.colors.text
                    ),
                )

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
fun ChipItem(text: String) {
    Text(
        modifier = Modifier
            .background(FitLadyaTheme.colors.primaryBackground, RoundedCornerShape(4.dp))
            .border(1.dp, FitLadyaTheme.colors.primaryBorder)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = text,
        style = FitLadyaTheme.typography.body,
        fontSize = 14.sp
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Hueta(textState: MutableState<String>) {


}