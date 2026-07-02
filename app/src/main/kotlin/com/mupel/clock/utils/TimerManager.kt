package com.mupel.clock.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerManager(private val context: Context) {

    enum class TimerState {
        IDLE, RUNNING, PAUSED, COMPLETED
    }

    private val handler = Handler(Looper.getMainLooper())
    private var totalSeconds: Long = 0
    private var remainingSeconds: Long = 0
    private var isRunning = false

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState

    private val _timerTime = MutableStateFlow(0L)
    val timerTime: StateFlow<Long> = _timerTime

    fun setTimerDuration(hours: Int, minutes: Int, seconds: Int) {
        totalSeconds = (hours * 3600L + minutes * 60L + seconds)
        remainingSeconds = totalSeconds
        _timerTime.value = totalSeconds
    }

    fun startTimer() {
        if (_timerState.value == TimerState.IDLE || _timerState.value == TimerState.COMPLETED) {
            remainingSeconds = totalSeconds
        }
        isRunning = true
        _timerState.value = TimerState.RUNNING
        updateTimer()
    }

    fun pauseTimer() {
        isRunning = false
        _timerState.value = TimerState.PAUSED
    }

    fun resumeTimer() {
        isRunning = true
        _timerState.value = TimerState.RUNNING
        updateTimer()
    }

    fun stopTimer() {
        handler.removeCallbacksAndMessages(null)
        isRunning = false
        remainingSeconds = 0
        _timerState.value = TimerState.IDLE
        _timerTime.value = 0
    }

    fun resetTimer() {
        handler.removeCallbacksAndMessages(null)
        isRunning = false
        remainingSeconds = totalSeconds
        _timerState.value = TimerState.IDLE
        _timerTime.value = totalSeconds
    }

    fun addMinutes(minutes: Int) {
        remainingSeconds += (minutes * 60)
        _timerTime.value = remainingSeconds
    }

    fun getTotalSeconds(): Long = remainingSeconds
    fun getElapsedSeconds(): Long = totalSeconds - remainingSeconds
    fun getState(): TimerState = _timerState.value

    private fun updateTimer() {
        if (isRunning && remainingSeconds > 0) {
            remainingSeconds--
            _timerTime.value = remainingSeconds

            if (remainingSeconds <= 0) {
                _timerState.value = TimerState.COMPLETED
                onTimerComplete()
            } else {
                handler.postDelayed({ updateTimer() }, 1000)
            }
        }
    }

    private fun onTimerComplete() {
        // Timer completed
    }
}
