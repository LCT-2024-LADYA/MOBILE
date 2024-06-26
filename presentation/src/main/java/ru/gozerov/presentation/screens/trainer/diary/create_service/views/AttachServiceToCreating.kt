package ru.gozerov.presentation.screens.trainer.diary.create_service.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gozerov.domain.models.TrainerService
import ru.gozerov.presentation.R
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@Composable
fun AttachServiceToCreating(
    services: List<TrainerService>,
    onSelect: (index: Int) -> Unit
) {

    val selectedService = remember { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(id = R.string.attach_service),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = FitLadyaTheme.colors.text
        )

        Spacer(modifier = Modifier.height(24.dp))
        Box {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                items(services.size) { index ->
                    val service = services[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                selectedService.intValue = services.indexOf(service)
                                onSelect(selectedService.intValue)
                            }
                    ) {
                        RadioButton(
                            colors = RadioButtonDefaults.colors(
                                selectedColor = FitLadyaTheme.colors.accent,
                                unselectedColor = FitLadyaTheme.colors.text
                            ),
                            selected = selectedService.intValue == services.indexOf(service),
                            onClick = {
                                selectedService.intValue = services.indexOf(service)
                                onSelect(selectedService.intValue)
                            }
                        )
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 4.dp),
                                text = service.name,
                                color = FitLadyaTheme.colors.text,
                                maxLines = 2
                            )
                            Text(
                                text = stringResource(id = R.string.price_is, service.price),
                                color = FitLadyaTheme.colors.text.copy(alpha = 0.48f)
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}