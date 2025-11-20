package io.mityukov.simpla.core.domain.training

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class IntervalType {
    Walk, Run
}

data class Training(val id: String, val intervals: List<Interval>) {
    val duration: Duration by lazy {
        intervals.fold(
            0L,
            { acc, interval -> acc + interval.duration.inWholeSeconds }).seconds
    }
}

data class Interval(val id: String, val intervalType: IntervalType, val duration: Duration)