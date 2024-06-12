package ru.gozerov.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainerCard(
    val id: Int,
    val photoUrl: String?,
    val firstName: String,
    val lastName: String,
    val roles: List<Role>,
    val age: Int,
    val sex: Int,
    val quote: String?,
    val specializations: List<Specialization>,
    val services: List<TrainerService>,
    val achievements: List<Achievement>,
    val workingDays: String,
    val workingTime: String,
    val experience: Int
) : Parcelable
