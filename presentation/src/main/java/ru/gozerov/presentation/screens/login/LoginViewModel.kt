package ru.gozerov.presentation.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.LoginAsTraineeUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginAsTraineeUseCase: LoginAsTraineeUseCase
) : ViewModel(){

    fun login(token: String, id: Long, email: String?) {
        viewModelScope.launch {
            loginAsTraineeUseCase.invoke(token, id, email).collect {

            }
        }
    }

}