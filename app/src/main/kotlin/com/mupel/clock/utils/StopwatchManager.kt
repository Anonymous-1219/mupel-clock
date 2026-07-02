package com.mupel.clock.utils

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class LapTime(val lapNumber: Int, val lapTime: Long, val totalTime: Long)

class StopwatchManager {

    enum class StopwatchState {
        IDLE, RUNNING, PAUSED
    }

    private val handler = Handler(Looper.getMainLooper())
    private var startTime = 0L
    private var pausedTime = 0L
    private var lapNumber = 0
    private val laps = mutableListOf<LapTime>()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime

    private val _state = MutableStateFlow(StopwatchState.IDLE)
    val state: StateFlow<StopwatchState> = _state

    fun start() {
        startTime = System.currentTimeMillis() - pausedTime
        _state.value = StopwatchState.RUNNING
        updateTime()
    }

    fun pause() {
        pausedTime = System.currentTimeMillis() - startTime
        _state.value = StopwatchState.PAUSED
        handler.removeCallbacksAndMessages(null)
    }

    fun resume() {
        start()
    }

    fun reset() {
        handler.removeCallbacksAndMessages(null)
        startTime = 0L
        pausedTime = 0L
        lapNumber = 0
        laps.clear()
        _state.value = StopwatchState.IDLE
        _elapsedTime.value = 0L
    }

    fun recordLap(): LapTime {
        lapNumber++
        val totalElapsed = System.currentTimeMillis() - startTime
        val lapTime = if (laps.isEmpty()) {
            totalElapsed
        } else {
            totalElapsed - laps.sumOf { it.totalTime }
        }

        val lap = LapTime(lapNumber, lapTime, totalElapsed)
        laps.add(lap)
        return lap
    }

    fun getLaps(): List<LapTime> = laps.toList()

    private fun updateTime() {
        if (_state.value == StopwatchState.RUNNING) {
            val elapsed = System.currentTimeMillis() - startTime
            _elapsedTime.value = elapsed
            handler.postDelayed({ updateTime() }, 10)
        }
    }
}
