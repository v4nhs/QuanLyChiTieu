package com.example.quanlychitieu.fragment

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
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.CategoryAdapter
import com.example.quanlychitieu.connectdb.AppDatabase
import com.example.quanlychitieu.entity.TransactionEntity
import com.example.quanlychitieu.databinding.FragmentInputBinding
import com.example.quanlychitieu.entity.CategoryItem
import com.example.quanlychitieu.repository.TransactionRepository
import com.example.quanlychitieu.viewmodel.InputViewModel
import com.example.quanlychitieu.viewmodel.InputViewModelFactory
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    // Giữ nguyên các biến thành viên
    private lateinit var inputViewModel: InputViewModel
    private val calendarInstance: Calendar = Calendar.getInstance()
    private var selectedDate: Date? = null
    private var isExpense: Boolean = true
    private var selectedCategory: CategoryItem? = null
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var expenseCategories: List<CategoryItem>
    private lateinit var incomeCategories: List<CategoryItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        // ### BỎ PHẦN LOGIC KHỞI TẠO Ở ĐÂY ###
        // Logic sẽ được chuyển vào onViewCreated
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ### PHẦN SỬA LỖI QUAN TRỌNG NHẤT ###
        // Khởi tạo ViewModel đúng cách thông qua Factory
        val dao = AppDatabase.getDatabase(requireContext()).transactionDao()
        val repository = TransactionRepository(dao)
        val factory = InputViewModelFactory(repository)
        inputViewModel = ViewModelProvider(this, factory).get(InputViewModel::class.java)
        // ### KẾT THÚC PHẦN SỬA LỖI ###

        // Bây giờ mới gọi các hàm cài đặt
        loadCategoryLists()
        setupInitialState()
        setupClickListeners()
        setupCategoryRecyclerView()
    }

    // =============================================================
    // TOÀN BỘ CÁC HÀM CÒN LẠI CỦA BẠN (saveTransaction, loadCategoryLists, ...)
    // ĐỀU GIỮ NGUYÊN, KHÔNG CẦN THAY ĐỔI GÌ.
    // Dưới đây là code của bạn để bạn tiện so sánh.
    // =============================================================

    private fun loadCategoryLists() {
        expenseCategories = listOf(
            CategoryItem("Ăn uống", R.drawable.ic_food),
            CategoryItem("Hàng ngày", R.drawable.ic_daily),
            CategoryItem("Quần áo", R.drawable.ic_clothes),
            CategoryItem("Mỹ phẩm", R.drawable.ic_beauty),
            CategoryItem("Giao lưu", R.drawable.ic_social),
            CategoryItem("Y tế", R.drawable.ic_medical),
            CategoryItem("Khác", R.drawable.ic_other)
        )

        incomeCategories = listOf(
            CategoryItem("Lương", R.drawable.ic_salary),
            CategoryItem("Thưởng", R.drawable.ic_bonus),
            CategoryItem("Thu nhập phụ", R.drawable.ic_side_income),
            CategoryItem("Đầu tư", R.drawable.ic_investment),
            CategoryItem("Khác", R.drawable.ic_other)
        )
    }

    private fun setupCategoryRecyclerView() {
        val initialCategories = if (isExpense) expenseCategories else incomeCategories
        categoryAdapter = CategoryAdapter(initialCategories) { category ->
            selectedCategory = category
        }
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = categoryAdapter
        }
    }

    private fun updateCategoriesForCurrentType() {
        selectedCategory = null
        val currentCategories = if (isExpense) expenseCategories else incomeCategories
        // Sửa lại logic cập nhật adapter một chút cho đúng
        categoryAdapter.updateCategories(currentCategories)
    }

    private fun setupInitialState() {
        setButtonSelected(binding.btnTienChi, true)
        setButtonSelected(binding.btnTienThu, false)
        isExpense = true
        binding.tilThuNhap.hint = getString(R.string.so_tien_chi_hint)
        binding.tvDanhMucChiTitle.text = getString(R.string.danh_muc_chi_title)
    }

    private fun setupClickListeners() {
        binding.btnTienChi.setOnClickListener {
            setButtonSelected(binding.btnTienChi, true)
            setButtonSelected(binding.btnTienThu, false)
            isExpense = true
            binding.tilThuNhap.hint = getString(R.string.so_tien_chi_hint)
            binding.tvDanhMucChiTitle.text = getString(R.string.danh_muc_chi_title)
            binding.btnNhapKhoanChi.text = "Nhập Khoản Chi"
            updateCategoriesForCurrentType()
        }

        binding.btnTienThu.setOnClickListener {
            setButtonSelected(binding.btnTienThu, true)
            setButtonSelected(binding.btnTienChi, false)
            isExpense = false
            binding.tilThuNhap.hint = getString(R.string.so_tien_thu_hint)
            binding.tvDanhMucChiTitle.text = getString(R.string.danh_muc_thu_title)
            binding.btnNhapKhoanChi.text = "Nhập Khoản Thu"
            updateCategoriesForCurrentType()
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

        inputViewModel.insertTransaction(transaction)
        Toast.makeText(requireContext(), "Đã lưu giao dịch!", Toast.LENGTH_LONG).show()
        resetInputFields()
    }

    private fun resetInputFields() {
        binding.etNgay.text?.clear()
        selectedDate = null
        binding.etThuNhap.text?.clear()
        binding.etGhiChu.text?.clear()
        selectedCategory = null
        if (::categoryAdapter.isInitialized) {
            categoryAdapter.clearSelection()
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
            button.strokeColor = null
            button.strokeWidth = 0
        } else {
            button.backgroundTintList =
                ContextCompat.getColorStateList(context, android.R.color.transparent)
            button.setTextColor(ContextCompat.getColor(context, R.color.app_teal_text_on_outlined))
            button.strokeColor = ContextCompat.getColorStateList(context, R.color.app_teal)
            button.strokeWidth = (1 * resources.displayMetrics.density).toInt()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}