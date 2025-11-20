package io.mityukov.simpla.core.data.repository.training

import io.mityukov.simpla.core.domain.training.Training

interface SelectedTrainingRepository {
    fun getSelectedTraining(): Training?
    fun setSelectedTraining(training: Training)
}