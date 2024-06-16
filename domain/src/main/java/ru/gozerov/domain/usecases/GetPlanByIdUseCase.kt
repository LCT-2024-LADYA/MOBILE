package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.TrainingPlan
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class GetPlanByIdUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(id: Int): TrainingPlan = withContext(Dispatchers.IO) {
        return@withContext trainingRepository.getPlan(id)
    }

}