package ru.gozerov.presentation.shared.utils

import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.ClientInfo
import ru.gozerov.domain.models.TrainerCard
import ru.gozerov.domain.models.TrainerInfo

fun TrainerCard.toChatCard() = ChatCard(id, firstName, lastName, photoUrl, "", "")

fun TrainerInfo.toChatCard() = ChatCard(id, firstName, lastName, photoUrl, "", "")

fun ClientInfo.toChatCard() = ChatCard(id, firstName, lastName, photoUrl, "", "")