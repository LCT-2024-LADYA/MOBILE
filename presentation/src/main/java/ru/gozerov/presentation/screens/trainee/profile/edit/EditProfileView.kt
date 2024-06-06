package ru.gozerov.presentation.screens.trainee.profile.edit

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.presentation.R
import ru.gozerov.presentation.shared.utils.RequestImageStorage
import ru.gozerov.presentation.shared.views.CustomTextField
import ru.gozerov.presentation.shared.views.UserAvatar
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditProfileView(
    firstNameState: MutableState<String>,
    lastNameState: MutableState<String>,
    photo: Any?,
    onPhotoSelected: (uri: Uri?) -> Unit,
    onSaveClicked: () -> Unit
) {
    val launchStoragePermissionState = remember { mutableStateOf(false) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            onPhotoSelected(uri)
            launchStoragePermissionState.value = false
        }

    if (launchStoragePermissionState.value) {
        RequestImageStorage {
            launcher.launch("image/*")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                FitLadyaTheme.colors.primaryBackground,
                RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(32.dp, 4.dp)
                .background(FitLadyaTheme.colors.text, RoundedCornerShape(100))
        )
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(id = R.string.data_changing),
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = FitLadyaTheme.colors.text
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .fillMaxWidth()
        ) {
            UserAvatar(size = 68.dp, photo, padding = 8.dp)
            Column(
                modifier = Modifier.padding(start = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.size(200.dp, 40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
                    onClick = {
                        launchStoragePermissionState.value = false
                        launchStoragePermissionState.value = true
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.change_photo),
                        color = FitLadyaTheme.colors.secondaryText
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = R.string.remove_photo),
                    color = FitLadyaTheme.colors.text
                )

            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(labelText = stringResource(id = R.string.name), textState = firstNameState, containerColor = FitLadyaTheme.colors.primaryBackground)
        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(labelText = stringResource(id = R.string.lastname), textState = lastNameState, containerColor = FitLadyaTheme.colors.primaryBackground)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.size(284.dp, 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
            onClick = onSaveClicked
        ) {
            Text(
                text = stringResource(id = R.string.save_changes),
                color = FitLadyaTheme.colors.secondaryText
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}