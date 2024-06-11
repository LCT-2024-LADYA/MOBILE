package ru.gozerov.presentation.screens.trainee.diary.find_training.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.TrainingCard

sealed interface FindTrainingEffect {

    object None : FindTrainingEffect

    data class LoadedAllTrainings(
        val flow: Flow<PagingData<TrainingCard>>
    ) : FindTrainingEffect

    data class LoadedUserTrainings(
        val flow: Flow<PagingData<TrainingCard>>
    ) : FindTrainingEffect

    data class AllFoundTrainings(
        val allFlow: Flow<PagingData<TrainingCard>>,
        val userFlow: Flow<PagingData<TrainingCard>>
    ) : FindTrainingEffect

    data class Error(
        val message: String
    ) : FindTrainingEffect

}