package ru.gozerov.data.cache

import android.content.SharedPreferences
import javax.inject.Inject

class TrainingStorageImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TrainingStorage {

    override fun saveLastTrainingId(id: Int) {
        sharedPreferences
            .edit()
            .putInt(KEY_LAST_TRAINING_ID, id)
            .apply()
    }

    override fun getLastTrainingId(): Int = sharedPreferences.getInt(KEY_LAST_TRAINING_ID, -1)

    companion object {

        private const val KEY_LAST_TRAINING_ID = "lastTrainingId"

    }

}