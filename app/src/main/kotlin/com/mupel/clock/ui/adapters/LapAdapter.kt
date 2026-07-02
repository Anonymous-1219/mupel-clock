package com.mupel.clock.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mupel.clock.databinding.ItemLapBinding
import com.mupel.clock.utils.LapTime

class LapAdapter : RecyclerView.Adapter<LapAdapter.LapViewHolder>() {

    private val laps = mutableListOf<LapTime>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapViewHolder {
        val binding = ItemLapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LapViewHolder, position: Int) {
        holder.bind(laps[position])
    }

    override fun getItemCount(): Int = laps.size

    fun addLap(lap: LapTime) {
        laps.add(0, lap)
        notifyItemInserted(0)
    }

    fun clearLaps() {
        laps.clear()
        notifyDataSetChanged()
    }

    inner class LapViewHolder(private val binding: ItemLapBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lap: LapTime) {
            val minutes = (lap.lapTime / 1000) / 60
            val seconds = ((lap.lapTime / 1000) % 60)
            val ms = (lap.lapTime % 1000)

            val lapTimeText = String.format("%02d:%02d.%03d", minutes, seconds, ms)
            binding.lapNumber.text = "Lap ${lap.lapNumber}"
            binding.lapTime.text = lapTimeText

            val totalMinutes = (lap.totalTime / 1000) / 60
            val totalSeconds = ((lap.totalTime / 1000) % 60)
            val totalMs = (lap.totalTime % 1000)
            val totalTimeText = String.format("%02d:%02d.%03d", totalMinutes, totalSeconds, totalMs)
            binding.totalTime.text = totalTimeText
        }
    }
}
