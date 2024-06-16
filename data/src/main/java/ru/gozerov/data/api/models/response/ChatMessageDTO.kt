package ru.gozerov.data.api.models.response

import kotlinx.serialization.Serializable


sealed class ChatItemDTO {
    @Serializable
    data class ChatMessageDTO(
        val id: Int,
        val is_to_user: Boolean,
        val message: String?,
        val service_id: Int?,
        val time: String,
        val trainer_id: Int,
        val user_id: Int
    ) : ChatItemDTO()

    data class DateMessageDTO(
        val date: String
    ) : ChatItemDTO()

}
