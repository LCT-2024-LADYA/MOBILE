package ru.gozerov.presentation.screens.trainer.diary.create_service

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ru.gozerov.domain.models.TrainerService
import ru.gozerov.domain.models.UserCard
import ru.gozerov.presentation.R
import ru.gozerov.presentation.screens.trainer.diary.create_service.models.CreateServiceEffect
import ru.gozerov.presentation.screens.trainer.diary.create_service.models.CreateServiceIntent
import ru.gozerov.presentation.screens.trainer.diary.create_service.views.AttachServiceToCreating
import ru.gozerov.presentation.screens.trainer.diary.create_service.views.AttachUserToCreating
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.shared.views.DateDDMMYYYYTextField
import ru.gozerov.presentation.shared.views.DateHHMMTextField
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateServiceScreen(
    navController: NavController,
    viewModel: CreateServiceViewModel,
    serviceDate: String?,
    contentPaddingValues: PaddingValues
) {

    val effect = viewModel.effect.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val searchUserState = remember { mutableStateOf("") }

    val services = remember { mutableStateOf(emptyList<TrainerService>()) }
    val serviceState = remember { mutableStateOf("") }
    var showAttachService: Boolean by remember { mutableStateOf(false) }

    val clientState = remember { mutableStateOf("") }
    val clients = remember { mutableStateOf<LazyPagingItems<UserCard>?>(null) }
    val currentUser = remember { mutableStateOf<UserCard?>(null) }
    var showAttachUser: Boolean by remember { mutableStateOf(false) }

    val date = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(serviceDate ?: ""))
    }
    val timeStart =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val timeEnd =
        rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(null) {
        viewModel.handleIntent(CreateServiceIntent.LoadServices)
    }

    when (effect) {
        is CreateServiceEffect.None -> {}
        is CreateServiceEffect.LoadedServices -> {
            services.value = effect.services
            viewModel.handleIntent(CreateServiceIntent.SearchUsers(searchUserState.value))
        }

        is CreateServiceEffect.LoadedUsers -> {
            clients.value = effect.users.collectAsLazyPagingItems()
        }

        is CreateServiceEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(CreateServiceIntent.Reset)
        }

        is CreateServiceEffect.CreatedService -> {
            navController.popBackStack()
            viewModel.handleIntent(CreateServiceIntent.Reset)
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        modifier = Modifier.padding(contentPaddingValues),
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            if (showAttachService)
                AttachServiceToCreating(
                    services = services.value,
                    onSelect = { index ->
                        serviceState.value = services.value[index].name
                    }
                )
            else
                clients.value?.let { data ->
                    AttachUserToCreating(
                        searchState = searchUserState,
                        onTextChange = { value ->
                            searchUserState.value = value
                            viewModel.handleIntent(CreateServiceIntent.SearchUsers(value))
                        },
                        data = data,
                        onProfileClick = { client ->
                            currentUser.value = client
                            clientState.value = "${client.lastName} ${client.firstName}"
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.collapse()
                            }
                        }
                    )
                }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = FitLadyaTheme.colors.secondary,
            topBar = {
                NavUpWithTitleToolbar(
                    navController = navController,
                    title = stringResource(id = R.string.service_creation)
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .clickable(indication = null, interactionSource = remember {
                        MutableInteractionSource()
                    }) {
                        if (scaffoldState.bottomSheetState.isExpanded) {
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                CustomTextField(
                    textState = clientState,
                    isEnabled = false,
                    labelText = stringResource(id = R.string.client),
                    containerColor = FitLadyaTheme.colors.primaryBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            showAttachUser = true
                            showAttachService = false
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                showAttachUser = true
                                showAttachService = false
                                coroutineScope.launch {
                                    scaffoldState.bottomSheetState.expand()
                                }
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.accent
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    textState = serviceState,
                    isEnabled = false,
                    containerColor = FitLadyaTheme.colors.primaryBackground,
                    labelText = stringResource(id = R.string.service),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            showAttachUser = false
                            showAttachService = true
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                showAttachUser = false
                                showAttachService = true
                                coroutineScope.launch {
                                    scaffoldState.bottomSheetState.expand()
                                }
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                tint = FitLadyaTheme.colors.accent
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                DateDDMMYYYYTextField(
                    textState = date,
                    labelText = stringResource(id = R.string.date),
                    containerColor = FitLadyaTheme.colors.primaryBackground,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                DateHHMMTextField(
                    textState = timeStart,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    containerColor = FitLadyaTheme.colors.primaryBackground,
                    labelText = stringResource(id = R.string.time_start),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                DateHHMMTextField(
                    textState = timeEnd,
                    labelText = stringResource(id = R.string.time_end),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    containerColor = FitLadyaTheme.colors.primaryBackground,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                    onClick = {
                        val service =
                            services.value.firstOrNull { s -> s.name == serviceState.value }?.id
                        if (currentUser.value?.id != null && service != null &&
                            timeStart.value.text.length == 5 && timeEnd.value.text.length == 5
                            && date.value.text.length == 10
                        )
                            viewModel.handleIntent(
                                CreateServiceIntent.CreateService(
                                    currentUser.value?.id!!,
                                    service,
                                    date.value.text,
                                    timeStart.value.text,
                                    timeEnd.value.text
                                )
                            )
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.save),
                        color = FitLadyaTheme.colors.secondaryText
                    )
                }
            }
        }
    }
}