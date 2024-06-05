package ru.gozerov.presentation.screens.login.login_trainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.LoginAsTrainerUseCase
import ru.gozerov.presentation.screens.login.login_trainer.models.LoginTrainerEffect
import ru.gozerov.presentation.screens.login.login_trainer.models.LoginTrainerIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class LoginTrainerViewModel @Inject constructor(
    private val loginAsTrainerUseCase: LoginAsTrainerUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<LoginTrainerEffect>(LoginTrainerEffect.None)
    val effect: StateFlow<LoginTrainerEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: LoginTrainerIntent) {
        viewModelScope.launch {
            when (intent) {
                is LoginTrainerIntent.Reset -> {
                    _effect.emit(LoginTrainerEffect.None)
                }

                is LoginTrainerIntent.Login -> {
                    runCatchingNonCancellation {
                        loginAsTrainerUseCase.invoke(intent.email, intent.password)
                    }
                        .map { flow ->
                            flow.collect { result ->
                                result
                                    .onSuccess {
                                        _effect.emit(LoginTrainerEffect.SuccessLogin)
                                    }
                                    .onFailure { throwable ->
                                        _effect.emit(
                                            LoginTrainerEffect.Error(throwable.message ?: "Error")
                                        )
                                    }
                            }
                        }
                }
            }
        }
    }

}