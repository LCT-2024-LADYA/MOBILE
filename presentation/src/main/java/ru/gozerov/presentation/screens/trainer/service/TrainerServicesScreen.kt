package ru.gozerov.presentation.screens.trainer.service

import TrainerServiceCard
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ru.gozerov.domain.models.CustomService
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.screens.trainee.profile.services.views.ConfirmChangesDialog
import ru.gozerov.presentation.screens.trainer.service.models.TrainerServicesEffect
import ru.gozerov.presentation.screens.trainer.service.models.TrainerServicesIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainerServicesScreen(
    contentPaddingValues: PaddingValues,
    viewModel: TrainerServicesViewModel,
    navController: NavController
) {
    val effect = viewModel.effect.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val services = remember { mutableStateOf<LazyPagingItems<CustomService>?>(null) }

    LaunchedEffect(null) {
        viewModel.handleIntent(TrainerServicesIntent.LoadServices)
    }

    when (effect) {
        is TrainerServicesEffect.None -> {}
        is TrainerServicesEffect.LoadedServices -> {
            val data = effect.services.collectAsLazyPagingItems()
            if (data.itemCount > 0)
                services.value = data
        }

        is TrainerServicesEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(TrainerServicesIntent.Reset)
        }

        is TrainerServicesEffect.DeletedService -> {
            viewModel.handleIntent(TrainerServicesIntent.LoadServices)
        }
    }


    val onDialogConfirm = remember { mutableStateOf<(status: Boolean) -> Unit>({}) }

    var showDialog: Boolean by remember { mutableStateOf(false) }
    var onPositiveClicked: Boolean by remember { mutableStateOf(false) }

    if (showDialog) {
        ConfirmChangesDialog(
            isPositive = onPositiveClicked,
            onDismiss = {
                showDialog = false
            },
            onConfirm = onDialogConfirm.value
        )
    }

    Scaffold(
        modifier = Modifier.padding(contentPaddingValues),
        containerColor = FitLadyaTheme.colors.primaryBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.services_manager),
                fontSize = 22.sp,
                color = FitLadyaTheme.colors.text,
                modifier = Modifier.padding(top = 16.dp, start = 32.dp, bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                services.value?.let { lazyItems ->
                    items(lazyItems.itemCount) { index ->
                        val service = lazyItems[index]
                        service?.let {
                            var isTrainerApproved: Boolean? by remember { mutableStateOf(service.isTrainerApproved) }
                            TrainerServiceCard(
                                service = service,
                                isTrainerApproved = isTrainerApproved,
                                onClick = { s ->
                                    onPositiveClicked = s
                                    showDialog = true
                                    onDialogConfirm.value = { status ->
                                        when (isTrainerApproved) {
                                            null -> {
                                                viewModel.handleIntent(
                                                    TrainerServicesIntent.SetStatus(
                                                        service.id,
                                                        status,
                                                        2
                                                    )
                                                )
                                                isTrainerApproved = status
                                            }

                                            true -> {
                                                viewModel.handleIntent(
                                                    TrainerServicesIntent.SetStatus(
                                                        service.id,
                                                        false,
                                                        2
                                                    )
                                                )
                                                isTrainerApproved = false
                                            }

                                            else -> {
                                                viewModel.handleIntent(
                                                    TrainerServicesIntent.SetStatus(
                                                        service.id,
                                                        true,
                                                        2
                                                    )
                                                )
                                                isTrainerApproved = true
                                            }
                                        }
                                        showDialog = false
                                    }
                                },
                                onPlan = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set(
                                        "id",
                                        service.userId
                                    )
                                    navController.navigate(Screen.CreatePlan.route)
                                },
                                onBottomAction = {
                                    viewModel.handleIntent(
                                        TrainerServicesIntent.DeleteService(
                                            service.id
                                        )
                                    )
                                }
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}