package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Achievement(
    val id: Int,
    val name: String,
    val isConfirmed: Boolean
) : Parcelable