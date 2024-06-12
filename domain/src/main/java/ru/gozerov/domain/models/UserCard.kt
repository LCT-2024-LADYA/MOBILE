package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserCard(
    val id: Int,
    val photoUrl: String?,
    val firstName: String,
    val lastName: String,
    val sex: Int,
    val age: Int
) : Parcelable
