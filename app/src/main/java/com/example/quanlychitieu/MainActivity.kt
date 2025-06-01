package com.example.quanlychitieu // Đảm bảo đúng package name của bạn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
// QUAN TRỌNG: Import đúng class Binding cho layout activity_main.xml MỚI
import com.example.quanlychitieu.databinding.ActivityMainBinding
// Import các Fragment của bạn
import com.example.quanlychitieu.InputFragment
import com.example.quanlychitieu.ReportCalendarFragment
import com.example.quanlychitieu.StatisticsFragment // << THÊM IMPORT CHO STATISTICSFRAGMENT
import com.example.quanlychitieu.R // << THÊM IMPORT CHO R (nếu chưa có và cần thiết)


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Sử dụng ActivityMainBinding cho layout activity_main.xml MỚI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // Đảm bảo R.id.nav_input là ID của item "Nhập vào" trong menu của bạn
            // và bạn đã tạo InputFragment
            binding.mainBottomNavigation.selectedItemId = R.id.nav_input // Chọn item "Nhập vào" làm mặc định
            loadFragment(InputFragment()) // Load InputFragment làm fragment ban đầu
        }

        binding.mainBottomNavigation.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_input -> { // ID của item "Nhập vào" trong menu
                    selectedFragment = InputFragment()
                }
                R.id.nav_calendar -> { // ID của item "Lịch" trong menu
                    selectedFragment = ReportCalendarFragment()
                }
                R.id.nav_chart_up -> { // SỬA Ở ĐÂY: ID của item "Thống kê"
                    // Thay R.id.nav_chart_up bằng ID thực tế từ file menu của bạn
                    selectedFragment = StatisticsFragment()
                }
                // TODO: Thêm các case cho các item khác nếu có (ví dụ: Báo cáo, Cài đặt)
                // R.id.nav_chart_bar -> selectedFragment = YourOtherReportFragment()
                // R.id.nav_settings -> selectedFragment = YourSettingsFragment()
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment)
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment) // Sử dụng ID của FragmentContainerView trong activity_main.xml mới
            .commit()
    }
}