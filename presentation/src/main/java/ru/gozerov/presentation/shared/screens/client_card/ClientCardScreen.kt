package ru.gozerov.presentation.shared.screens.client_card

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ru.gozerov.domain.models.ClientInfo
import ru.gozerov.domain.utils.getAgeString
import ru.gozerov.presentation.R
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.shared.screens.client_card.models.ClientCardEffect
import ru.gozerov.presentation.shared.screens.client_card.models.ClientCardIntent
import ru.gozerov.presentation.shared.utils.showError
import ru.gozerov.presentation.shared.utils.toChatCard
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClientCardScreen(
    clientId: Int,
    navController: NavController,
    viewModel: ClientCardViewModel
) {
    val effect = viewModel.effect.collectAsState().value

    val availableSex =
        listOf(stringResource(id = R.string.sex_man_d), stringResource(id = R.string.sex_woman_d))

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val clientState = remember { mutableStateOf<ClientInfo?>(null) }

    when (effect) {
        is ClientCardEffect.None -> {}

        is ClientCardEffect.LoadedProfile -> {
            clientState.value = effect.client
        }

        is ClientCardEffect.Error -> {
            snackbarHostState.showError(coroutineScope, effect.message)
            viewModel.handleIntent(ClientCardIntent.Reset)
        }
    }

    LaunchedEffect(null) {
        viewModel.handleIntent(ClientCardIntent.LoadProfile(clientId))
    }

    val client = clientState.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        client?.let {
            Column(modifier = Modifier.fillMaxSize()) {

                NavUpWithTitleToolbar(
                    navController = navController,
                    title = "${client.firstName} ${client.lastName}"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(360.dp)
                        .background(
                            FitLadyaTheme.colors.avatarBackground,
                            RoundedCornerShape(16.dp)
                        ),
                ) {
                    client.photoUrl?.let { _ ->
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                            model = client.photoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    } ?: Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                            .padding(top = 56.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        painter = painterResource(id = R.drawable.ic_profile_man),
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    text = "${client.firstName} ${client.lastName}",
                    color = FitLadyaTheme.colors.text,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                val ageText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = FitLadyaTheme.colors.text,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append(stringResource(id = R.string.age))
                    }
                    withStyle(
                        style = SpanStyle(
                            color = FitLadyaTheme.colors.accent,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("  ${getAgeString(client.age)}")
                    }
                }
                Text(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    text = ageText
                )

                Spacer(modifier = Modifier.height(12.dp))
                val sexText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = FitLadyaTheme.colors.text,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append(stringResource(id = R.string.sex))
                    }
                    withStyle(
                        style = SpanStyle(
                            color = FitLadyaTheme.colors.accent,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("  ${availableSex[client.sex - 1]}")
                    }
                }
                Text(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    text = sexText
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                    onClick = {
                        Log.e("AAAA", client.id.toString())
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "client",
                            client.toChatCard()
                        )
                        navController.navigate(Screen.TrainerChat.route)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.to_text),
                        color = FitLadyaTheme.colors.secondaryText
                    )
                }
            }
        }
    }
}