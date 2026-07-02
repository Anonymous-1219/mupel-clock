package com.mupel.clock.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mupel.clock.R
import com.mupel.clock.databinding.FragmentTimerBinding
import com.mupel.clock.service.FloatingTimerService
import com.mupel.clock.utils.TimerManager
import kotlinx.coroutines.launch

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var timerManager: TimerManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timerManager = TimerManager(requireContext())

        setupUI()
        observeTimerState()
    }

    private fun setupUI() {
        binding.hoursUp.setOnClickListener { incrementHours() }
        binding.hoursDown.setOnClickListener { decrementHours() }
        binding.minutesUp.setOnClickListener { incrementMinutes() }
        binding.minutesDown.setOnClickListener { decrementMinutes() }
        binding.secondsUp.setOnClickListener { incrementSeconds() }
        binding.secondsDown.setOnClickListener { decrementSeconds() }

        binding.timerStart.setOnClickListener { startTimer() }
        binding.timerPause.setOnClickListener { pauseTimer() }
        binding.timerStop.setOnClickListener { stopTimer() }
        binding.timerReset.setOnClickListener { resetTimer() }
        binding.timerAdd5.setOnClickListener { addFiveMinutes() }
        binding.timerRestart.setOnClickListener { restartTimer() }
        binding.timerAdd5End.setOnClickListener { addFiveMinutesAfterCompletion() }

        updateDisplay()
    }

    private fun incrementHours() {
        val current = binding.timerHours.text.toString().toIntOrNull() ?: 0
        binding.timerHours.setText((if (current < 23) current + 1 else 0).toString())
    }

    private fun decrementHours() {
        val current = binding.timerHours.text.toString().toIntOrNull() ?: 0
        binding.timerHours.setText((if (current > 0) current - 1 else 23).toString())
    }

    private fun incrementMinutes() {
        val current = binding.timerMinutes.text.toString().toIntOrNull() ?: 0
        binding.timerMinutes.setText((if (current < 59) current + 1 else 0).toString())
    }

    private fun decrementMinutes() {
        val current = binding.timerMinutes.text.toString().toIntOrNull() ?: 0
        binding.timerMinutes.setText((if (current > 0) current - 1 else 59).toString())
    }

    private fun incrementSeconds() {
        val current = binding.timerSeconds.text.toString().toIntOrNull() ?: 0
        binding.timerSeconds.setText((if (current < 59) current + 1 else 0).toString())
    }

    private fun decrementSeconds() {
        val current = binding.timerSeconds.text.toString().toIntOrNull() ?: 0
        binding.timerSeconds.setText((if (current > 0) current - 1 else 59).toString())
    }

    private fun startTimer() {
        val hours = binding.timerHours.text.toString().toIntOrNull() ?: 0
        val minutes = binding.timerMinutes.text.toString().toIntOrNull() ?: 0
        val seconds = binding.timerSeconds.text.toString().toIntOrNull() ?: 0
        timerManager.setTimerDuration(hours, minutes, seconds)
        timerManager.startTimer()
        startFloatingTimer()
    }

    private fun pauseTimer() {
        timerManager.pauseTimer()
    }

    private fun stopTimer() {
        timerManager.stopTimer()
        stopFloatingTimer()
    }

    private fun resetTimer() {
        timerManager.resetTimer()
        updateDisplay()
    }

    private fun addFiveMinutes() {
        timerManager.addMinutes(5)
    }

    private fun restartTimer() {
        timerManager.startTimer()
        startFloatingTimer()
    }

    private fun addFiveMinutesAfterCompletion() {
        timerManager.addMinutes(5)
        timerManager.resumeTimer()
        startFloatingTimer()
    }

    private fun observeTimerState() {
        lifecycleScope.launch {
            timerManager.timerState.collect { state ->
                updateDisplay()
                updateUI()
            }
        }

        lifecycleScope.launch {
            timerManager.timerTime.collect { time ->
                updateDisplay()
                updateProgressBar()
            }
        }
    }

    private fun updateDisplay() {
        val totalSeconds = timerManager.getTotalSeconds()
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        binding.timerTime.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        binding.timerHours.setText(hours.toString())
        binding.timerMinutes.setText(minutes.toString())
        binding.timerSeconds.setText(seconds.toString())
    }

    private fun updateUI() {
        val state = timerManager.getState()
        when (state) {
            TimerManager.TimerState.IDLE -> {
                binding.timerInputGroup.visibility = View.VISIBLE
                binding.timerControls.visibility = View.VISIBLE
                binding.timerActions.visibility = View.GONE
                binding.timerCompleted.visibility = View.GONE
            }
            TimerManager.TimerState.RUNNING -> {
                binding.timerInputGroup.visibility = View.GONE
                binding.timerControls.visibility = View.GONE
                binding.timerActions.visibility = View.VISIBLE
                binding.timerCompleted.visibility = View.GONE
            }
            TimerManager.TimerState.PAUSED -> {
                binding.timerInputGroup.visibility = View.GONE
                binding.timerControls.visibility = View.GONE
                binding.timerActions.visibility = View.VISIBLE
                binding.timerCompleted.visibility = View.GONE
            }
            TimerManager.TimerState.COMPLETED -> {
                binding.timerInputGroup.visibility = View.GONE
                binding.timerControls.visibility = View.GONE
                binding.timerActions.visibility = View.GONE
                binding.timerCompleted.visibility = View.VISIBLE
            }
        }
    }

    private fun updateProgressBar() {
        val totalSeconds = timerManager.getTotalSeconds()
        val elapsedSeconds = timerManager.getElapsedSeconds()

        if (totalSeconds > 0) {
            val progress = ((elapsedSeconds.toFloat() / totalSeconds) * 100).toInt()
            binding.progressBar.progress = progress
        }
    }

    private fun startFloatingTimer() {
        val intent = Intent(requireContext(), FloatingTimerService::class.java)
        intent.action = FloatingTimerService.ACTION_START
        requireContext().startService(intent)
    }

    private fun stopFloatingTimer() {
        val intent = Intent(requireContext(), FloatingTimerService::class.java)
        intent.action = FloatingTimerService.ACTION_STOP
        requireContext().startService(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
