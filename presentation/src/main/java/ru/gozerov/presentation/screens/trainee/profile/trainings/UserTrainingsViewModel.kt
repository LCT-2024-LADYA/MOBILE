package ru.gozerov.presentation.screens.trainee.profile.trainings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.repositories.TrainingRepository
import ru.gozerov.presentation.screens.trainee.profile.trainings.models.UserTrainingEffect
import ru.gozerov.presentation.screens.trainee.profile.trainings.models.UserTrainingsIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class UserTrainingsViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository
) : ViewModel() {

    private val _effect = MutableStateFlow<UserTrainingEffect>(UserTrainingEffect.None)
    val effect: StateFlow<UserTrainingEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: UserTrainingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is UserTrainingsIntent.Reset -> {
                    _effect.emit(UserTrainingEffect.None)
                }

                is UserTrainingsIntent.LoadTrainings -> {
                    runCatchingNonCancellation {
                        trainingRepository.getSimpleUserTraining(intent.query)
                    }
                        .onSuccess { flow ->
                            _effect.emit(UserTrainingEffect.LoadedTrainings(flow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(UserTrainingEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }

}