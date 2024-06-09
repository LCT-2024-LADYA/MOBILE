package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Training(
    val id: Int,
    val name: String,
    val description: String,
    val exercises: List<CustomExercise>
) : Parcelable
