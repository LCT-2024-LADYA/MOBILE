package ru.gozerov.presentation.screens.trainer.profile

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.Achievement
import ru.gozerov.domain.models.Role
import ru.gozerov.domain.models.Specialization
import ru.gozerov.domain.models.TrainerMainInfoDTO
import ru.gozerov.domain.models.TrainerService
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.profile.edit.EditProfileView
import ru.gozerov.presentation.screens.trainer.profile.models.TrainerProfileEffect
import ru.gozerov.presentation.screens.trainer.profile.models.TrainerProfileIntent
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
fun TrainerProfileScreen(
    parentNavController: NavController,
    navController: NavController,
    viewModel: TrainerProfileViewModel,
    paddingValues: PaddingValues
) {
    val effect = viewModel.effect.collectAsState().value

    LaunchedEffect(null) {
        viewModel.handleIntent(TrainerProfileIntent.GetMainInfo)
    }

    val firstNameState = remember { mutableStateOf("") }
    val lastNameState = remember { mutableStateOf("") }
    val photoState = remember { mutableStateOf<Any?>(null) }
    val newPhotoState = remember { mutableStateOf<Uri?>(null) }
    val availableSex =
        listOf(stringResource(id = R.string.sex_man), stringResource(id = R.string.sex_woman))

    val ageState = remember { mutableStateOf("") }
    val sexState = remember { mutableStateOf("") }

    val allRoles = remember { mutableStateOf<List<Role>>(emptyList()) }
    val roleState = remember { mutableStateOf<Role?>(null) }
    val roleString = remember { mutableStateOf<String>("") }

    val trainerSpecializationState = remember { mutableStateOf<List<Specialization>>(emptyList()) }
    val specializationInputState = remember { mutableStateOf("") }
    val allSpecializations = remember { mutableStateOf<List<Specialization>>(emptyList()) }
    val filteredSpecializations = remember { mutableStateOf<List<Specialization>>(emptyList()) }

    val trainerAchievementsState = remember { mutableStateOf<List<Achievement>>(emptyList()) }
    val achievementState = remember { mutableStateOf("") }

    val dateState = remember { mutableStateOf("") }

    val trainerServicesState = remember { mutableStateOf<List<TrainerService>>(emptyList()) }
    val serviceNameState = remember { mutableStateOf("") }
    val servicePriceState = remember { mutableStateOf("") }
    val quoteState = remember { mutableStateOf<String>("") }
    val experienceState = remember { mutableStateOf("") }

    val emailState = remember { mutableStateOf("") }
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

    var isPlan: Boolean by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val incorrectEmailMessage = stringResource(id = R.string.incorrect_email)
    val incorrectAgeMessage = stringResource(id = R.string.incorrect_age)
    val errorMessage = stringResource(id = R.string.incorrect_data)

    when (effect) {
        is TrainerProfileEffect.None -> {}
        is TrainerProfileEffect.LoadedProfile -> {
            viewModel.handleIntent(TrainerProfileIntent.Reset)
            photoState.value = effect.mainInfo.photoUrl
            firstNameState.value = effect.mainInfo.firstName
            lastNameState.value = effect.mainInfo.lastName
            ageState.value = effect.mainInfo.age.toString()
            sexState.value = availableSex.elementAt(effect.mainInfo.sex - 1)
            emailState.value = effect.mainInfo.email
            quoteState.value = effect.mainInfo.quote ?: ""
            roleState.value = effect.mainInfo.roles.firstOrNull()
            roleString.value = effect.mainInfo.roles.firstOrNull()?.name ?: ""
            trainerSpecializationState.value = effect.mainInfo.specializations
            trainerAchievementsState.value = effect.mainInfo.achievements
            trainerServicesState.value = effect.mainInfo.services
            experienceState.value = effect.mainInfo.experience.toString()
            viewModel.handleIntent(TrainerProfileIntent.GetRoles)
        }

        is TrainerProfileEffect.LoadedRoles -> {
            allRoles.value = effect.roles
            viewModel.handleIntent(TrainerProfileIntent.GetSpecializations)
        }

        is TrainerProfileEffect.LoadedSpecializations -> {
            allSpecializations.value = effect.specializations
            viewModel.handleIntent(TrainerProfileIntent.Reset)
        }

        is TrainerProfileEffect.SuccessPhotoUpload -> {
            snackbarHostState.showError(
                coroutineScope,
                stringResource(id = R.string.success_photo_update)
            )
            photoState.value = newPhotoState.value
            viewModel.handleIntent(TrainerProfileIntent.Reset)
        }

        is TrainerProfileEffect.RemovedPhoto -> {
            photoState.value = null
        }

        is TrainerProfileEffect.SuccessCreatedService -> {
            val newServices = trainerServicesState.value.toMutableList()
            newServices.add(
                TrainerService(
                    effect.id,
                    effect.name,
                    effect.price,
                    effect.profileAccess
                )
            )
            trainerServicesState.value = newServices
            viewModel.handleIntent(TrainerProfileIntent.Reset)
        }

        is TrainerProfileEffect.SuccessCreatedAchievement -> {
            val newAchievements = trainerAchievementsState.value.toMutableList()
            newAchievements.add(Achievement(effect.id, effect.name, false))
            trainerAchievementsState.value = newAchievements
            viewModel.handleIntent(TrainerProfileIntent.Reset)
        }

        is TrainerProfileEffect.SuccessUpdatedProfile -> {
            snackbarHostState.showError(
                coroutineScope,
                stringResource(id = R.string.success_info_update)
            )
            viewModel.handleIntent(TrainerProfileIntent.Reset)
        }

        is TrainerProfileEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(TrainerProfileIntent.Reset)
        }

        is TrainerProfileEffect.Logout -> {
            viewModel.handleIntent(TrainerProfileIntent.Reset)
            parentNavController.navigate(Screen.ChoiceLogin.route) {
                popUpTo(Screen.TrainerTabs.route) {
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
        modifier = Modifier.padding(paddingValues),
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            EditProfileView(
                firstNameState = firstNameState,
                lastNameState = lastNameState,
                photo = photoState.value,
                onPhotoSelected = { uri ->
                    uri?.let { notNullUri ->
                        newPhotoState.value = uri
                        viewModel.handleIntent(TrainerProfileIntent.UpdatePhoto(notNullUri))
                    }
                },
                onRemovePhotoClick = {
                    viewModel.handleIntent(TrainerProfileIntent.RemovePhoto)
                },
                onSaveClicked = {
                    if (!isValidEmail(emailState.value)) {
                        snackbarHostState.showError(coroutineScope, incorrectEmailMessage)
                    } else if (!isValidAge(ageState.value)) {
                        snackbarHostState.showError(coroutineScope, incorrectAgeMessage)
                    } else if ((firstNameState.value.isNotBlank() && lastNameState.value.isNotBlank()
                                && experienceState.value.toIntOrNull() != null) && quoteState.value.isNotBlank()
                    ) {
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                        val info = TrainerMainInfoDTO(
                            ageState.value.toInt(),
                            emailState.value,
                            experienceState.value.toInt(),
                            firstNameState.value,
                            lastNameState.value,
                            quoteState.value,
                            availableSex.indexOf(sexState.value) + 1
                        )

                        val specializationIds =
                            trainerSpecializationState.value.map { specialization -> specialization.id }
                        viewModel.handleIntent(
                            TrainerProfileIntent.UpdateProfile(
                                info,
                                listOf(roleState.value?.id ?: 0),
                                specializationIds
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
                UserAvatar(size = 128.dp, photoState.value)

                Spacer(modifier = Modifier.height(24.dp))
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
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.size(260.dp, 40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                    onClick = {
                        if (!isValidEmail(emailState.value)) {
                            snackbarHostState.showError(coroutineScope, incorrectEmailMessage)
                        } else if (!isValidAge(ageState.value)) {
                            snackbarHostState.showError(coroutineScope, incorrectAgeMessage)
                        } else if ((firstNameState.value.isNotBlank() && lastNameState.value.isNotBlank()
                                    && experienceState.value.toIntOrNull() != null) && quoteState.value.isNotBlank()
                        ) {
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                            val info = TrainerMainInfoDTO(
                                ageState.value.toInt(),
                                emailState.value,
                                experienceState.value.toInt(),
                                firstNameState.value,
                                lastNameState.value,
                                quoteState.value,
                                availableSex.indexOf(sexState.value) + 1
                            )

                            val specializationIds =
                                trainerSpecializationState.value.map { specialization -> specialization.id }
                            viewModel.handleIntent(
                                TrainerProfileIntent.UpdateProfile(
                                    info,
                                    listOf(roleState.value?.id ?: 0),
                                    specializationIds
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

                Spacer(modifier = Modifier.height(20.dp))

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

                        CustomTextField(
                            textState = experienceState,
                            labelText = stringResource(id = R.string.experience),
                            modifier = Modifier.width(260.dp),
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        quoteState.value.let {
                            CustomTextField(
                                labelText = stringResource(id = R.string.favorite_quote),
                                textState = quoteState,
                                modifier = Modifier.width(260.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                        }

                        Box {

                            CustomTextField(
                                textState = roleString,
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
                                allRoles.value.forEach { role ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = role.name,
                                                color = FitLadyaTheme.colors.text
                                            )
                                        },
                                        onClick = {
                                            roleState.value = role
                                            roleString.value = role.name
                                            isRoleExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(id = R.string.specializations),
                            modifier = Modifier
                                .width(260.dp)
                                .background(
                                    FitLadyaTheme.colors.secondary,
                                    RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                )
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                            color = FitLadyaTheme.colors.fieldPrimaryText,
                            fontSize = 12.sp
                        )
                        FlowRow(
                            modifier = Modifier
                                .width(260.dp)
                                .background(FitLadyaTheme.colors.secondary)
                                .padding(start = 16.dp, end = 16.dp, top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            trainerSpecializationState.value.forEach { specialization ->
                                ChipItem(text = specialization.name) { name ->
                                    val newSpecializations =
                                        trainerSpecializationState.value.toMutableList()
                                    var removedItem: Specialization? = null
                                    newSpecializations.removeIf { s ->
                                        if (s.name == name)
                                            removedItem = s
                                        s.name == name
                                    }
                                    removedItem?.let { item ->
                                        val newAll = allSpecializations.value.toMutableList()
                                        val newFilters =
                                            filteredSpecializations.value.toMutableList()
                                        newFilters.add(item)
                                        newAll.add(item)
                                        filteredSpecializations.value = newFilters
                                        allSpecializations.value = newAll
                                    }
                                    isSpecializationExpanded = false
                                    trainerSpecializationState.value = newSpecializations
                                }
                            }
                        }
                    }

                    if (specializationInputState.value.isNotBlank() && isSpecializationExpanded) {
                        LazyColumn(
                            modifier = Modifier
                                .padding(start = 4.dp, end = 4.dp, bottom = 28.dp)
                                .width(320.dp)
                                .heightIn(max = 200.dp)
                                .align(Alignment.BottomCenter)
                                .background(FitLadyaTheme.colors.secondary),
                        ) {
                            items(filteredSpecializations.value.size) { index ->
                                val specialization = filteredSpecializations.value[index]
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = specialization.name,
                                            color = FitLadyaTheme.colors.text
                                        )
                                    },
                                    onClick = {
                                        val newTags =
                                            trainerSpecializationState.value.toMutableList()
                                        newTags.add(specialization)
                                        val newSpecializations =
                                            allSpecializations.value.toMutableList()
                                        val newFilteredSpecializations =
                                            filteredSpecializations.value.toMutableList()
                                        newFilteredSpecializations.remove(specialization)
                                        newSpecializations.remove(specialization)
                                        allSpecializations.value = newSpecializations
                                        filteredSpecializations.value = newFilteredSpecializations
                                        trainerSpecializationState.value = newTags
                                    }
                                )
                            }
                        }
                    }
                }
                BasicTextField(
                    modifier = Modifier
                        .height(40.dp)
                        .width(260.dp),

                    value = specializationInputState.value,
                    onValueChange = { text ->
                        isSpecializationExpanded = true
                        filteredSpecializations.value =
                            allSpecializations.value.filter { specialization ->
                                specialization.name.lowercase().contains(text.lowercase())
                            }
                        specializationInputState.value = text
                    },
                    singleLine = true,
                    textStyle = TextStyle(color = FitLadyaTheme.colors.text),
                    cursorBrush = SolidColor(FitLadyaTheme.colors.primary),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(
                                    FitLadyaTheme.colors.secondary,
                                    RoundedCornerShape(
                                        topStart = 0.dp,
                                        topEnd = 0.dp
                                    )
                                )
                                .padding(horizontal = 16.dp, vertical = 0.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            innerTextField()
                        }
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.width(260.dp),
                    color = FitLadyaTheme.colors.primary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.width(260.dp)) {
                    Text(
                        text = stringResource(id = R.string.sport_achievements),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = FitLadyaTheme.colors.text
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                FlowRow(
                    modifier = Modifier.width(260.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    trainerAchievementsState.value.forEach { achievement ->
                        AchievementChip(achievement) { id ->
                            val newAchievements = trainerAchievementsState.value.toMutableList()
                            newAchievements.removeIf { achievement -> achievement.id == id }
                            trainerAchievementsState.value = newAchievements
                            viewModel.handleIntent(TrainerProfileIntent.RemoveAchievement(id))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    textState = achievementState,
                    labelText = stringResource(id = R.string.achievement),
                    modifier = Modifier.width(260.dp)
                )

                AddAchievementButton(text = stringResource(id = R.string.send_at_moderation)) {
                    viewModel.handleIntent(TrainerProfileIntent.CreateAchievement(achievementState.value))
                }

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    textState = dateState,
                    labelText = stringResource(id = R.string.schedule),
                    modifier = Modifier.width(260.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(contentAlignment = Alignment.TopStart, modifier = Modifier.width(260.dp)) {
                    Text(
                        text = stringResource(id = R.string.services),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = FitLadyaTheme.colors.text
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                FlowRow(
                    modifier = Modifier.width(260.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    trainerServicesState.value.forEach { service ->
                        ServiceChip(service) { id ->
                            val services = trainerServicesState.value.toMutableList()
                            services.removeIf { s -> s.id == id }
                            trainerServicesState.value = services
                            viewModel.handleIntent(TrainerProfileIntent.RemoveService(id))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    textState = serviceNameState,
                    isIndicatorEnabled = false,
                    labelText = stringResource(id = R.string.add_service),
                    modifier = Modifier.width(260.dp)
                )

                CustomTextField(
                    textState = servicePriceState,
                    isRounded = false,
                    isIndicatorEnabled = false,
                    labelText = stringResource(id = R.string.price),
                    modifier = Modifier.width(260.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row(
                    modifier = Modifier
                        .width(260.dp)
                        .background(FitLadyaTheme.colors.secondary),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isPlan,
                        onCheckedChange = { value ->
                            isPlan = value
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = FitLadyaTheme.colors.primary,
                            checkmarkColor = Color.White,
                            uncheckedColor = FitLadyaTheme.colors.fieldPrimaryText
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.plan_or_training),
                        fontSize = 14.sp,
                        color = FitLadyaTheme.colors.text,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember {
                                MutableInteractionSource()
                            }) {
                            isPlan = !isPlan
                        }
                    )
                }

                AddAchievementButton(
                    text = stringResource(id = R.string.add)
                ) {
                    viewModel.handleIntent(
                        TrainerProfileIntent.CreateService(
                            serviceNameState.value,
                            servicePriceState.value.toInt(),
                            isPlan
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                viewModel.handleIntent(TrainerProfileIntent.Logout)
                            }
                        ) {
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
}

@Composable
fun ChipItem(text: String, onRemoveClick: (text: String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(FitLadyaTheme.colors.secondary, RoundedCornerShape(100))
            .border(1.dp, FitLadyaTheme.colors.primaryBorder, RoundedCornerShape(100))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            modifier = Modifier.widthIn(max = 192.dp),
            text = text,
            color = FitLadyaTheme.colors.text,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(
            modifier = Modifier
                .size(16.dp)
                .padding(start = 4.dp),
            onClick = {
                onRemoveClick(text)
            }
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = FitLadyaTheme.colors.text
            )
        }
    }
}

@Composable
fun AchievementChip(achievement: Achievement, onDeleteClick: (id: Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(32.dp)
            .background(FitLadyaTheme.colors.primaryBackground, RoundedCornerShape(8.dp))
            .border(1.dp, FitLadyaTheme.colors.secondaryBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {

        if (achievement.isConfirmed) {
            Icon(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(16.dp),
                imageVector = Icons.Default.Done,
                contentDescription = null,
                tint = FitLadyaTheme.colors.accent
            )
        }

        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .widthIn(max = 192.dp),
            text = achievement.name,
            color = FitLadyaTheme.colors.text,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(
            modifier = Modifier
                .size(16.dp)
                .padding(start = 4.dp),
            onClick = {
                onDeleteClick(achievement.id)
            }
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = FitLadyaTheme.colors.text
            )
        }
    }
}


@Composable
fun ServiceChip(service: TrainerService, onDeleteClick: (id: Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(32.dp)
            .background(FitLadyaTheme.colors.primaryBackground, RoundedCornerShape(8.dp))
            .border(1.dp, FitLadyaTheme.colors.secondaryBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {

        Text(
            modifier = Modifier.widthIn(max = 140.dp),
            text = service.name,
            color = FitLadyaTheme.colors.text,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(id = R.string.vertical_slash),
            color = FitLadyaTheme.colors.text,
            fontSize = 14.sp
        )

        Text(
            modifier = Modifier.widthIn(max = 52.dp),
            text = service.price.toString(),
            color = FitLadyaTheme.colors.accent,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        IconButton(
            modifier = Modifier
                .size(16.dp)
                .padding(start = 4.dp),
            onClick = {
                onDeleteClick(service.id)
            }
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = FitLadyaTheme.colors.text
            )
        }
    }
}

@Composable
fun AddAchievementButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(width = 260.dp, height = 36.dp)
            .background(
                FitLadyaTheme.colors.primary,
                RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            color = FitLadyaTheme.colors.secondaryText
        )
    }

}
