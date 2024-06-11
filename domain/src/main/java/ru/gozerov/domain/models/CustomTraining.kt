package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomTraining(
    val id: Int,
    val date: String,
    val description: String,
    val name: String,
    val timeStart: String,
    val timeEnd: String,
    val exercises: List<CustomExercise>
): Parcelable
