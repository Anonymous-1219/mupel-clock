package com.mupel.clock.utils

import android.app.Activity
import android.os.Build
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

object SystemUIHelper {

    fun setupSystemUI(activity: Activity) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)

        val insetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        insetsController?.isAppearanceLightStatusBars = false
        insetsController?.isAppearanceLightNavigationBars = false

        activity.window.statusBarColor = activity.getColor(android.R.color.transparent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.navigationBarColor = activity.getColor(android.R.color.transparent)
        }

        activity.actionBar?.hide()
    }
}
