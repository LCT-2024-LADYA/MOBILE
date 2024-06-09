package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomExercise(
    val id: Int,
    val name: String,
    val tags: List<String>,
    val photos: List<String>,
    val reps: Int,
    val sets: Int,
    val weight: Int,
    val isDone: Boolean
) : Parcelable
