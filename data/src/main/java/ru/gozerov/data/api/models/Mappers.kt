package ru.gozerov.data.api.models

import ru.gozerov.data.api.ApiConstants.BASE_URL_FOR_PHOTO
import ru.gozerov.data.api.models.request.RegisterRequestBody
import ru.gozerov.data.api.models.response.AchievementDTO
import ru.gozerov.data.api.models.response.CreateTrainingResponse
import ru.gozerov.data.api.models.response.CustomExerciseDTO
import ru.gozerov.data.api.models.response.CustomTrainingDTO
import ru.gozerov.data.api.models.response.ExerciseDTO
import ru.gozerov.data.api.models.response.GetScheduleResponse
import ru.gozerov.data.api.models.response.ScheduledTrainingDTO
import ru.gozerov.data.api.models.response.TrainerInfoResponse
import ru.gozerov.data.api.models.response.TrainerMainInfoRequestBody
import ru.gozerov.data.api.models.response.UserInfoResponse
import ru.gozerov.domain.models.Achievement
import ru.gozerov.domain.models.ClientInfo
import ru.gozerov.domain.models.CreatedTraining
import ru.gozerov.domain.models.CustomExercise
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.models.Exercise
import ru.gozerov.domain.models.RegisterModel
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainerInfo
import ru.gozerov.domain.models.TrainerMainInfoDTO
import ru.gozerov.domain.models.Training

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

fun CreateTrainingResponse.toCreatedTraining() = CreatedTraining(id, ids)

fun ScheduledTrainingDTO.toCustomTraining() =
    CustomTraining(
        id,
        date,
        description,
        name,
        time_start,
        time_end,
        exercises.map { exercise -> exercise.toCustomExercise() })

fun CustomExerciseDTO.toCustomExercise() = CustomExercise(
    id,
    name,
    listOf(difficulty, type, muscle, equipment),
    photos.map { url ->
        BASE_URL_FOR_PHOTO + url
    },
    reps,
    sets,
    weight,
    status
)

fun ExerciseDTO.toExercise() =
    Exercise(
        id,
        photos.map { url ->
            BASE_URL_FOR_PHOTO + url
        },
        name,
        listOf(difficulty, type, muscle, equipment)
    )


fun GetScheduleResponse.toScheduledTraining() = ScheduledTraining(date, user_training_ids)

fun CustomTrainingDTO.toTraining() =
    Training(id, name, description, exercises.map { exercise -> exercise.toCustomExercise() })