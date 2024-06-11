package ru.gozerov.presentation.screens.trainee.diary.find_exercise.models

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.gozerov.domain.models.Exercise

sealed interface FindExerciseEffect {

    object None : FindExerciseEffect

    data class LoadedExercises(
        val exercises: Flow<PagingData<Exercise>>
    ) : FindExerciseEffect

    data class Error(
        val message: String
    ) : FindExerciseEffect

    object Exit : FindExerciseEffect

}