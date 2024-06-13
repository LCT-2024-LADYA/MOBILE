package ru.gozerov.presentation.screens.trainee.chat.list.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.domain.models.Role
import ru.gozerov.domain.models.Specialization
import ru.gozerov.presentation.R
import ru.gozerov.presentation.screens.trainer.profile.ChipItem
import ru.gozerov.presentation.ui.theme.FitLadyaTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrainerFilterView(
    selectedRole: MutableState<Int>,
    selectedSpecializations: MutableState<List<Specialization>>,
    roles: List<Role>,
    specializations: MutableState<List<Specialization>>,
    onConfirm: () -> Unit
) {
    val specializationInputState = remember { mutableStateOf("") }
    val filteredSpecializations = remember { mutableStateOf<List<Specialization>>(emptyList()) }
    var isSpecializationExpanded: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                FitLadyaTheme.colors.secondary,
                RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                isSpecializationExpanded = false
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(32.dp, 4.dp)
                .background(FitLadyaTheme.colors.text, RoundedCornerShape(100))
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.filters),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = FitLadyaTheme.colors.text
        )
        Spacer(modifier = Modifier.height(32.dp))


        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    contentAlignment = Alignment.CenterStart, modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                ) {
                    LazyColumn {
                        items(roles.size) { index ->
                            val role = roles[index]
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        selectedRole.value = roles.indexOf(role)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = FitLadyaTheme.colors.primary,
                                        unselectedColor = FitLadyaTheme.colors.primaryBorder
                                    ),
                                    selected = selectedRole.value == roles.indexOf(role),
                                    onClick = {
                                        selectedRole.value = roles.indexOf(role)
                                    }
                                )
                                Text(
                                    modifier = Modifier.padding(start = 8.dp),
                                    text = role.name,
                                    color = FitLadyaTheme.colors.text,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
                Text(
                    text = stringResource(id = R.string.specializations),
                    modifier = Modifier
                        .width(320.dp)
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
                        .width(320.dp)
                        .background(FitLadyaTheme.colors.secondary)
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    selectedSpecializations.value.forEach { specialization ->
                        ChipItem(text = specialization.name) { name ->
                            val newSpecializations =
                                selectedSpecializations.value.toMutableList()
                            var removedItem: Specialization? = null
                            newSpecializations.removeIf { s ->
                                if (s.name == name)
                                    removedItem = s
                                s.name == name
                            }
                            removedItem?.let { item ->
                                val newAll = specializations.value.toMutableList()
                                val newFilters = filteredSpecializations.value.toMutableList()
                                newFilters.add(item)
                                newAll.add(item)
                                filteredSpecializations.value = newFilters
                                specializations.value = newAll
                            }
                            isSpecializationExpanded = false
                            selectedSpecializations.value = newSpecializations
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
                        .background(FitLadyaTheme.colors.primaryBackground),
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
                                    selectedSpecializations.value.toMutableList()
                                newTags.add(specialization)
                                val newSpecializations =
                                    specializations.value.toMutableList()
                                val newFilteredSpecializations =
                                    filteredSpecializations.value.toMutableList()
                                newFilteredSpecializations.remove(specialization)
                                newSpecializations.remove(specialization)
                                specializations.value = newSpecializations
                                filteredSpecializations.value = newFilteredSpecializations
                                selectedSpecializations.value = newTags
                            }
                        )
                    }
                }
            }
        }
        BasicTextField(
            modifier = Modifier
                .height(40.dp)
                .width(320.dp),

            value = specializationInputState.value,
            onValueChange = { text ->
                isSpecializationExpanded = true
                filteredSpecializations.value =
                    specializations.value.filter { specialization ->
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
            modifier = Modifier.width(320.dp),
            color = FitLadyaTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FitLadyaTheme.colors.primary),
            onClick = {
                onConfirm()
            }
        ) {
            Text(
                text = stringResource(id = R.string.approve),
                color = FitLadyaTheme.colors.secondaryText
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}