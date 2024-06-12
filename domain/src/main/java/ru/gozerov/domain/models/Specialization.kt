package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Specialization(
    val id: Int,
    val name: String
) : Parcelable
