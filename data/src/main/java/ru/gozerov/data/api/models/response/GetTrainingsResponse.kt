package ru.gozerov.data.api.models.response

import ru.gozerov.domain.models.TrainingCard

data class GetTrainingsResponse(
    val cursor: Int,
    val objects: List<TrainingCard>
)