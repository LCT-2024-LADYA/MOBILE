package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduledTraining(
    val date: String,
    val ids: List<Int>
): Parcelable
