package ru.gozerov.data.cache

interface TrainingStorage {

    fun saveLastTrainingId(id: Int)

    fun getLastTrainingId(): Int

}