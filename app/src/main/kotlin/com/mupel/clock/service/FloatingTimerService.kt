package com.mupel.clock.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.mupel.clock.R
import com.mupel.clock.utils.TimerManager

class FloatingTimerService : Service() {

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
    }

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: FrameLayout
    private lateinit var timerManager: TimerManager
    private val handler = Handler(Looper.getMainLooper())

    private var isExpanded = false
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        timerManager = TimerManager(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: ACTION_START

        when (action) {
            ACTION_START -> showFloatingTimer()
            ACTION_PAUSE -> timerManager.pauseTimer()
            ACTION_STOP -> hideFloatingTimer()
        }

        return START_STICKY
    }

    private fun showFloatingTimer() {
        if (::floatingView.isInitialized && floatingView.parent != null) {
            return
        }

        floatingView = FrameLayout(this).apply {
            setBackgroundResource(R.drawable.floating_timer_bg)
        }

        val params = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE

            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            width = 120
            height = 120
            gravity = Gravity.TOP or Gravity.END
            x = 0
            y = 300
        }

        windowManager.addView(floatingView, params)
        startTimerUpdates()
    }

    private fun startTimerUpdates() {
        handler.post(object : Runnable {
            override fun run() {
                val totalSeconds = timerManager.getTotalSeconds()
                if (totalSeconds > 0) {
                    val minutes = totalSeconds / 60
                    val seconds = totalSeconds % 60

                    floatingView.findViewById<TextView>(R.id.floating_time)?.text =
                        String.format("%02d:%02d", minutes, seconds)

                    handler.postDelayed(this, 1000)
                } else {
                    hideFloatingTimer()
                }
            }
        })
    }

    private fun hideFloatingTimer() {
        handler.removeCallbacksAndMessages(null)
        if (::floatingView.isInitialized && floatingView.parent != null) {
            windowManager.removeView(floatingView)
        }
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        try {
            if (::floatingView.isInitialized && floatingView.parent != null) {
                windowManager.removeView(floatingView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
