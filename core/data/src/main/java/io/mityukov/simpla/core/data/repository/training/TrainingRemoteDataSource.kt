package io.mityukov.simpla.core.data.repository.training

import io.mityukov.simpla.network.retrofit.RemoteTraining


interface TrainingRemoteDataSource {
    suspend fun getTraining(trainingId: String): RemoteTraining
}