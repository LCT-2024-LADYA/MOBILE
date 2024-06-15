package ru.gozerov.presentation.screens.trainee.diary.select_training.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.TrainingCard

sealed interface SelectTrainingEffect {


    object None : SelectTrainingEffect

    data class LoadedAllTrainings(
        val flow: Flow<PagingData<TrainingCard>>
    ) : SelectTrainingEffect

    data class LoadedTrainerTrainings(
        val flow: Flow<PagingData<TrainingCard>>
    ) : SelectTrainingEffect

    data class AllFoundTrainings(
        val allFlow: Flow<PagingData<TrainingCard>>,
        val trainerFlow: Flow<PagingData<TrainingCard>>
    ) : SelectTrainingEffect

    data class Error(
        val message: String
    ) : SelectTrainingEffect

}