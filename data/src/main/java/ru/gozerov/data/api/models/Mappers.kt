package ru.gozerov.data.api.models

import ru.gozerov.data.api.ApiConstants.BASE_URL_FOR_PHOTO
import ru.gozerov.data.api.models.request.CreateExerciseDTO
import ru.gozerov.data.api.models.request.CreateTrainerTrainingDTO
import ru.gozerov.data.api.models.request.CreateTrainingDTO
import ru.gozerov.data.api.models.request.RegisterRequestBody
import ru.gozerov.data.api.models.request.TrainerMainInfoRequestBody
import ru.gozerov.data.api.models.response.AchievementDTO
import ru.gozerov.data.api.models.response.ChatItemDTO
import ru.gozerov.data.api.models.response.ClientCover
import ru.gozerov.data.api.models.response.ClientProfileDTO
import ru.gozerov.data.api.models.response.CreateTrainingResponse
import ru.gozerov.data.api.models.response.CustomClientServiceDTO
import ru.gozerov.data.api.models.response.CustomExerciseDTO
import ru.gozerov.data.api.models.response.CustomServiceDTO
import ru.gozerov.data.api.models.response.CustomTrainerTrainingDTO
import ru.gozerov.data.api.models.response.CustomTrainingDTO
import ru.gozerov.data.api.models.response.ExerciseDTO
import ru.gozerov.data.api.models.response.GetChatsResponseBody
import ru.gozerov.data.api.models.response.GetScheduleResponse
import ru.gozerov.data.api.models.response.ScheduledServiceDTO
import ru.gozerov.data.api.models.response.ScheduledTrainingDTO
import ru.gozerov.data.api.models.response.TrainerCover
import ru.gozerov.data.api.models.response.TrainerInfoResponse
import ru.gozerov.data.api.models.response.TrainerProfileDTO
import ru.gozerov.data.api.models.response.UserInfoResponse
import ru.gozerov.domain.models.Achievement
import ru.gozerov.domain.models.ChatCard
import ru.gozerov.domain.models.ChatItem
import ru.gozerov.domain.models.ClientCustomService
import ru.gozerov.domain.models.ClientInfo
import ru.gozerov.domain.models.CreateExerciseModel
import ru.gozerov.domain.models.CreateTrainerTrainingModel
import ru.gozerov.domain.models.CreateTrainingModel
import ru.gozerov.domain.models.CustomExercise
import ru.gozerov.domain.models.CustomService
import ru.gozerov.domain.models.CustomTrainerTraining
import ru.gozerov.domain.models.CustomTraining
import ru.gozerov.domain.models.Exercise
import ru.gozerov.domain.models.IdResponse
import ru.gozerov.domain.models.RegisterModel
import ru.gozerov.domain.models.ScheduleService
import ru.gozerov.domain.models.ScheduledTraining
import ru.gozerov.domain.models.TrainerCard
import ru.gozerov.domain.models.TrainerInfo
import ru.gozerov.domain.models.TrainerMainInfoDTO
import ru.gozerov.domain.models.Training
import ru.gozerov.domain.models.UserCard

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
    RegisterRequestBody(email, firstName, lastName, password, age, sex)

fun AchievementDTO.toAchievement() = Achievement(id, name, is_confirmed)

fun TrainerMainInfoDTO.toTrainerMainInfoRequestBody() =
    TrainerMainInfoRequestBody(age, email, experience, firstName, lastName, quote, sex)

fun CreateTrainingResponse.toCreatedTraining() = IdResponse(id)

fun ScheduledTrainingDTO.toCustomTraining() =
    CustomTraining(
        id,
        training_id,
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

fun GetChatsResponseBody.toChatCard() =
    ChatCard(
        id,
        first_name,
        last_name,
        if (photo_url == null) null else BASE_URL_FOR_PHOTO + photo_url,
        last_message,
        time_last_message
    )

fun ChatItemDTO.ChatMessageDTO.toChatMessage() =
    ChatItem.ChatMessage(id, is_to_user, message, service_id, time, trainer_id, user_id)

fun ChatItemDTO.DateMessageDTO.toChatDate() = ChatItem.DateMessage(message = date)

fun TrainerCover.toTrainerCard() = TrainerCard(
    id,
    if (photo_url == null) null else BASE_URL_FOR_PHOTO + photo_url,
    first_name,
    last_name,
    roles,
    age,
    sex,
    quote,
    specializations,
    experience
)

fun ClientCover.toUserCard() = UserCard(
    id,
    if (photo_url == null) null else BASE_URL_FOR_PHOTO + photo_url,
    first_name,
    last_name,
    sex,
    age
)

fun TrainerProfileDTO.toTrainerInfo() = TrainerInfo(
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

fun ClientProfileDTO.toClientInfo() =
    ClientInfo(
        id,
        first_name,
        last_name,
        age,
        email,
        if (photo_url == null) null else BASE_URL_FOR_PHOTO + photo_url,
        sex
    )

fun CreateExerciseModel.toCreateExerciseDTO() = CreateExerciseDTO(id, step)

fun CreateTrainingModel.toCreateTrainingDTO() = CreateTrainingDTO(
    description = description,
    name = name,
    exercises = exercises.map { model -> model.toCreateExerciseDTO() }
)

fun CustomTrainerTrainingDTO.toCustomTrainerTraining() =
    CustomTrainerTraining(
        id,
        description,
        name,
        exercises.map { it.toCustomExercise() },
        is_confirm,
        wants_public
    )

fun CreateTrainerTrainingModel.toCreateTrainerTrainingDTO() = CreateTrainerTrainingDTO(
    description,
    exercises.map { exercise -> exercise.toCreateExerciseDTO() },
    name,
    wantsPublic
)

fun CustomServiceDTO.toCustomService() = CustomService(
    id,
    is_payed,
    service,
    service_id,
    trainer_confirm,
    trainer_id,
    user.toUserCard(),
    user_confirm,
    user_id
)

fun ScheduledServiceDTO.toScheduledService() = ScheduleService(
    date,
    id,
    is_payed,
    schedule_id,
    service,
    service_id,
    time_start,
    time_end,
    trainer_confirm,
    trainer_id,
    user.toUserCard(),
    user_confirm,
    user_id
)

fun CustomClientServiceDTO.toClientCustomService() = ClientCustomService(
    id,
    is_payed,
    service,
    service_id,
    trainer_confirm,
    trainer_id,
    trainer.toTrainerCard(),
    user_confirm,
    user_id
)