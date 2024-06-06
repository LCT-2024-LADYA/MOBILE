package ru.gozerov.presentation.screens.trainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.CreateAchievementUseCase
import ru.gozerov.domain.usecases.CreateServiceUseCase
import ru.gozerov.domain.usecases.DeleteAchievementUseCase
import ru.gozerov.domain.usecases.DeleteTrainerServiceUseCase
import ru.gozerov.domain.usecases.GetRolesUseCase
import ru.gozerov.domain.usecases.GetSpecializationsUseCase
import ru.gozerov.domain.usecases.GetTrainerInfoUseCase
import ru.gozerov.domain.usecases.UpdateTrainerPhotoUseCase
import ru.gozerov.domain.usecases.UpdateTrainerProfileUseCase
import ru.gozerov.presentation.screens.trainer.models.TrainerProfileEffect
import ru.gozerov.presentation.screens.trainer.models.TrainerProfileIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class TrainerProfileViewModel @Inject constructor(
    private val getRolesUseCase: GetRolesUseCase,
    private val getSpecializationsUseCase: GetSpecializationsUseCase,
    private val createAchievementUseCase: CreateAchievementUseCase,
    private val deleteAchievementUseCase: DeleteAchievementUseCase,
    private val createServiceUseCase: CreateServiceUseCase,
    private val deleteServiceUseCase: DeleteTrainerServiceUseCase,
    private val getTrainerInfoUseCase: GetTrainerInfoUseCase,
    private val updateTrainerProfileUseCase: UpdateTrainerProfileUseCase,
    private val updateTrainerPhotoUseCase: UpdateTrainerPhotoUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<TrainerProfileEffect>(TrainerProfileEffect.None)
    val effect: StateFlow<TrainerProfileEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: TrainerProfileIntent) {
        viewModelScope.launch {
            when (intent) {
                is TrainerProfileIntent.Reset -> {
                    _effect.emit(TrainerProfileEffect.None)
                }

                is TrainerProfileIntent.GetRoles -> {
                    runCatchingNonCancellation {
                        getRolesUseCase.invoke()
                    }
                        .map { result ->
                            result
                                .onSuccess { roles ->
                                    _effect.emit(TrainerProfileEffect.LoadedRoles(roles))
                                }
                                .onFailure { throwable ->
                                    _effect.emit(TrainerProfileEffect.Error(throwable.message.toString()))
                                }
                        }
                }

                is TrainerProfileIntent.GetSpecializations -> {
                    runCatchingNonCancellation {
                        getSpecializationsUseCase.invoke()
                    }
                        .map { result ->
                            result
                                .onSuccess { specializations ->
                                    _effect.emit(
                                        TrainerProfileEffect.LoadedSpecializations(
                                            specializations
                                        )
                                    )
                                }
                                .onFailure { throwable ->
                                    _effect.emit(TrainerProfileEffect.Error(throwable.message.toString()))
                                }
                        }
                }

                is TrainerProfileIntent.GetMainInfo -> {
                    runCatchingNonCancellation {
                        getTrainerInfoUseCase.invoke()
                    }
                        .map { result ->
                            result
                                .onSuccess { info ->
                                    _effect.emit(TrainerProfileEffect.LoadedProfile(info))
                                }
                                .onFailure { throwable ->
                                    _effect.emit(TrainerProfileEffect.Error(throwable.message.toString()))
                                }
                        }
                }

                is TrainerProfileIntent.UpdateProfile -> {
                    runCatchingNonCancellation {
                        updateTrainerProfileUseCase.invoke(
                            intent.trainerMainInfoDTO,
                            intent.roles,
                            intent.specializations
                        )
                    }
                        .map { flow ->
                            flow.collect { result ->
                                result
                                    .onSuccess {
                                        _effect.emit(TrainerProfileEffect.SuccessUpdatedProfile)
                                    }
                                    .onFailure { throwable ->
                                        _effect.emit(TrainerProfileEffect.Error(throwable.message.toString()))
                                    }
                            }
                        }
                }

                is TrainerProfileIntent.CreateAchievement -> {
                    runCatchingNonCancellation {
                        createAchievementUseCase.invoke(intent.name)
                    }
                        .map { result ->
                            result
                                .onSuccess { id ->
                                    _effect.emit(
                                        TrainerProfileEffect.SuccessCreatedAchievement(
                                            id,
                                            intent.name
                                        )
                                    )
                                }
                                .onFailure { throwable ->
                                    _effect.emit(TrainerProfileEffect.Error(throwable.message.toString()))
                                }
                        }
                }

                is TrainerProfileIntent.RemoveAchievement -> {
                    runCatchingNonCancellation {
                        deleteAchievementUseCase.invoke(intent.id)
                    }
                        .map { result ->
                            result
                                .onFailure { throwable ->
                                    _effect.emit(TrainerProfileEffect.Error(throwable.message.toString()))
                                }
                        }
                }

                is TrainerProfileIntent.CreateService -> {
                    runCatchingNonCancellation {
                        createServiceUseCase.invoke(intent.name, intent.price)
                    }
                        .map { result ->
                            result
                                .onSuccess { id ->
                                    _effect.emit(
                                        TrainerProfileEffect.SuccessCreatedService(
                                            id,
                                            intent.name,
                                            intent.price
                                        )
                                    )
                                }
                                .onFailure { throwable ->
                                    _effect.emit(TrainerProfileEffect.Error(throwable.message.toString()))
                                }
                        }
                }

                is TrainerProfileIntent.RemoveService -> {
                    runCatchingNonCancellation {
                        deleteServiceUseCase.invoke(intent.id)
                    }
                        .map { result ->
                            result
                                .onFailure { throwable ->
                                    _effect.emit(TrainerProfileEffect.Error(throwable.message.toString()))
                                }
                        }
                }

                is TrainerProfileIntent.UpdatePhoto -> {
                    runCatchingNonCancellation {
                        updateTrainerPhotoUseCase.invoke(intent.uri)
                    }
                        .map { result ->
                            result
                                .onSuccess {
                                    _effect.emit(TrainerProfileEffect.SuccessPhotoUpload)
                                }
                                .onFailure { throwable ->
                                    _effect.emit(TrainerProfileEffect.Error(throwable.message.toString()))
                                }
                        }
                }

            }
        }
    }

}