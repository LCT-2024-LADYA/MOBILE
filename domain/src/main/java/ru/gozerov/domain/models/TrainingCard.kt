package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainingCard(
    val id: Int,
    val name: String,
    val exercises: Int,
    val description: String
) : Parcelable
