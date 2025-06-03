package com.example.quanlychitieu // Hoặc package của bạn

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quanlychitieu.adapter.CategoryAdapter
import com.example.quanlychitieu.database.TransactionEntity
import com.example.quanlychitieu.databinding.FragmentInputBinding
import com.example.quanlychitieu.model.CategoryItem
import com.example.quanlychitieu.viewmodel.InputViewModel
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    private val calendarInstance: Calendar = Calendar.getInstance()
    private var selectedDate: Date? = null

    private lateinit var inputViewModel: InputViewModel
    private var isExpense: Boolean = true // Mặc định là chi

    private var selectedCategory: CategoryItem? = null
    private lateinit var categoryAdapter: CategoryAdapter

    // --- KHAI BÁO HAI DANH SÁCH DANH MỤC RIÊNG BIỆT ---
    private lateinit var expenseCategories: List<CategoryItem>
    private lateinit var incomeCategories: List<CategoryItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        inputViewModel = ViewModelProvider(this).get(InputViewModel::class.java)

        loadCategoryLists() // Tải danh sách danh mục
        setupInitialState()
        setupClickListeners()
        setupCategoryRecyclerView() // Setup RecyclerView với danh sách ban đầu (chi)

        return binding.root
    }

    // --- TẢI DỮ LIỆU CHO CẢ HAI DANH SÁCH DANH MỤC ---
    private fun loadCategoryLists() {
        // DANH MỤC CHI (Sử dụng các icon bạn đã có cho "Tiền Chi")
        expenseCategories = listOf(
            CategoryItem("Ăn uống", R.drawable.ic_food),
            CategoryItem("Hàng ngày", R.drawable.ic_daily),
            CategoryItem("Quần áo", R.drawable.ic_clothes),
            CategoryItem("Mỹ phẩm", R.drawable.ic_beauty),
            CategoryItem("Giao lưu", R.drawable.ic_social),
            CategoryItem("Y tế", R.drawable.ic_medical),
            CategoryItem("Khác", R.drawable.ic_other)
        )

        // DANH MỤC THU (Sử dụng các icon bạn đã có cho "Tiền Thu")
        incomeCategories = listOf(
            CategoryItem("Lương", R.drawable.ic_salary),
            CategoryItem("Thưởng", R.drawable.ic_bonus),
            CategoryItem("Thu nhập phụ", R.drawable.ic_side_income),
            CategoryItem("Đầu tư", R.drawable.ic_investment),
            CategoryItem("Khác", R.drawable.ic_other)
        )
    }

    private fun setupCategoryRecyclerView() {
        // Chọn danh sách ban đầu dựa trên isExpense
        val initialCategories = if (isExpense) expenseCategories else incomeCategories
        categoryAdapter = CategoryAdapter(initialCategories) { category ->
            selectedCategory = category
        }
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 3) // Hoặc số cột bạn muốn
            adapter = categoryAdapter
        }
    }

    // --- HÀM CẬP NHẬT DANH SÁCH ICON KHI CHUYỂN TAB ---
    private fun updateCategoriesForCurrentType() {
        selectedCategory = null // Reset lựa chọn danh mục cũ
        val currentCategories = if (isExpense) expenseCategories else incomeCategories
        // Tạo adapter mới hoặc cập nhật dữ liệu cho adapter hiện tại
        // Cách đơn giản là tạo adapter mới:
        categoryAdapter = CategoryAdapter(currentCategories) { category ->
            selectedCategory = category
        }
        binding.rvCategories.adapter = categoryAdapter

        // HOẶC, nếu CategoryAdapter của bạn có hàm setData(List<CategoryItem>):
        // categoryAdapter.setData(currentCategories)
        // categoryAdapter.clearSelection() // Thêm hàm này vào adapter để bỏ chọn item cũ
    }

    private fun setupInitialState() {
        setButtonSelected(binding.btnTienChi, true)
        setButtonSelected(binding.btnTienThu, false)
        isExpense = true
        binding.tilThuNhap.hint = getString(R.string.so_tien_chi_hint) // Đảm bảo có string resource này
        binding.tvDanhMucChiTitle.text = getString(R.string.danh_muc_chi_title) // Đảm bảo có string resource này
        updateCategoriesForCurrentType() // Hiển thị danh mục chi ban đầu
    }

    private fun setupClickListeners() {
        binding.btnTienChi.setOnClickListener {
            setButtonSelected(binding.btnTienChi, true)
            setButtonSelected(binding.btnTienThu, false)
            isExpense = true
            binding.tilThuNhap.hint = getString(R.string.so_tien_chi_hint)
            binding.tvDanhMucChiTitle.text = getString(R.string.danh_muc_chi_title) // Hoặc "Danh Mục Chi"
            binding.btnNhapKhoanChi.text = "Nhập Khoản Chi" // Cập nhật text nút
            updateCategoriesForCurrentType() // Cập nhật RecyclerView
        }

        binding.btnTienThu.setOnClickListener {
            setButtonSelected(binding.btnTienThu, true)
            setButtonSelected(binding.btnTienChi, false)
            isExpense = false
            binding.tilThuNhap.hint = getString(R.string.so_tien_thu_hint) // Đảm bảo có string resource này
            binding.tvDanhMucChiTitle.text = getString(R.string.danh_muc_thu_title) // Hoặc "Danh Mục Thu"
            binding.btnNhapKhoanChi.text = "Nhập Khoản Thu" // Cập nhật text nút
            updateCategoriesForCurrentType() // Cập nhật RecyclerView
        }

        binding.etNgay.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnNhapKhoanChi.setOnClickListener {
            saveTransaction()
        }
    }

    private fun saveTransaction() {
        val ghiChu = binding.etGhiChu.text.toString().trim()
        val soTienString = binding.etThuNhap.text.toString().trim()

        if (selectedDate == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show()
            return
        }
        if (soTienString.isBlank()) {
            Toast.makeText(requireContext(), "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedCategory == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show()
            return
        }

        val soTien: Double
        try {
            soTien = soTienString.toDouble()
            if (soTien <= 0) {
                Toast.makeText(requireContext(), "Số tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show()
                return
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        val transactionType = if (isExpense) "CHI" else "THU"
        val transaction = TransactionEntity(
            amount = soTien,
            type = transactionType,
            date = selectedDate!!,
            category = selectedCategory!!.name,
            note = ghiChu
        )

        inputViewModel.addTransaction(transaction)
        Toast.makeText(requireContext(), "Đã lưu: ${transactionType.lowercase(Locale.getDefault())} ${selectedCategory!!.name}", Toast.LENGTH_LONG).show()
        resetInputFields()
    }

    private fun resetInputFields() {
        binding.etNgay.text?.clear()
        selectedDate = null
        binding.etThuNhap.text?.clear()
        binding.etGhiChu.text?.clear()
        selectedCategory = null
        // Nếu adapter có hàm clearSelection, gọi ở đây
        if (::categoryAdapter.isInitialized) { // Kiểm tra adapter đã được khởi tạo chưa
            categoryAdapter.clearSelection()
        }

        // Tùy chọn: Reset về tab "Tiền Chi"
        if (!isExpense) { // Nếu đang ở tab tiền thu, có thể tự động click về tab tiền chi
            // binding.btnTienChi.performClick() // Hoặc không làm gì cả, để người dùng tự chọn
        }
        binding.etNgay.requestFocus()
    }

    private fun showDatePickerDialog() {
        val year = calendarInstance.get(Calendar.YEAR)
        val month = calendarInstance.get(Calendar.MONTH)
        val day = calendarInstance.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                calendarInstance.set(selectedYear, selectedMonth, selectedDayOfMonth)
                selectedDate = calendarInstance.time
                updateDateInView()
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDateInView() {
        selectedDate?.let {
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            binding.etNgay.setText(sdf.format(it))
        }
    }

    private fun setButtonSelected(button: MaterialButton, isSelected: Boolean) {
        val context = button.context
        if (isSelected) {
            button.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.app_teal)
            button.setTextColor(ContextCompat.getColor(context, R.color.app_teal_text_on_solid))
            button.strokeColor = null // Bỏ viền khi được chọn
            button.strokeWidth = 0
        } else {
            button.backgroundTintList =
                ContextCompat.getColorStateList(context, android.R.color.transparent) // Nền trong suốt
            button.setTextColor(ContextCompat.getColor(context, R.color.app_teal_text_on_outlined))
            button.strokeColor = ContextCompat.getColorStateList(context, R.color.app_teal) // Hiện viền
            button.strokeWidth = (1 * resources.displayMetrics.density).toInt() // độ dày viền 1dp
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}