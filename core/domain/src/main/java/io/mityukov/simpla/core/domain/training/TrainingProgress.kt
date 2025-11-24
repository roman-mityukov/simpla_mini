package io.mityukov.simpla.core.domain.training

import io.mityukov.simpla.core.domain.geo.Geolocation
import kotlin.time.Duration

enum class TrainingLaunchStatus {
    NotStarted,
    Started,
    Completed,
}

data class TrainingProgress(
    val intervalProgress: List<IntervalProgress>,
    val currentDuration: Duration,
    val totalDuration: Duration,
    val track: List<Geolocation>,
    val status: TrainingLaunchStatus,
) {
    val launched: Boolean
        get() = status == TrainingLaunchStatus.Started
    val completedIntervals: Int
        get() = intervalProgress.filter { it.progress == 1f }.size
}

data class IntervalProgress(
    val progress: Float,
    val currentDuration: Duration,
    val duration: Duration,
    val title: String,
)