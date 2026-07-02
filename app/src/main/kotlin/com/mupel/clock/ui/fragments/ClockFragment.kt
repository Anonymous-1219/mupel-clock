package com.mupel.clock.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mupel.clock.databinding.FragmentClockBinding
import com.mupel.clock.utils.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

class ClockFragment : Fragment() {

    private var _binding: FragmentClockBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            updateClock()
            handler.postDelayed(this, 1000)
        }
    }

    private var is24HourFormat = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = PreferenceManager(requireContext())
        is24HourFormat = prefs.is24HourFormat()

        setupFormatButtons()
        updateClock()
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(updateRunnable, 1000)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupFormatButtons() {
        binding.format12h.apply {
            isSelected = !is24HourFormat
            setOnClickListener {
                is24HourFormat = false
                PreferenceManager(requireContext()).setTimeFormat(false)
                updateButtons()
                updateClock()
            }
        }

        binding.format24h.apply {
            isSelected = is24HourFormat
            setOnClickListener {
                is24HourFormat = true
                PreferenceManager(requireContext()).setTimeFormat(true)
                updateButtons()
                updateClock()
            }
        }

        updateButtons()
    }

    private fun updateButtons() {
        binding.format12h.isSelected = !is24HourFormat
        binding.format24h.isSelected = is24HourFormat
    }

    private fun updateClock() {
        val now = Calendar.getInstance()

        val timeFormat = if (is24HourFormat) "HH:mm" else "hh:mm"
        val sdf = SimpleDateFormat(timeFormat, Locale.US)
        binding.digitalTime.text = sdf.format(now.time)

        if (!is24HourFormat) {
            val ampm = if (now.get(Calendar.HOUR_OF_DAY) >= 12) "PM" else "AM"
            binding.timeFormat.text = ampm
            binding.timeFormat.visibility = View.VISIBLE
        } else {
            binding.timeFormat.visibility = View.GONE
        }

        val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US)
        binding.currentDate.text = dateFormat.format(now.time)
    }
}
