package ru.gozerov.presentation.screens.trainer.diary.create_service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.CreateUserServiceUseCase
import ru.gozerov.domain.usecases.GetClientCardsUseCase
import ru.gozerov.domain.usecases.GetTrainerInfoUseCase
import ru.gozerov.presentation.screens.trainer.diary.create_service.models.CreateServiceEffect
import ru.gozerov.presentation.screens.trainer.diary.create_service.models.CreateServiceIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class CreateServiceViewModel @Inject constructor(
    private val getTrainerInfoUseCase: GetTrainerInfoUseCase,
    private val getClientCardsUseCase: GetClientCardsUseCase,
    private val createUserServiceUseCase: CreateUserServiceUseCase
): ViewModel() {

    private val _effect = MutableStateFlow<CreateServiceEffect>(CreateServiceEffect.None)
    val effect: StateFlow<CreateServiceEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: CreateServiceIntent) {
        viewModelScope.launch {
            when(intent) {
                is CreateServiceIntent.Reset -> {
                    _effect.emit(CreateServiceEffect.None)
                }
                is CreateServiceIntent.SearchUsers -> {
                    runCatchingNonCancellation {
                        getClientCardsUseCase.invoke(intent.query)
                    }
                        .onSuccess { flow ->
                            _effect.emit(CreateServiceEffect.LoadedUsers(flow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(CreateServiceEffect.Error(throwable.message.toString()))
                        }
                }
                is CreateServiceIntent.LoadServices -> {
                    runCatchingNonCancellation {
                        getTrainerInfoUseCase.invoke()
                    }
                        .map { result ->
                            result
                                .onSuccess { info ->
                                    _effect.emit(CreateServiceEffect.LoadedServices(info.services))
                                }
                                .onFailure { throwable ->
                                    _effect.emit(CreateServiceEffect.Error(throwable.message.toString()))
                                }
                        }
                }

                is CreateServiceIntent.CreateService -> {
                    runCatchingNonCancellation {
                        createUserServiceUseCase.invoke(intent.userId, intent.serviceId, intent.date, intent.timeStart, intent.timeEnd)
                    }
                        .onSuccess {
                            _effect.emit(CreateServiceEffect.CreatedService)
                        }
                        .onFailure { throwable ->
                            _effect.emit(CreateServiceEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}