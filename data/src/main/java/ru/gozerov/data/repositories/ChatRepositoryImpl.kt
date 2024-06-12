package ru.gozerov.data.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import ru.gozerov.data.api.ChatApi
import ru.gozerov.data.api.models.request.MessageBody
import ru.gozerov.data.api.models.request.MessageData
import ru.gozerov.data.api.models.response.ChatMessageDTO
import ru.gozerov.data.api.models.toChatCard
import ru.gozerov.data.api.models.toChatMessage
import ru.gozerov.data.api.paging.ClientMessagePagingSource
import ru.gozerov.data.api.paging.TrainerMessagePagingSource
import ru.gozerov.data.cache.LoginStorage
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.ChatMessage
import ru.gozerov.domain.repositories.ChatRepository
import ru.gozerov.domain.repositories.LoginRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatApi: ChatApi,
    private val loginRepository: LoginRepository,
    private val loginStorage: LoginStorage,
    private val clientMessagePagingSourceFactory: ClientMessagePagingSource.Factory,
    private val trainerMessagePagingSourceFactory: TrainerMessagePagingSource.Factory
) : ChatRepository {

    override suspend fun getTrainerChats(): List<ChatCard> = runRequestSafelyNotResult(
        checkToken = { loginRepository.checkToken() },
        accessTokenAction = { loginStorage.getTrainerAccessToken() },
        action = { token ->
            chatApi.getTrainerChats(token).map { response -> response.toChatCard() }
        }
    )

    override suspend fun getTrainerChatMessages(userId: Int): Flow<PagingData<ChatMessage>> {
        val messagePagingSource = trainerMessagePagingSourceFactory.create(userId)
        val pager = Pager(PagingConfig(50)) {
            messagePagingSource
        }
        return pager.flow.map { data ->
            data.map { dto -> dto.toChatMessage() }
        }
    }

    override suspend fun getClientChats(): List<ChatCard> = runRequestSafelyNotResult(
        checkToken = { loginRepository.checkToken() },
        accessTokenAction = { loginStorage.getClientAccessToken() },
        action = { token ->
            chatApi.getUserChats(token).map { response -> response.toChatCard() }
        }
    )

    override suspend fun getClientChatMessages(trainerId: Int): Flow<PagingData<ChatMessage>> {
        val messagePagingSource = clientMessagePagingSourceFactory.create(trainerId)
        val pager = Pager(PagingConfig(50)) {
            messagePagingSource
        }
        return pager.flow.map { data ->
            data.map { dto -> dto.toChatMessage() }
        }
    }

    private val messages = mutableListOf<ChatMessage>()

    private val messageFlow = MutableSharedFlow<ChatMessage>(1, 0, BufferOverflow.DROP_OLDEST)

    private var currentSocket: WebSocket? = null

    override suspend fun runWebSocket() {
        currentSocket?.close(1000, null)
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        val role = loginStorage.getRole()
        if (role == 0) return
        runRequestSafelyNotResult(
            checkToken = { loginRepository.checkToken() },
            accessTokenAction = { if (role == 1) loginStorage.getClientAccessToken() else loginStorage.getTrainerAccessToken() },
            action = { token ->

                val client = OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                ).build()

                val request = Request.Builder()
                    .url("ws://176.109.99.250:8080/ws")
                    .addHeader("Sec-WebSocket-Protocol", token)
                    .build()

                val listener = object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        currentSocket = webSocket
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        Log.e("AAA", text)
                        val message = Json.decodeFromString<ChatMessageDTO>(text).toChatMessage()
                        scope.launch {
                            messageFlow.emit(message)
                            messages.add(message)
                        }
                    }

                    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                        println("Received bytes: ${bytes.hex()}")
                    }

                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                        webSocket.close(1000, null)
                        currentSocket = null
                        println("WebSocket closing: $code / $reason")
                    }

                    override fun onFailure(
                        webSocket: WebSocket,
                        t: Throwable,
                        response: Response?
                    ) {
                        println("WebSocket error: ${t.message}")
                    }
                }

                client.newWebSocket(request, listener)
            }
        )
    }

    override suspend fun sendMessage(to: Int, message: String) {
        val body = MessageBody("message", MessageData(to, message, null))
        currentSocket?.send(Json.encodeToString(body))
    }

    override suspend fun checkNewMessages(): Flow<ChatMessage> {
        return messageFlow
    }

}