package ru.gozerov.data.api.models

import ru.gozerov.data.api.ApiConstants.BASE_URL_FOR_PHOTO
import ru.gozerov.data.api.models.request.RegisterRequestBody
import ru.gozerov.data.api.models.response.AchievementDTO
import ru.gozerov.data.api.models.response.TrainerInfoResponse
import ru.gozerov.data.api.models.response.TrainerMainInfoRequestBody
import ru.gozerov.data.api.models.response.UserInfoResponse
import ru.gozerov.domain.models.Achievement
import ru.gozerov.domain.models.ClientInfo
import ru.gozerov.domain.models.RegisterModel
import ru.gozerov.domain.models.TrainerInfo
import ru.gozerov.domain.models.TrainerMainInfoDTO

fun UserInfoResponse.toClientInfo() =
    ClientInfo(
        id,
        first_name,
        last_name,
        age,
        email,
        if (photo_url == null) null else BASE_URL_FOR_PHOTO + photo_url,
        sex
    )

fun TrainerInfoResponse.toTrainerInfo() =
    TrainerInfo(
        id,
        first_name,
        last_name,
        email,
        if (photo_url == null) null else BASE_URL_FOR_PHOTO + photo_url,
        age,
        sex,
        experience,
        quote,
        roles,
        specializations,
        services,
        achievements.map { dto -> dto.toAchievement() }
    )

fun RegisterModel.toRegisterRequestBody() =
    RegisterRequestBody(email, password, firstName, lastName, age, sex)

fun AchievementDTO.toAchievement() = Achievement(id, name, is_confirmed)

fun TrainerMainInfoDTO.toTrainerMainInfoRequestBody() =
    TrainerMainInfoRequestBody(age, email, experience, firstName, lastName, quote, sex)