package com.example.quanlychitieu // Hoặc package name của bạn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anychart.AnyChart // THÊM IMPORT
import com.anychart.chart.common.dataentry.DataEntry // THÊM IMPORT
import com.anychart.chart.common.dataentry.ValueDataEntry // THÊM IMPORT
import com.anychart.enums.Align // THÊM IMPORT
import com.anychart.enums.LegendLayout // THÊM IMPORT
import com.example.quanlychitieu.R // Đảm bảo import R đúng
import com.example.quanlychitieu.databinding.FragmentStatisticsBinding


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val view = binding.root

        setupPieChart() // Gọi hàm để vẽ biểu đồ

        // TODO: Thêm code xử lý cho bộ chọn Tháng/Năm và cập nhật biểu đồ/danh sách
        // binding.spinnerMonth.setOnClickListener { ... }
        // binding.spinnerYear.setOnClickListener { ... }

        return view
    }

    private fun setupPieChart() {
        // (Tùy chọn) Hiển thị ProgressBar khi biểu đồ đang tải
        // binding.anyChartViewStatistics.setProgressBar(binding.progressBarChart) // Bạn cần thêm ProgressBar vào XML nếu dùng

        val pie = AnyChart.pie()

        // Tạo dữ liệu mẫu (sau này bạn sẽ lấy từ database)
        val dataEntries: MutableList<DataEntry> = ArrayList()
        // Ví dụ:
        val totalIncome = 50000000.0 // Tổng thu (Lấy từ DB)
        val totalExpense = 20000000.0 // Tổng chi (Lấy từ DB)

        if (totalIncome > 0) {
            dataEntries.add(ValueDataEntry("Tổng Thu", totalIncome))
        }
        if (totalExpense > 0) {
            dataEntries.add(ValueDataEntry("Tổng Chi", totalExpense))
        }

        // Nếu không có dữ liệu thì hiển thị thông báo hoặc biểu đồ trống
        if (dataEntries.isEmpty()){
            dataEntries.add(ValueDataEntry("Không có dữ liệu", 1)) // Dữ liệu giả để biểu đồ hiển thị
            pie.labels().enabled(true) // Bật nhãn
            pie.labels().format("{%X}: {%Value}{groupsSeparator:.}") // Định dạng nhãn
            pie.palette(arrayOf("#CCCCCC")) // Màu xám cho không có dữ liệu
        } else {
            // Đặt màu cho các phần của biểu đồ
            // Màu xanh lá cho "Tổng Thu", màu đỏ cho "Tổng Chi"
            // Đảm bảo thứ tự màu khớp với thứ tự bạn add dataEntries
            val colors = mutableListOf<String>()
            if (totalIncome > 0) colors.add(getString(R.color.income_green_hex)) // Lấy mã hex từ colors.xml
            if (totalExpense > 0) colors.add(getString(R.color.expense_red_hex)) // Lấy mã hex từ colors.xml
            pie.palette(colors.toTypedArray())
        }


        pie.data(dataEntries)

        // pie.title("Thống kê Thu Chi") // Bỏ tiêu đề của biểu đồ nếu bạn đã có TextView tiêu đề ở trên

        // Tùy chỉnh để giống Donut Chart (biểu đồ tròn khuyết)
        pie.innerRadius("60%") // Điều chỉnh % để thay đổi độ lớn của lỗ ở giữa

        // Tắt legend mặc định của biểu đồ (vì bạn đã có legend riêng bằng View)
        pie.legend().enabled(false)

        // Tùy chỉnh tooltip (khi nhấn vào một phần của biểu đồ)
        // pie.tooltip().titleFormat("{%X}").format("Giá trị: {%Value}{groupsSeparator:.}đ")

        binding.anyChartViewStatistics.setChart(pie)
    }

    // Bạn sẽ cần định nghĩa các màu này trong res/values/colors.xml dưới dạng string
    // ví dụ: <string name="income_green_hex">#4CAF50</string>
    //        <string name="expense_red_hex">#F44336</string>
    // Lý do dùng getString là vì R.color.income_green trả về Int, còn palette cần String mã hex

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}