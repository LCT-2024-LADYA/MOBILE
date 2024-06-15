package ru.gozerov.presentation.screens.trainer.diary.paste_training.models

sealed interface PasteTrainingEffect {

    object None : PasteTrainingEffect

    object Pasted : PasteTrainingEffect

    class Error(
        val message: String
    ) : PasteTrainingEffect

}