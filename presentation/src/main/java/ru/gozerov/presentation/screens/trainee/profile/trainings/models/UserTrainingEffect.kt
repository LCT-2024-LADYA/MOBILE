package ru.gozerov.presentation.screens.trainee.profile.trainings.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.TrainingCard

sealed interface UserTrainingEffect {

    object None : UserTrainingEffect

    data class LoadedTrainings(
        val trainings: Flow<PagingData<TrainingCard>>
    ) : UserTrainingEffect

    class Error(
        val message: String
    ) : UserTrainingEffect

}