package io.mityukov.simpla.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mityukov.simpla.core.domain.training.TrainingController
import io.mityukov.simpla.core.domain.training.TrainingProgress
import io.mityukov.simpla.core.domain.training.TrainingStatus
import io.mityukov.simpla.log.logd
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TrainingState {
    data object Pending : TrainingState
    data class Progress(val data: TrainingProgress) : TrainingState
}

sealed interface TrainingEvent {
    data object SetupTraining : TrainingEvent
    data object SwitchTraining : TrainingEvent
}

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val trainingController: TrainingController,
) : ViewModel() {
    val stateFlow = trainingController.status.map {
        val state = when (it) {
            is TrainingStatus.Progress -> {
                TrainingState.Progress(it.data)
            }

            TrainingStatus.TrainingNotSelected -> {
                TrainingState.Pending
            }
        }
        logd("TrainingViewModel $state")
        state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = TrainingState.Pending,
    )

    fun add(event: TrainingEvent) {
        viewModelScope.launch {
            when (event) {
                TrainingEvent.SwitchTraining -> {
                    trainingController.switchTraining()
                }

                TrainingEvent.SetupTraining -> {
                    viewModelScope.launch {
                        trainingController.setupTraining()
                    }
                }
            }
        }
    }
}