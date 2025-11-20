package io.mityukov.data

import io.mityukov.geo.tracking.core.data.toListIntervalProgress
import io.mityukov.simpla.core.domain.training.Interval
import io.mityukov.simpla.core.domain.training.IntervalProgress
import io.mityukov.simpla.core.domain.training.IntervalType
import io.mityukov.simpla.core.domain.training.Training
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class TrainingControllerTest {
    @Test
    fun toListIntervalProgress() {
        val training = Training(
            id = "a",
            intervals = listOf(
                Interval(
                    id = "b",
                    intervalType = IntervalType.Walk,
                    duration = 10.seconds,
                ),
                Interval(
                    id = "c",
                    intervalType = IntervalType.Run,
                    duration = 20.seconds,
                ),
                Interval(
                    id = "d",
                    intervalType = IntervalType.Walk,
                    duration = 10.seconds,
                )
            )
        )
        val fixture = listOf(
            IntervalProgress(1f, 10.seconds, IntervalType.Walk),
            IntervalProgress(0.5f, 20.seconds, IntervalType.Run),
            IntervalProgress(0f, 10.seconds, IntervalType.Walk),
        )

        val listIntervalProgress = training.toListIntervalProgress(20.seconds)
        assert(listIntervalProgress == fixture)
    }
}