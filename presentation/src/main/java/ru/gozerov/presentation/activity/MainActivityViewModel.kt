package ru.gozerov.presentation.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.RefreshTokenUseCase
import ru.gozerov.presentation.activity.models.MainActivityEffect
import ru.gozerov.presentation.activity.models.MainActivityIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val refreshTokenUseCase: RefreshTokenUseCase
): ViewModel() {

    private val _effect = MutableStateFlow<MainActivityEffect>(MainActivityEffect.None)
    val effect: StateFlow<MainActivityEffect>
        get() = _effect.asStateFlow()

    init {
        handle(MainActivityIntent.CheckAuth)
    }

    fun handle(intent: MainActivityIntent) {
        viewModelScope.launch {
            when(intent) {
                is MainActivityIntent.CheckAuth -> {
                    runCatchingNonCancellation {
                        refreshTokenUseCase.invoke()
                    }
                        .onSuccess { result ->
                            _effect.emit(MainActivityEffect.AuthResult(!result.isAuth, result.isClient))
                        }
                        .onFailure {  throwable ->
                            _effect.emit(MainActivityEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}