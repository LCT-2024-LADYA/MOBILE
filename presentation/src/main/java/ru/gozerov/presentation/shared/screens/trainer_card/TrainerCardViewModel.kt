package ru.gozerov.presentation.shared.screens.trainer_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.usecases.GetTrainerProfileUseCase
import ru.gozerov.presentation.shared.screens.trainer_card.models.TrainerCardEffect
import ru.gozerov.presentation.shared.screens.trainer_card.models.TrainerCardIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class TrainerCardViewModel @Inject constructor(
    private val getTrainerProfileUseCase: GetTrainerProfileUseCase
) : ViewModel() {

    private val _effect = MutableStateFlow<TrainerCardEffect>(TrainerCardEffect.None)
    val effect: StateFlow<TrainerCardEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: TrainerCardIntent) {
        viewModelScope.launch {
            when (intent) {
                is TrainerCardIntent.Reset -> {
                    _effect.emit(TrainerCardEffect.None)
                }

                is TrainerCardIntent.LoadProfile -> {
                    runCatchingNonCancellation {
                        getTrainerProfileUseCase.invoke(intent.trainerId)
                    }
                        .onSuccess { trainer ->
                            _effect.emit(TrainerCardEffect.LoadedProfile(trainer))
                        }
                        .onFailure { throwable ->
                            _effect.emit(TrainerCardEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}