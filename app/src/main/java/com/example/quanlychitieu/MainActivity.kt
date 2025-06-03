package com.example.quanlychitieu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.quanlychitieu.databinding.ActivityMainBinding
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

// THÊM CÁC IMPORT FRAGMENT CÒN THIẾU CỦA BẠN VÀO ĐÂY
// import com.example.quanlychitieu.InputFragment
// import com.example.quanlychitieu.ReportCalendarFragment
// import com.example.quanlychitieu.BudgetDetailFragment // Đây có vẻ là Fragment cho Thống kê của bạn
// import com.example.quanlychitieu.ReportFragment // Nếu bạn có Fragment Report riêng



class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // DÒNG QUAN TRỌNG: Lấy chủ đề đã lưu và áp dụng TRƯỚC KHI gọi super.onCreate()
        val sharedPrefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val savedThemeMode = sharedPrefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(savedThemeMode)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DÒNG QUAN TRỌNG: tắt icon tint để giữ màu gốc PNG
        // Bạn đã có dòng này, đảm bảo nó được đặt ở đây sau setContentView
        binding.mainBottomNavigation.itemIconTintList = null
        binding.mainBottomNavigation.itemTextColor = null // Cũng tắt text color tint nếu bạn muốn giữ màu mặc định của text


        if (savedInstanceState == null) {
            // Đặt mục được chọn mặc định và load Fragment tương ứng
            binding.mainBottomNavigation.selectedItemId = R.id.nav_input
            loadFragment(InputFragment())
        }

        binding.mainBottomNavigation.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_input -> selectedFragment = InputFragment()
                R.id.nav_calendar -> selectedFragment = ReportCalendarFragment()
                R.id.nav_chart_bar -> selectedFragment = BudgetDetailFragment() // Sử dụng BudgetDetailFragment cho nav_chart_bar
                R.id.nav_setting -> selectedFragment = SettingsFragment()
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment)
            }
            true // Trả về true để cho biết sự kiện đã được xử lý
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment) // Sử dụng ID của FragmentContainerView của bạn
            // .addToBackStack(null) // Tùy chọn: nếu bạn muốn người dùng có thể quay lại Fragment trước bằng nút Back
            .commit()
    }

    // **********************************************
    // TRIỂN KHAI PHẦN CÒN THIẾU: HÀM TỪ INTERFACE ThemeSelectionListener
    // **********************************************
//    override fun onThemeSelected(themeMode: Int) {
//        // Lưu lựa chọn chủ đề vào SharedPreferences
//        val sharedPrefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
//        sharedPrefs.edit().putInt("theme_mode", themeMode).apply()
//
//        // Áp dụng chủ đề mới
//        AppCompatDelegate.setDefaultNightMode(themeMode)
//
//        // Tùy chọn: Khởi động lại Activity để áp dụng chủ đề ngay lập tức
//        // Điều này đảm bảo toàn bộ UI được vẽ lại với theme mới.
//        // Nếu không recreate(), bạn sẽ cần quản lý việc cập nhật UI trong từng component thủ công.
//        recreate()
//    }
}