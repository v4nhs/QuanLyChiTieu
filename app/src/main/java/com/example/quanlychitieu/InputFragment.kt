package com.example.quanlychitieu // Đảm bảo đúng package name của bạn

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.quanlychitieu.R // QUAN TRỌNG: Đảm bảo bạn đã import R của project
import com.example.quanlychitieu.databinding.FragmentInputBinding // ViewBinding cho fragment_input.xml
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!! // Sử dụng !! để đảm bảo binding không null sau onCreateView

    private val calendarInstance: Calendar = Calendar.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View { // Kiểu trả về là View vì binding.root không bao giờ null ở đây
        _binding = FragmentInputBinding.inflate(inflater, container, false)

        // Các thiết lập ban đầu và listeners
        setupInitialState()      // Gọi hàm thiết lập trạng thái ban đầu
        setupClickListeners()    // Gọi hàm thiết lập các listener

        return binding.root // Trả về root view của binding
    }

    // Hàm để thiết lập trạng thái ban đầu cho các View
    private fun setupInitialState() {
        // Mặc định chọn "TIỀN CHI"
        setButtonSelected(binding.btnTienChi, true)
        setButtonSelected(binding.btnTienThu, false)
        // Sử dụng string resource cho hint text, ví dụ:
        binding.tilThuNhap.hint = getString(R.string.so_tien_chi_hint)
        binding.tvDanhMucChiTitle.text = getString(R.string.danh_muc_chi_title)
        // Nếu bạn muốn ô ngày hiển thị ngày hiện tại khi fragment được tạo:
        // updateDateInView()
        // Hoặc nếu bạn muốn hiển thị "Chọn ngày" làm hint mờ trong etNgay,
        // đảm bảo trong fragment_input.xml, etNgay có android:hint="Chọn ngày"
        // và không gọi setText ở đây.
    }

    // Hàm để thiết lập các onClickListener
    private fun setupClickListeners() {
        binding.btnTienChi.setOnClickListener {
            setButtonSelected(binding.btnTienChi, true)
            setButtonSelected(binding.btnTienThu, false)
            binding.tilThuNhap.hint = getString(R.string.so_tien_chi_hint)
            binding.tvDanhMucChiTitle.text = getString(R.string.danh_muc_chi_title)
        }

        binding.btnTienThu.setOnClickListener {
            setButtonSelected(binding.btnTienThu, true)
            setButtonSelected(binding.btnTienChi, false)
            binding.tilThuNhap.hint = getString(R.string.so_tien_thu_hint)
            binding.tvDanhMucChiTitle.text = getString(R.string.danh_muc_thu_title)
        }

        binding.etNgay.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnNhapKhoanChi.setOnClickListener {
            val ngay = binding.etNgay.text.toString()
            val ghiChu = binding.etGhiChu.text.toString()
            val soTien = binding.etThuNhap.text.toString()

            if (ngay.isBlank()) { // Thêm kiểm tra cho ngày
                Toast.makeText(requireContext(), "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (soTien.isBlank()) {
                Toast.makeText(requireContext(), getString(R.string.vui_long_nhap_so_tien), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val message = "Ngày: $ngay, Ghi chú: $ghiChu, Số tiền: $soTien"
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            // TODO: Xử lý logic nhập khoản chi/thu thực tế
        }
    }

    private fun showDatePickerDialog() {
        val year = calendarInstance.get(Calendar.YEAR)
        val month = calendarInstance.get(Calendar.MONTH)
        val day = calendarInstance.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
                requireContext(), // Sử dụng requireContext() trong Fragment
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        calendarInstance.set(selectedYear, selectedMonth, selectedDayOfMonth)
                        updateDateInView()
                },
                year,
                month,
                day
        )
        // datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Tùy chọn: không cho chọn ngày tương lai
        datePickerDialog.show()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy" // Có thể lấy định dạng này từ string resources
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etNgay.setText(sdf.format(calendarInstance.time))
    }

    private fun setButtonSelected(button: MaterialButton, isSelected: Boolean) {
        val context = button.context // Hoặc requireContext()
        if (isSelected) {
            button.backgroundTintList =
                    ContextCompat.getColorStateList(context, R.color.app_teal)
            button.setTextColor(ContextCompat.getColor(context, R.color.app_teal_text_on_solid))
            button.strokeColor = null
            button.strokeWidth = 0
        } else {
            button.backgroundTintList =
                    ContextCompat.getColorStateList(context, android.R.color.transparent)
            button.setTextColor(ContextCompat.getColor(context, R.color.app_teal_text_on_outlined))
            button.strokeColor = ContextCompat.getColorStateList(context, R.color.app_teal)
            button.strokeWidth = (1 * resources.displayMetrics.density).toInt() // 1dp
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Quan trọng: giải phóng binding để tránh memory leak
    }
}