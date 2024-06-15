package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainingPlan(
    val id: Int,
    val name: String,
    val description: String,
    val trainings: List<TrainingCard>
): Parcelable
