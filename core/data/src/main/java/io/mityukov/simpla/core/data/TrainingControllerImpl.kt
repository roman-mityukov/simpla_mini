package io.mityukov.simpla.core.data

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.mityukov.simpla.core.common.di.DispatcherIO
import io.mityukov.geo.tracking.core.data.repository.geo.GeolocationProvider
import io.mityukov.simpla.core.domain.geo.Geolocation
import io.mityukov.simpla.core.domain.training.IntervalProgress
import io.mityukov.simpla.core.domain.training.Training
import io.mityukov.simpla.core.domain.training.TrainingController
import io.mityukov.simpla.core.domain.training.TrainingProgress
import io.mityukov.simpla.core.domain.training.TrainingStatus
import io.mityukov.simpla.core.domain.training.TrainingLaunchStatus
import io.mityukov.simpla.core.data.repository.training.ForegroundTrainingService
import io.mityukov.simpla.core.data.repository.training.SelectedTrainingRepository
import io.mityukov.simpla.core.data.repository.training.TrainingTimer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private fun Training.toEmptyProgress(): TrainingStatus.Progress {
    val intervalProgress = this.intervals.map {
        IntervalProgress(
            progress = 0f,
            duration = it.duration,
            currentDuration = 0.seconds,
            title = it.title,
        )
    }
    return TrainingStatus.Progress(
        data = TrainingProgress(
            intervalProgress = intervalProgress,
            currentDuration = 0.seconds,
            totalDuration = this.duration,
            track = listOf(),
            status = TrainingLaunchStatus.NotStarted,
        )
    )
}

fun Training.toListIntervalProgress(trainingDuration: Duration): List<IntervalProgress> {
    var remainingTrainingDuration = trainingDuration
    val result = this.intervals.map { interval ->
        val (progress, currentDuration) = if (remainingTrainingDuration.isNegative()) {
            Pair(0f, 0.seconds)
        } else {
            val result = if (remainingTrainingDuration >= interval.duration) {
                Pair(1f, interval.duration)
            } else {
                Pair(
                    (remainingTrainingDuration / interval.duration).toFloat(),
                    remainingTrainingDuration
                )
            }
            remainingTrainingDuration -= interval.duration
            result
        }

        IntervalProgress(
            progress = progress,
            currentDuration = currentDuration,
            duration = interval.duration,
            title = interval.title,
        )
    }
    return result
}

class TrainingControllerImpl @Inject constructor(
    @param:ApplicationContext private val applicationContext: Context,
    @param:DispatcherIO private val coroutineDispatcher: CoroutineDispatcher,
    private val selectedTrainingRepository: SelectedTrainingRepository,
    private val geolocationProvider: GeolocationProvider,
    private val soundManager: SoundManager,
) : TrainingController {
    private lateinit var timer: TrainingTimer
    private val coroutineScope = CoroutineScope(coroutineDispatcher)
    private var timerSubscriptionJob: Job? = null
    private var geolocationSubscription: Job? = null
    private val geolocations = mutableListOf<Geolocation>()

    private val mutableStateFlow =
        MutableStateFlow(
            if (selectedTrainingRepository.getSelectedTraining() != null) {
                selectedTrainingRepository.getSelectedTraining()!!.toEmptyProgress()
            } else {
                TrainingStatus.TrainingNotSelected
            }
        )
    override val status: StateFlow<TrainingStatus> = mutableStateFlow.asStateFlow()

    override suspend fun switchTraining() {
        if (timerSubscriptionJob != null) {
            stop()
        } else {
            start()
        }
    }

    override suspend fun setupTraining() {
        val currentStatus = status.first()
        val selectedTraining = selectedTrainingRepository.getSelectedTraining()
        if (currentStatus is TrainingStatus.TrainingNotSelected && selectedTraining != null) {
            mutableStateFlow.update {
                selectedTraining.toEmptyProgress()
            }
        }
    }

    private suspend fun start() {
        geolocations.clear()
        mutableStateFlow.update {
            selectedTrainingRepository.getSelectedTraining()!!.toEmptyProgress()
        }
        launchTraining()
    }

    private suspend fun stop() {
        timer.stop()
        geolocationSubscription?.cancel()
        geolocationSubscription = null
        timerSubscriptionJob?.cancel()
        timerSubscriptionJob = null
        stopForegroundService()

        val currentStatus = status.first() as TrainingStatus.Progress

        mutableStateFlow.update {
            TrainingStatus.Progress(
                data = currentStatus.data.copy(status = TrainingLaunchStatus.Completed)
            )
        }
    }

    private suspend fun launchTraining() {
        startForegroundService()

        timer = TrainingTimer()
        timerSubscriptionJob = coroutineScope.launch {
            timer.events.collect { timerDuration ->
                val currentProgress = (status.first() as TrainingStatus.Progress).data
                val training = selectedTrainingRepository.getSelectedTraining()!!

                if (timerDuration.seconds > training.duration) {
                    coroutineScope.launch {
                        stop()
                        soundManager.playBeep()
                    }
                } else {
                    mutableStateFlow.update {
                        val newProgress = currentProgress.copy(
                            intervalProgress = training.toListIntervalProgress(timerDuration.seconds),
                            currentDuration = timerDuration.seconds,
                            status = TrainingLaunchStatus.Started,
                        )
                        if (newProgress.completedIntervals > currentProgress.completedIntervals) {
                            soundManager.playBeep()
                        }
                        TrainingStatus.Progress(
                            data = newProgress
                        )
                    }
                }
            }
        }

        geolocationSubscription = coroutineScope.launch {
            geolocationProvider.locationUpdates(
                3.seconds,
                10f,
            )
                .collect { result ->
                    val currentStatus = status.first()
                    result.location?.let {
                        geolocations.add(
                            Geolocation(
                                latitude = it.latitude,
                                longitude = it.longitude,
                                altitude = it.altitude,
                                speed = it.speed,
                                time = it.time,
                            )
                        )

                        mutableStateFlow.update {
                            TrainingStatus.Progress(
                                data = (currentStatus as TrainingStatus.Progress).data.copy(
                                    track = geolocations,
                                )
                            )
                        }
                    }
                }
        }

        val currentStatus = status.first() as TrainingStatus.Progress
        mutableStateFlow.update {
            TrainingStatus.Progress(
                data = currentStatus.data.copy(status = TrainingLaunchStatus.Started)
            )
        }

        timer.start()
    }

    private fun startForegroundService() {
        val intent = Intent(applicationContext, ForegroundTrainingService::class.java)
        applicationContext.startService(intent)
    }

    private fun stopForegroundService() {
        applicationContext.stopService(
            Intent(
                applicationContext,
                ForegroundTrainingService::class.java
            )
        )
    }
}
