package io.mityukov.simpla.core.data.repository.training

import io.mityukov.simpla.core.data.repository.RepositoryFailure
import io.mityukov.simpla.core.data.repository.RepositoryResult
import io.mityukov.simpla.core.domain.training.Interval
import io.mityukov.simpla.core.domain.training.Training
import io.mityukov.simpla.log.logw
import io.mityukov.simpla.network.retrofit.RemoteTraining
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class TrainingRepositoryImpl @Inject constructor(
    private val remoteDataSource: TrainingRemoteDataSource
) : TrainingRepository {
    private val mutex = Mutex()
    private val cache = HashMap<String, Training>()

    override suspend fun getTraining(trainingId: String): RepositoryResult<Training> =
        mutex.withLock {
            val cachedTraining = cache[trainingId]
            if (cachedTraining != null) {
                return@withLock RepositoryResult.Success(cachedTraining)
            }

            try {
                val remoteTraining = remoteDataSource.getTraining(trainingId)
                val training = mapTrainingRemoteToDomain(remoteTraining)
                cache[trainingId] = training
                return@withLock RepositoryResult.Success(training)
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                logw("TrainingRepositoryImpl error $e")
                return@withLock RepositoryResult.Failure(RepositoryFailure.IO)
            }
        }

    private fun mapTrainingRemoteToDomain(remote: RemoteTraining): Training {
        return Training(
            id = remote.id.toString(),
            duration = remote.totalTime.seconds,
            intervals = remote.intervals.map { remoteInterval ->
                Interval(
                    title = remoteInterval.title,
                    duration = remoteInterval.time.seconds
                )
            },
        )
    }
}