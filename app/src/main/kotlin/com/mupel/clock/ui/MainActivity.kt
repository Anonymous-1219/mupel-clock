package com.mupel.clock.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mupel.clock.R
import com.mupel.clock.databinding.ActivityMainBinding
import com.mupel.clock.ui.fragments.ClockFragment
import com.mupel.clock.ui.fragments.StopwatchFragment
import com.mupel.clock.ui.fragments.TimerFragment
import com.mupel.clock.utils.SystemUIHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemUIHelper.setupSystemUI(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupBottomNavigation()
    }

    private fun setupViewPager() {
        viewPager = binding.viewPager
        viewPager.adapter = PagerAdapter(this)
        viewPager.offscreenPageLimit = 3

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNav.selectedItemId = when (position) {
                    0 -> R.id.nav_clock
                    1 -> R.id.nav_timer
                    2 -> R.id.nav_stopwatch
                    else -> R.id.nav_clock
                }
            }
        })
    }

    private fun setupBottomNavigation() {
        bottomNav = binding.bottomNavigation
        bottomNav.setOnItemSelectedListener { item ->
            val position = when (item.itemId) {
                R.id.nav_clock -> 0
                R.id.nav_timer -> 1
                R.id.nav_stopwatch -> 2
                else -> 0
            }
            viewPager.currentItem = position
            true
        }
    }

    private inner class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ClockFragment()
                1 -> TimerFragment()
                2 -> StopwatchFragment()
                else -> ClockFragment()
            }
        }
    }
}
