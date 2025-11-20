package io.mityukov.simpla.core.data.repository.training

import io.mityukov.simpla.core.common.di.DispatcherIO
import io.mityukov.simpla.network.retrofit.RemoteApi
import io.mityukov.simpla.network.retrofit.RemoteInterval
import io.mityukov.simpla.network.retrofit.RemoteIntervalType
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
//            val request = remoteApi.getTraining()
//            val response = request.execute()
//            val remoteTraining = response.body()!!
//            remoteTraining
            RemoteTraining(id = "a", intervals = listOf(
                RemoteInterval(id = "b", intervalType = RemoteIntervalType.Walk, duration = 10000),
                RemoteInterval(id = "c", intervalType = RemoteIntervalType.Run, duration = 20000),
                RemoteInterval(id = "d", intervalType = RemoteIntervalType.Walk, duration = 10000),
            ))
        }
}