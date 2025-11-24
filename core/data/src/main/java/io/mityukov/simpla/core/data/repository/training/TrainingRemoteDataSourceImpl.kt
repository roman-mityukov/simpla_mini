package io.mityukov.simpla.core.data.repository.training

import io.mityukov.simpla.core.common.di.DispatcherIO
import io.mityukov.simpla.network.retrofit.RemoteApi
import io.mityukov.simpla.network.retrofit.RemoteTraining
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrainingRemoteDataSourceImpl @Inject constructor(
    private val remoteApi: RemoteApi,
    @param:DispatcherIO private val coroutineDispatcher: CoroutineDispatcher,
) : TrainingRemoteDataSource {
    override suspend fun getTraining(trainingId: String): RemoteTraining =
        withContext(coroutineDispatcher) {
            val request = remoteApi.getTraining()
            val response = request.execute()
            val remoteResult = response.body()!!
            remoteResult.timer
        }
}