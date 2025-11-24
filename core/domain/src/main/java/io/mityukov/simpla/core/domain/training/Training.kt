package io.mityukov.simpla.core.domain.training

import kotlin.time.Duration

enum class IntervalType {
    Walk, Run
}

data class Training(val id: String, val duration: Duration, val intervals: List<Interval>)

data class Interval(val title: String, val duration: Duration)