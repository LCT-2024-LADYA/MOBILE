package ru.gozerov.presentation.screens.login.login_trainee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.LoginAsTraineeUseCase
import ru.gozerov.presentation.screens.login.login_trainee.models.LoginTraineeEffect
import ru.gozerov.presentation.screens.login.login_trainee.models.LoginTraineeIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class LoginTraineeViewModel @Inject constructor(
    private val loginAsTraineeUseCase: LoginAsTraineeUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<LoginTraineeEffect>(LoginTraineeEffect.None)
    val effect: StateFlow<LoginTraineeEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: LoginTraineeIntent) {
        viewModelScope.launch {
            when (intent) {
                is LoginTraineeIntent.Reset -> {
                    _effect.emit(LoginTraineeEffect.None)
                }

                is LoginTraineeIntent.Login -> {
                    runCatchingNonCancellation {
                        loginAsTraineeUseCase.invoke(intent.email, intent.password)
                    }
                        .map { flow ->
                            flow.collect { result ->
                                result
                                    .onSuccess {
                                        _effect.emit(LoginTraineeEffect.SuccessLogin)
                                    }
                                    .onFailure {
                                        _effect.emit(
                                            LoginTraineeEffect.Error(it.message ?: "Error")
                                        )
                                    }
                            }
                        }
                }
            }
        }
    }


}