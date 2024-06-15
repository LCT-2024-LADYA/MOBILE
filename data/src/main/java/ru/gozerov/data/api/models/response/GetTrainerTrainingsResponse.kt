package ru.gozerov.data.api.models.response

import ru.gozerov.domain.models.TrainerTrainingCard

data class GetTrainerTrainingsResponse(
    val cursor: Int,
    val objects: List<TrainerTrainingCard>
)
