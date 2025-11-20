package io.mityukov.simpla.core.data.repository.training

import io.mityukov.simpla.core.domain.training.Training
import javax.inject.Inject

class SelectedTrainingRepositoryImpl @Inject constructor() : SelectedTrainingRepository {
    private var selectedTraining: Training? = null
    override fun getSelectedTraining(): Training? {
        return selectedTraining
    }

    override fun setSelectedTraining(training: Training) {
        selectedTraining = training
    }
}