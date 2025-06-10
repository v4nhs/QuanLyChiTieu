package com.example.quanlychitieu // Thay bằng package name của bạn

import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class ThemeTestActivity : AppCompatActivity() {

    private val PREFS_NAME = "theme_prefs"
    private val KEY_THEME = "selected_theme"
    private val THEME_LIGHT = 0
    private val THEME_DARK = 1
    private val THEME_SYSTEM = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_test)

        val button = findViewById<Button>(R.id.btnChangeThemeTest)
        button.setOnClickListener {
            showThemeSelectionDialog()
        }
    }

    private fun showThemeSelectionDialog() {
        val themeOptions = resources.getStringArray(R.array.theme_options)
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedTheme = sharedPreferences.getInt(KEY_THEME, THEME_SYSTEM)

        AlertDialog.Builder(this)
            .setTitle("Chọn chủ đề")
            .setSingleChoiceItems(themeOptions, savedTheme) { dialog, which ->
                val newThemeMode = when (which) {
                    THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                // So sánh với chế độ hiện tại
                if (AppCompatDelegate.getDefaultNightMode() != newThemeMode) {
                    AppCompatDelegate.setDefaultNightMode(newThemeMode)
                    // Lưu lựa chọn
                    sharedPreferences.edit().putInt(KEY_THEME, which).apply()
                    // Khởi động lại Activity để áp dụng theme
                    recreate()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Hủy", null)
            .create()
            .show()
    }
}