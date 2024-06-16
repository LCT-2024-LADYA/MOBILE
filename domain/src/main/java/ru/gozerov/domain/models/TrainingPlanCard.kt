package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainingPlanCard(
    val id: Int,
    val name: String,
    val description: String,
    val trainings: Int
): Parcelable
