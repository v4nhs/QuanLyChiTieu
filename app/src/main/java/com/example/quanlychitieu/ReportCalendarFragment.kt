package com.example.quanlychitieu // Hoặc package của bạn

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat // Import ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.quanlychitieu.database.TransactionEntity // Nếu bạn muốn dùng
import com.example.quanlychitieu.databinding.FragmentReportCalendarBinding
import com.example.quanlychitieu.viewmodel.ReportViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReportCalendarFragment : Fragment() {

    private var _binding: FragmentReportCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var reportViewModel: ReportViewModel
    private val calendarInstance: Calendar = Calendar.getInstance()
    private val monthYearFormat = SimpleDateFormat("MMMM, yyyy", Locale("vi", "VN"))
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportCalendarBinding.inflate(inflater, container, false)
        reportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)

        setupUI()
        setupObservers() // Gọi trước setupEventListeners để observer sẵn sàng
        setupEventListeners()
        reportViewModel.loadMonthlySummary(calendarInstance.get(Calendar.YEAR), calendarInstance.get(Calendar.MONTH))

        // Tải dữ liệu tóm tắt cho ngày hiện tại ban đầu
        val today = calendarInstance.time
        binding.tvSelectedDate.text = dateFormat.format(today) // Hiển thị ngày hiện tại
        binding.tvSelectedDate.visibility = View.VISIBLE
        reportViewModel.loadDailySummaryAndTransactions(today) // Tải summary cho ngày hiện tại

        return binding.root
    }

    private fun setupUI() {
        binding.tvTotalIncome.text = formatCurrency(0.0)
        binding.tvTotalExpense.text = formatCurrency(0.0)
        binding.tvNetTotal.text = formatCurrency(0.0)
        // Không cần set text cho tvSelectedDate ở đây nữa vì sẽ làm trong onCreateView
    }

    private fun setupObservers() {
        // Observer cho tóm tắt THÁNG (bạn có thể quyết định có hiển thị cái này không,
        // hoặc chỉ dùng nó để cập nhật tvMonthYearSelector nếu cần)
        reportViewModel.monthlyTotalIncome.observe(viewLifecycleOwner) { /* Xử lý nếu cần */ }
        reportViewModel.monthlyTotalExpense.observe(viewLifecycleOwner) { /* Xử lý nếu cần */ }
        reportViewModel.monthlyNetIncome.observe(viewLifecycleOwner) { /* Xử lý nếu cần */ }


        // --- OBSERVER CHO TÓM TẮT THEO NGÀY ĐÃ CHỌN ---
        reportViewModel.dailyTotalIncome.observe(viewLifecycleOwner) { income ->
            binding.tvTotalIncome.text = formatCurrency(income ?: 0.0)
            // Đặt màu cho thu nhập nếu muốn
            binding.tvTotalIncome.setTextColor(ContextCompat.getColor(requireContext(), R.color.income_green))
        }

        reportViewModel.dailyTotalExpense.observe(viewLifecycleOwner) { expense ->
            binding.tvTotalExpense.text = formatCurrency(expense ?: 0.0)
            if ((expense ?: 0.0) > 0) {
                binding.tvTotalExpense.setTextColor(ContextCompat.getColor(requireContext(), R.color.expense_red))
            } else {
                // Nếu chi tiêu là 0, có thể đặt màu trung tính hơn
                binding.tvTotalExpense.setTextColor(ContextCompat.getColor(requireContext(), R.color.summary_text_amount))
            }
        }

        reportViewModel.dailyNetIncome.observe(viewLifecycleOwner) { netIncome ->
            binding.tvNetTotal.text = formatCurrency(netIncome ?: 0.0)
            val netIncomeValue = netIncome ?: 0.0
            if (netIncomeValue < 0) {
                binding.tvNetTotal.setTextColor(ContextCompat.getColor(requireContext(), R.color.expense_red))
            } else if (netIncomeValue > 0) {
                binding.tvNetTotal.setTextColor(ContextCompat.getColor(requireContext(), R.color.income_green))
            } else {
                binding.tvNetTotal.setTextColor(ContextCompat.getColor(requireContext(), R.color.summary_text_amount))
            }
        }
        // --- HẾT PHẦN OBSERVER CHO NGÀY ---


        reportViewModel.transactionsForSelectedDate.observe(viewLifecycleOwner) { transactions ->
            // TODO: Nếu bạn có RecyclerView để hiển thị danh sách giao dịch chi tiết của ngày,
            // cập nhật adapter ở đây.
            // Ví dụ: dailyTransactionAdapter.submitList(transactions)
            if (transactions.isNullOrEmpty()) {
                // Có thể hiển thị thông báo "Không có giao dịch" cho ngày này
            }
        }
    }

    private fun setupEventListeners() {
        binding.tvDailySummaryTitle.setOnClickListener {
            showMonthYearPickerDialog()
        }

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendarInstance.set(year, month, dayOfMonth)
            val selectedDate = calendarInstance.time

            binding.tvSelectedDate.text = dateFormat.format(selectedDate)
            binding.tvSelectedDate.visibility = View.VISIBLE

            // --- GỌI HÀM TẢI DỮ LIỆU CHO NGÀY ĐÃ CHỌN ---
            reportViewModel.loadDailySummaryAndTransactions(selectedDate)
        }
    }

    private fun showMonthYearPickerDialog() {
        val year = calendarInstance.get(Calendar.YEAR)
        val month = calendarInstance.get(Calendar.MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, _ ->
                calendarInstance.set(Calendar.YEAR, selectedYear)
                calendarInstance.set(Calendar.MONTH, selectedMonth)
                reportViewModel.loadMonthlySummary(selectedYear, selectedMonth)

                // Tùy chọn: Có thể bạn muốn tự động chọn ngày đầu tiên của tháng mới
                // và tải tóm tắt ngày cho ngày đó.
                // calendarInstance.set(Calendar.DAY_OF_MONTH, 1)
                // val firstDayOfMonth = calendarInstance.time
                // binding.tvSelectedDate.text = dateFormat.format(firstDayOfMonth)
                // binding.tvSelectedDate.visibility = View.VISIBLE
                // reportViewModel.loadDailySummaryAndTransactions(firstDayOfMonth)
            },
            year,
            month,
            1
        )
        datePickerDialog.show()
    }



    private fun formatCurrency(amount: Double): String {
        val locale = Locale("vi", "VN")
        val formatter = NumberFormat.getCurrencyInstance(locale)
        return formatter.format(amount)
    }

    override fun onResume() {
        super.onResume()
        // Khi Fragment quay trở lại, tải lại dữ liệu cho ngày đang hiển thị trên tvSelectedDate
        // hoặc ngày hiện tại nếu tvSelectedDate trống.
        val displayedDateString = binding.tvSelectedDate.text.toString()
        val dateToLoad = if (displayedDateString.isNotEmpty()) {
            try {
                dateFormat.parse(displayedDateString) ?: Date() // Nếu parse lỗi, dùng ngày hiện tại
            } catch (e: Exception) {
                Date() // Mặc định là ngày hiện tại nếu có lỗi parse
            }
        } else {
            Date() // Ngày hiện tại nếu tvSelectedDate trống
        }
        reportViewModel.loadDailySummaryAndTransactions(dateToLoad)

        // Cũng có thể tải lại tóm tắt tháng nếu cần
        // val currentYear = calendarInstance.get(Calendar.YEAR) // Hoặc lấy từ trạng thái đã lưu
        // val currentMonth = calendarInstance.get(Calendar.MONTH)
        // reportViewModel.loadMonthlySummary(currentYear, currentMonth)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}