package ru.gozerov.data.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gozerov.data.api.models.response.GetChatMessagesResponseBody
import ru.gozerov.data.api.models.response.GetChatsResponseBody

interface ChatApi {

    @GET("api/chat/trainer")
    suspend fun getTrainerChats(@Header("access_token") token: String): List<GetChatsResponseBody>

    @GET("api/chat/trainer/{user_id}")
    suspend fun getTrainerChatMessages(
        @Header("access_token") token: String,
        @Path("user_id") userId: Int,
        @Query("cursor") cursor: Int
    ): GetChatMessagesResponseBody

    @GET("api/chat/user")
    suspend fun getUserChats(@Header("access_token") token: String): List<GetChatsResponseBody>

    @GET("api/chat/user/{trainer_id}")
    suspend fun getUserChatMessages(
        @Header("access_token") token: String,
        @Path("trainer_id") userId: Int,
        @Query("cursor") cursor: Int
    ): GetChatMessagesResponseBody

}