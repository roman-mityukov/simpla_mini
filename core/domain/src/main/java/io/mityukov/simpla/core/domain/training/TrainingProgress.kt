package io.mityukov.simpla.core.domain.training

import io.mityukov.simpla.core.domain.geo.Geolocation
import kotlin.time.Duration

enum class TrainingStatusEnum {
    NotStarted,
    Started,
    Completed,
}

data class TrainingProgress(
    val intervalProgress: List<IntervalProgress>,
    val currentDuration: Duration,
    val track: List<Geolocation>,
    val status: TrainingStatusEnum,
) {
    val launched: Boolean
        get() = status == TrainingStatusEnum.Started
    val completedIntervals: Int
        get() = intervalProgress.filter { it.progress == 1f }.size
}

data class IntervalProgress(
    val progress: Float,
    val currentDuration: Duration,
    val duration: Duration,
    val intervalType: IntervalType,
)