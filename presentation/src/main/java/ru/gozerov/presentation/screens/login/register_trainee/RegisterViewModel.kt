package ru.gozerov.presentation.screens.login.register_trainee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.RegisterUseCase
import ru.gozerov.presentation.screens.login.register_trainee.models.RegisterEffect
import ru.gozerov.presentation.screens.login.register_trainee.models.RegisterIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<RegisterEffect>(RegisterEffect.None)
    val effect: StateFlow<RegisterEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: RegisterIntent) {
        viewModelScope.launch {
            when (intent) {
                is RegisterIntent.Navigate -> {
                    _effect.emit(RegisterEffect.None)
                }

                is RegisterIntent.Register -> {
                    runCatchingNonCancellation {
                        registerUseCase.invoke(intent.email, intent.password, intent.firstName, intent.lastName, intent.age, intent.sex)
                    }
                        .map { flow ->
                            flow.collect { result ->
                                result
                                    .onSuccess {
                                        _effect.emit(RegisterEffect.SuccessLoginTrainee)
                                    }
                                    .onFailure { throwable ->
                                        _effect.emit(
                                            RegisterEffect.Error(throwable.message ?: "Error")
                                        )
                                    }
                            }
                        }
                }
            }
        }
    }
}