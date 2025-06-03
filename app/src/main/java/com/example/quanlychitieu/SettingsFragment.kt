package com.example.quanlychitieu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        val settingsContainer: LinearLayout = view.findViewById(R.id.llSettingsContainer)

        fun addSettingItem(iconResId: Int, title: String, onClickAction: () -> Unit) {
            val settingItemView = inflater.inflate(R.layout.item_setting, settingsContainer, false) as CardView
            val ivIcon: ImageView = settingItemView.findViewById(R.id.ivSettingIcon)
            val tvTitle: TextView = settingItemView.findViewById(R.id.tvSettingTitle)

            ivIcon.setImageResource(iconResId)
            // Không áp dụng tint ở đây
            tvTitle.text = title

            settingItemView.setOnClickListener {
                onClickAction.invoke()
            }

            settingsContainer.addView(settingItemView)
        }

        // Các mục cài đặt khác
        addSettingItem(
            R.drawable.ic_settings,
            "Cài đặt cơ bản cho ứng dụng"
        ) {
            // Logic xử lý khi click
        }

        // DÒNG QUAN TRỌNG: Khi click vào mục Thay đổi màu chủ đề
        addSettingItem(
            R.drawable.ic_pen, // Hoặc icon bút/cọ bạn có
            "Thay đổi màu chủ đề của giao diện"
        ) {
//            val themeDialog = ThemeSelectionDialogFragment()
//            // Sử dụng parentFragmentManager để dialog được hiển thị bởi Activity chứa Fragment này
//            themeDialog.show(parentFragmentManager, "ThemeSelectionDialog")
        }

        addSettingItem(
            R.drawable.ic_gobal, // Biểu tượng quả địa cầu
            "Thay đổi ngôn ngữ của giao diện"
        ) {
            // Logic xử lý khi click
        }

        addSettingItem(
            R.drawable.ic_chart, // Biểu tượng biểu đồ
            "Đặt hạn mức cho chi tiêu"
        ) {
            // Logic xử lý khi click
        }

        addSettingItem(
            R.drawable.ic_bell, // Biểu tượng chuông
            "Cài đặt thời gian nhắc nhở nhập chi tiêu"
        ) {
            // Logic xử lý khi click
        }

        return view
    }
}