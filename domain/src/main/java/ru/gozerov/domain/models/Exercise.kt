package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    val id: Int,
    val photos: List<String>,
    val name: String,
    val tags: List<String>,
    val weight: Double,
    val setsCount: Int,
    val repsCount: Int
) : Parcelable
