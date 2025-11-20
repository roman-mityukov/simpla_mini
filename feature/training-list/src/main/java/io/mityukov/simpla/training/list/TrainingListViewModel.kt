package io.mityukov.simpla.training.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.mityukov.simpla.core.data.repository.RepositoryResult
import io.mityukov.simpla.core.data.repository.training.SelectedTrainingRepository
import io.mityukov.simpla.core.data.repository.training.TrainingRepository
import io.mityukov.simpla.core.domain.training.Training
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

internal sealed interface TrainingListState {
    data class Default(val error: Boolean = false, val pending: Boolean = false) : TrainingListState
    data object DataReceived : TrainingListState
}

internal sealed interface TrainingListEvent {
    data class LoadTraining(val trainingId: String) : TrainingListEvent
}

@HiltViewModel
internal class TrainingListViewModel @Inject constructor(
    private val trainingRepository: TrainingRepository,
    private val selectedTrainingRepository: SelectedTrainingRepository,
) : ViewModel() {
    private val mutableStateFlow =
        MutableStateFlow<TrainingListState>(TrainingListState.Default())
    val stateFlow = mutableStateFlow.asStateFlow()

    fun add(event: TrainingListEvent) {
        when (event) {
            is TrainingListEvent.LoadTraining -> {
                mutableStateFlow.update {
                    TrainingListState.Default(pending = true)
                }
                viewModelScope.launch {
                    val result = trainingRepository.getTraining(event.trainingId)

                    when (result) {
                        is RepositoryResult.Failure<*> -> {
                            mutableStateFlow.update {
                                TrainingListState.Default(error = true)
                            }
                        }

                        is RepositoryResult.Success<*> -> {
                            mutableStateFlow.update {
                                TrainingListState.DataReceived
                            }
                            selectedTrainingRepository.setSelectedTraining(result.data as Training)
                            delay(1000)
                            mutableStateFlow.update {
                                TrainingListState.Default()
                            }
                        }
                    }
                }
            }
        }
    }
}