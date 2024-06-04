package ru.gozerov.presentation.screens.login.verify_email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.LoginAsTraineeUseCase
import ru.gozerov.presentation.screens.login.verify_email.models.VerifyEmailEffect
import ru.gozerov.presentation.screens.login.verify_email.models.VerifyEmailIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val loginAsTraineeUseCase: LoginAsTraineeUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<VerifyEmailEffect>(VerifyEmailEffect.None)
    val effect: StateFlow<VerifyEmailEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: VerifyEmailIntent) {
        viewModelScope.launch {
            when (intent) {
                is VerifyEmailIntent.VerifyEmail -> {
                    runCatchingNonCancellation {
                        loginAsTraineeUseCase.invoke(intent.accessToken, intent.vkId, intent.email)
                    }
                        .map { flow ->
                            flow.collect { result ->
                                result
                                    .onSuccess {
                                        _effect.emit(VerifyEmailEffect.SuccessLogin)
                                    }
                                    .onFailure {
                                        _effect.emit(VerifyEmailEffect.Error())
                                    }
                            }
                        }
                }
            }
        }
    }


}