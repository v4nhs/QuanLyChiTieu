package com.example.quanlychitieu.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.quanlychitieu.R
import com.example.quanlychitieu.connectdb.AppDatabase
import com.example.quanlychitieu.databinding.FragmentReportCalendarBinding
import com.example.quanlychitieu.viewmodel.ReportViewModelFactory
import com.example.quanlychitieu.viewmodel.ReportViewModel
import com.example.quanlychitieu.repository.TransactionRepository
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ReportCalendarFragment : Fragment() {

    private var _binding: FragmentReportCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ReportViewModel
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo ViewModel đúng cách qua Factory
        val factory = ReportViewModelFactory(
            TransactionRepository(
                AppDatabase.getDatabase(requireContext()).transactionDao()
            )
        )
        viewModel = ViewModelProvider(this, factory).get(ReportViewModel::class.java)

        setupEventListeners()
        setupObservers()

        // Tải dữ liệu cho ngày hôm nay khi mới mở Fragment
        viewModel.setSelectedDate(Calendar.getInstance())
    }

    // Hàm này đăng ký lắng nghe dữ liệu từ ViewModel và cập nhật UI
    private fun setupObservers() {
        // Lắng nghe ngày được chọn để cập nhật text
        viewModel.selectedDate.observe(viewLifecycleOwner) { calendar ->
            binding.tvSelectedDate.text = dateFormat.format(calendar.time)
        }

        // Lắng nghe tổng thu nhập
        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            binding.tvTotalIncome.text = formatCurrency(income ?: 0.0)
            updateNetTotal()
        }

        // Lắng nghe tổng chi tiêu
        viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
            binding.tvTotalExpense.text = formatCurrency(expense ?: 0.0)
            updateNetTotal()
        }
    }

    // Hàm này xử lý sự kiện người dùng
    private fun setupEventListeners() {
        // Khi người dùng chọn một ngày mới trên CalendarView
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val newCalendar = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
            // Yêu cầu ViewModel cập nhật lại ngày đã chọn
            viewModel.setSelectedDate(newCalendar)
        }
    }

    // Tính toán và cập nhật lại số tổng cuối cùng
    private fun updateNetTotal() {
        val income = viewModel.totalIncome.value ?: 0.0
        val expense = viewModel.totalExpense.value ?: 0.0
        val netTotal = income - expense

        binding.tvNetTotal.text = formatCurrency(netTotal)

        // Đổi màu cho số tổng
        val colorRes = when {
            netTotal < 0 -> R.color.expense_red
            netTotal > 0 -> R.color.income_green
            else -> R.color.summary_text_amount
        }
        binding.tvNetTotal.setTextColor(ContextCompat.getColor(requireContext(), colorRes))
    }

    // Hàm tiện ích để định dạng tiền tệ
    private fun formatCurrency(amount: Double): String {
        val locale = Locale("vi", "VN")
        return NumberFormat.getCurrencyInstance(locale).format(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}