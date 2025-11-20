package io.mityukov.simpla.core.data.repository.training

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Timer
import kotlin.concurrent.timer

internal class TrainingTimer(val period: Long = 1000L) {
    private var duration: Int = 0
    private val _events = MutableStateFlow(duration)
    val events: StateFlow<Int> = _events.asStateFlow()
    private var timer: Timer? = null

    fun start() {
        duration = 0
        timer = timer(period = period) {
            duration++
            _events.update {
                duration
            }
        }
    }

    fun stop() {
        timer?.cancel()
        timer = null
    }
}
