package ru.gozerov.presentation.screens.login.login_choice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.LoginThroughVkUseCase
import ru.gozerov.presentation.screens.login.login_choice.models.LoginEffect
import ru.gozerov.presentation.screens.login.login_choice.models.LoginIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginThroughVkUseCase: LoginThroughVkUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<LoginEffect>(LoginEffect.None)
    val effect: StateFlow<LoginEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: LoginIntent) {
        viewModelScope.launch {
            when (intent) {
                is LoginIntent.Navigate -> {
                    _effect.emit(LoginEffect.None)
                }

                is LoginIntent.LoginThroughVK -> {
                    runCatchingNonCancellation {
                        loginThroughVkUseCase.invoke()
                    }
                        .map { flow ->
                            flow.collect { result ->
                                result
                                    .onSuccess { response ->
                                        _effect.emit(
                                            LoginEffect.SuccessLogin(response.token, response.id)
                                        )
                                    }
                                    .onFailure {
                                        _effect.emit(LoginEffect.Error())
                                    }
                            }
                        }
                }
            }
        }
    }

}