package ru.gozerov.presentation.screens.trainee.profile.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.gozerov.domain.repositories.TrainingRepository
import ru.gozerov.domain.utils.convertDateToUTC
import ru.gozerov.presentation.screens.trainee.profile.statistics.models.ClientStatisticsEffect
import ru.gozerov.presentation.screens.trainee.profile.statistics.models.ClientStatisticsIntent
import ru.gozerov.presentation.shared.utils.runCatchingNonCancellation
import javax.inject.Inject

@HiltViewModel
class ClientStatisticsViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository
) : ViewModel() {

    private val _effect = MutableStateFlow<ClientStatisticsEffect>(ClientStatisticsEffect.None)
    val effect: StateFlow<ClientStatisticsEffect>
        get() = _effect.asStateFlow()

    fun handleIntent(intent: ClientStatisticsIntent) {
        viewModelScope.launch {
            when (intent) {
                is ClientStatisticsIntent.Reset -> {
                    _effect.emit(ClientStatisticsEffect.None)
                }

                is ClientStatisticsIntent.LoadStatistics -> {
                    runCatchingNonCancellation {
                        trainingRepository.getProgress(
                            intent.query,
                            convertDateToUTC(intent.dateStart),
                            convertDateToUTC(intent.dateEnd)
                        )
                    }
                        .onSuccess { flow ->
                            _effect.emit(ClientStatisticsEffect.LoadedStatistics(flow))
                        }
                        .onFailure { throwable ->
                            _effect.emit(ClientStatisticsEffect.Error(throwable.message.toString()))
                        }
                }
            }
        }
    }


}