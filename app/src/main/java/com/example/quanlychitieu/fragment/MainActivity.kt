package com.example.quanlychitieu.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.quanlychitieu.databinding.ActivityMainBinding
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.quanlychitieu.R
import com.example.quanlychitieu.fragment.ReportPieChartFragment

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPrefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val savedThemeMode = sharedPrefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(savedThemeMode)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainBottomNavigation.itemIconTintList = null
        binding.mainBottomNavigation.itemTextColor = null


        if (savedInstanceState == null) {
            binding.mainBottomNavigation.selectedItemId = R.id.nav_input
            loadFragment(InputFragment())
        }

        binding.mainBottomNavigation.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_input -> selectedFragment = InputFragment()
                R.id.nav_calendar -> selectedFragment = ReportCalendarFragment()
                R.id.nav_chart_up -> selectedFragment = ReportPieChartFragment()
                R.id.nav_chart_bar -> selectedFragment = BudgetDetailFragment()
                R.id.nav_setting -> selectedFragment = SettingsFragment()
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment)
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .commit()
    }
}