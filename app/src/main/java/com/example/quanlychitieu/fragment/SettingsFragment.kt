package com.example.quanlychitieu.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import com.example.quanlychitieu.R

class SettingsFragment : Fragment() {

    // Thống nhất lại SharedPreferences với MainActivity
    private val PREFS_NAME = "theme_prefs"
    private val KEY_THEME = "selected_theme"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tìm nút "Thay đổi màu chủ đề" bằng ID từ file XML
        val settingChangeThemeButton = view.findViewById<CardView>(R.id.settingChangeTheme)
        settingChangeThemeButton.setOnClickListener {
            showThemeSelectionDialog()
        }

        // Gán sự kiện cho các nút khác (nếu cần)
        view.findViewById<CardView>(R.id.settingChangeLanguage).setOnClickListener {
            Toast.makeText(context, "Chức năng thay đổi ngôn ngữ", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<CardView>(R.id.settingSpendingLimit).setOnClickListener {
            Toast.makeText(context, "Chức năng đặt hạn mức", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<CardView>(R.id.settingReminder).setOnClickListener {
            Toast.makeText(context, "Chức năng cài đặt nhắc nhở", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showThemeSelectionDialog() {
        context?.let { ctx ->
            val themeOptions = resources.getStringArray(R.array.theme_options)
            // Lấy giá trị đã lưu để biết mục nào đang được chọn
            val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val savedThemeMode = sharedPreferences.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

            val checkedItem = when(savedThemeMode) {
                AppCompatDelegate.MODE_NIGHT_NO -> 0 // Sáng
                AppCompatDelegate.MODE_NIGHT_YES -> 1 // Tối
                else -> 2 // Hệ thống
            }

            AlertDialog.Builder(ctx)
                .setTitle("Chọn chủ đề")
                .setSingleChoiceItems(themeOptions, checkedItem) { dialog, which ->
                    val themeMode = when (which) {
                        0 -> AppCompatDelegate.MODE_NIGHT_NO
                        1 -> AppCompatDelegate.MODE_NIGHT_YES
                        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }
                    saveThemeAndRecreate(themeMode)
                    dialog.dismiss()
                }
                .setNegativeButton("Hủy", null)
                .create()
                .show()
        }
    }

    /**
     * Hợp nhất 2 hàm cũ thành 1 hàm duy nhất:
     * 1. Lưu lựa chọn mới vào SharedPreferences.
     * 2. Khởi động lại Activity để áp dụng theme.
     */
    private fun saveThemeAndRecreate(themeMode: Int) {
        // Lưu lựa chọn theme mới
        activity?.let {
            val sharedPreferences = it.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(KEY_THEME, themeMode).apply()
        }
        // Khởi động lại Activity để áp dụng theme
        activity?.recreate()
    }
}