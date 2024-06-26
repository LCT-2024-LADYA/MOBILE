package ru.gozerov.presentation.screens.trainee.diary.create_training.models

import ru.gozerov.domain.models.CreateTrainingModel

sealed interface CreateTrainingIntent {

    object Reset : CreateTrainingIntent

    data class CreateTraining(
        val createTrainingModel: CreateTrainingModel,
        val date: String,
        val timeStart: String,
        val timeEnd: String
    ) : CreateTrainingIntent

    data class GetTraining(
        val id: Int
    ) : CreateTrainingIntent

    data class RemoveExercise(
        val id: Int,
        val index: Int
    ) : CreateTrainingIntent

    object GetAddedExercises : CreateTrainingIntent

    object Clear : CreateTrainingIntent

    data class NextTraining(
        val id: Int?
    ) : CreateTrainingIntent

    data class CreateTrainerTraining(
        val createTrainingModel: CreateTrainingModel,
        val wantsPublic: Boolean,
        val date: String,
        val timeStart: String,
        val timeEnd: String
    ) : CreateTrainingIntent

}