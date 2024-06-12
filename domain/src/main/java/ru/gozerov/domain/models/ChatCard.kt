package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatCard(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val photoUrl: String?,
    val lastMessage: String,
    val timeLastMessage: String
) : Parcelable
