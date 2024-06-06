package ru.gozerov.domain.repositories

import ru.gozerov.domain.models.Training

interface TrainingRepository {

    suspend fun getMainTraining(): Training?

}