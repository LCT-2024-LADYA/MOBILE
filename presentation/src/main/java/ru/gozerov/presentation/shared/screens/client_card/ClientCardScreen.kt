package ru.gozerov.presentation.shared.screens.client_card

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import ru.gozerov.domain.models.UserCard
import ru.gozerov.domain.utils.getAgeString
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.views.NavUpWithTitleToolbar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClientCardScreen(
    contentPaddingValues: PaddingValues,
    userCard: UserCard,
    navController: NavController
) {
    val availableSex =
        listOf(stringResource(id = R.string.sex_man_d), stringResource(id = R.string.sex_woman_d))

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier
            .padding(contentPaddingValues)
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FitLadyaTheme.colors.primaryBackground
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            NavUpWithTitleToolbar(
                navController = navController,
                title = "${userCard.firstName} ${userCard.lastName}"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(360.dp)
                    .background(FitLadyaTheme.colors.avatarBackground, RoundedCornerShape(16.dp)),
            ) {
                userCard.photoUrl?.let { _ ->
                    AsyncImage(
                        modifier = Modifier.clip(CircleShape),
                        model = userCard.photoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                } ?: Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    painter = painterResource(id = R.drawable.ic_profile_man),
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier.padding(horizontal = 32.dp),
                text = "${userCard.firstName} ${userCard.lastName}",
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
                    append("  ${getAgeString(userCard.age)}")
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
                    append("  ${availableSex[userCard.sex - 1]}")
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