package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Training(
    val id: Int,
    val name: String,
    val date: String,
    val time: String,
    val exerciseCount: Int,
    val description: String,
    val exercises: List<Exercise>
) : Parcelable
