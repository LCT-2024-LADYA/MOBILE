package ru.gozerov.data.cache

import android.content.SharedPreferences
import javax.inject.Inject

class LoginStorageImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences
) : LoginStorage {

    override fun saveClientTokens(accessToken: String, refreshToken: String) =
        sharedPrefs
            .edit()
            .putString(KEY_CLIENT_ACCESS_TOKEN, accessToken)
            .putString(KEY_CLIENT_REFRESH_TOKEN, refreshToken)
            .putInt(KEY_LAST_LOGGED_USER_TYPE, 1)
            .apply()

    override fun getClientAccessToken(): String? =
        sharedPrefs.getString(KEY_CLIENT_ACCESS_TOKEN, null)

    override fun getClientRefreshToken(): String? =
        sharedPrefs.getString(KEY_CLIENT_REFRESH_TOKEN, null)

    override fun saveTrainerTokens(accessToken: String, refreshToken: String) =
        sharedPrefs
            .edit()
            .putString(KEY_TRAINER_ACCESS_TOKEN, accessToken)
            .putString(KEY_TRAINER_REFRESH_TOKEN, refreshToken)
            .putInt(KEY_LAST_LOGGED_USER_TYPE, 2)
            .apply()

    override fun getTrainerAccessToken(): String? =
        sharedPrefs.getString(KEY_TRAINER_ACCESS_TOKEN, null)

    override fun getTrainerRefreshToken(): String? =
        sharedPrefs.getString(KEY_TRAINER_REFRESH_TOKEN, null)

    override fun getRole(): Int = sharedPrefs
        .getInt(KEY_LAST_LOGGED_USER_TYPE, 0)

    override fun clearClientTokens() {
        sharedPrefs
            .edit()
            .remove(KEY_CLIENT_ACCESS_TOKEN)
            .remove(KEY_CLIENT_REFRESH_TOKEN)
            .apply()
    }

    override fun clearTrainerTokens() {
        sharedPrefs
            .edit()
            .remove(KEY_TRAINER_ACCESS_TOKEN)
            .remove(KEY_TRAINER_REFRESH_TOKEN)
            .apply()
    }

    companion object {

        private const val KEY_CLIENT_ACCESS_TOKEN = "clientAccessToken"
        private const val KEY_CLIENT_REFRESH_TOKEN = "clientRefreshToken"

        private const val KEY_TRAINER_ACCESS_TOKEN = "trainerAccessToken"
        private const val KEY_TRAINER_REFRESH_TOKEN = "trainerRefreshToken"

        private const val KEY_LAST_LOGGED_USER_TYPE = "lastLoggedUserType"

    }

}