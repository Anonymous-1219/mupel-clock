package com.mupel.clock.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mupel.clock.databinding.FragmentStopwatchBinding
import com.mupel.clock.ui.adapters.LapAdapter
import com.mupel.clock.utils.StopwatchManager
import kotlinx.coroutines.launch

class StopwatchFragment : Fragment() {

    private var _binding: FragmentStopwatchBinding? = null
    private val binding get() = _binding!!

    private lateinit var stopwatchManager: StopwatchManager
    private lateinit var lapAdapter: LapAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stopwatchManager = StopwatchManager()
        setupLapRecyclerView()
        setupUI()
        observeStopwatchState()
    }

    private fun setupLapRecyclerView() {
        lapAdapter = LapAdapter()
        binding.lapsList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            adapter = lapAdapter
        }
    }

    private fun setupUI() {
        binding.stopwatchStart.setOnClickListener { startStopwatch() }
        binding.stopwatchPause.setOnClickListener { pauseStopwatch() }
        binding.stopwatchResume.setOnClickListener { resumeStopwatch() }
        binding.stopwatchReset.setOnClickListener { resetStopwatch() }
        binding.stopwatchLap.setOnClickListener { recordLap() }
    }

    private fun startStopwatch() {
        stopwatchManager.start()
    }

    private fun pauseStopwatch() {
        stopwatchManager.pause()
    }

    private fun resumeStopwatch() {
        stopwatchManager.resume()
    }

    private fun resetStopwatch() {
        stopwatchManager.reset()
        lapAdapter.clearLaps()
    }

    private fun recordLap() {
        val lap = stopwatchManager.recordLap()
        lapAdapter.addLap(lap)
        binding.lapsList.scrollToPosition(0)
    }

    private fun observeStopwatchState() {
        lifecycleScope.launch {
            stopwatchManager.elapsedTime.collect { time ->
                updateDisplay(time)
            }
        }

        lifecycleScope.launch {
            stopwatchManager.state.collect { state ->
                updateUI(state)
            }
        }
    }

    private fun updateDisplay(milliseconds: Long) {
        val totalSeconds = milliseconds / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val ms = milliseconds % 1000

        binding.stopwatchTime.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        binding.stopwatchMs.text = String.format(".%03d", ms)
    }

    private fun updateUI(state: StopwatchManager.StopwatchState) {
        when (state) {
            StopwatchManager.StopwatchState.IDLE -> {
                binding.stopwatchStart.visibility = View.VISIBLE
                binding.stopwatchPause.visibility = View.GONE
                binding.stopwatchResume.visibility = View.GONE
                binding.stopwatchReset.visibility = View.GONE
                binding.stopwatchLap.visibility = View.GONE
            }
            StopwatchManager.StopwatchState.RUNNING -> {
                binding.stopwatchStart.visibility = View.GONE
                binding.stopwatchPause.visibility = View.VISIBLE
                binding.stopwatchResume.visibility = View.GONE
                binding.stopwatchReset.visibility = View.GONE
                binding.stopwatchLap.visibility = View.VISIBLE
            }
            StopwatchManager.StopwatchState.PAUSED -> {
                binding.stopwatchStart.visibility = View.GONE
                binding.stopwatchPause.visibility = View.GONE
                binding.stopwatchResume.visibility = View.VISIBLE
                binding.stopwatchReset.visibility = View.VISIBLE
                binding.stopwatchLap.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
