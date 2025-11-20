package io.mityukov.simpla.core.data.repository.training

import io.mityukov.simpla.core.data.repository.RepositoryResult
import io.mityukov.simpla.core.domain.training.Training

interface TrainingRepository {
    suspend fun getTraining(trainingId: String): RepositoryResult<Training>
}