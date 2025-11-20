package io.mityukov.simpla.core.domain.training

import kotlinx.coroutines.flow.StateFlow

sealed interface TrainingStatus {
    data object TrainingNotSelected : TrainingStatus
    data class Progress(val data: TrainingProgress) : TrainingStatus
}

interface TrainingController {
    val status: StateFlow<TrainingStatus>
    suspend fun switchTraining()
    suspend fun setupTraining()
}