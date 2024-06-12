package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainerService(
    val id: Int,
    val name: String,
    val price: Int
): Parcelable
