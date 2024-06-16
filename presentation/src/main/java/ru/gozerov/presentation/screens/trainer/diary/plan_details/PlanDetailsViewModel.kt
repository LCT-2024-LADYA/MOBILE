package ru.gozerov.presentation.screens.trainer.diary.plan_details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

@HiltViewModel
class PlanDetailsViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository
) : ViewModel() {

    suspend fun getPlan(id: Int) = trainingRepository.getPlan(id)

}