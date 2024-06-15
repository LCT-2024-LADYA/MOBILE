package ru.gozerov.presentation.screens.trainer.diary.paste_training.models

import ru.gozerov.domain.models.CustomExercise

sealed interface PasteTrainingIntent {

    object Reset : PasteTrainingIntent

    data class ScheduleTraining(
        val id: Int,
        val date: String,
        val timeStart: String,
        val timeEnd: String,
        val exercises: List<CustomExercise>
    ) : PasteTrainingIntent

}