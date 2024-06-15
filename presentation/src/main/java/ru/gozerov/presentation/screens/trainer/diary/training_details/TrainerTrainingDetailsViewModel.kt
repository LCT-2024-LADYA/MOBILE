package ru.gozerov.presentation.screens.trainer.diary.training_details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.gozerov.domain.models.CustomTrainerTraining
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

@HiltViewModel
class TrainerTrainingDetailsViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository
) : ViewModel() {

    suspend fun getTraining(id: Int): CustomTrainerTraining {
        return trainingRepository.getTrainerTrainingById(id)
    }

}