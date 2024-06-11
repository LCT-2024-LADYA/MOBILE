package ru.gozerov.domain.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.TrainingCard
import ru.gozerov.domain.repositories.TrainingRepository
import javax.inject.Inject

class GetTrainingsUseCase @Inject constructor(
    private val trainingRepository: TrainingRepository
) {

    suspend operator fun invoke(query: String): Flow<PagingData<TrainingCard>> =
        withContext(Dispatchers.IO) {
            return@withContext trainingRepository.getSimpleTrainings(query.ifBlank { null })
        }

}