package ru.gozerov.data.cache

interface LoginStorage {

    fun saveClientTokens(accessToken: String, refreshToken: String)

    fun getClientAccessToken(): String?

    fun getClientRefreshToken(): String?

    fun saveTrainerTokens(accessToken: String, refreshToken: String)

    fun getTrainerAccessToken(): String?

    fun getTrainerRefreshToken(): String?


    // 0 - no auth user
    // 1 - client
    // 2 - trainer
    fun getRole(): Int

    fun clearClientTokens()

}