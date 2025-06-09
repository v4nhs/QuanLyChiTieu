package com.example.quanlychitieu.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quanlychitieu.R
import com.example.quanlychitieu.adapter.TransactionReportAdapter
import com.example.quanlychitieu.connectdb.AppDatabase
import com.example.quanlychitieu.databinding.FragmentReportPieChartBinding
import com.example.quanlychitieu.viewmodel.ReportViewModelFactory
import com.example.quanlychitieu.viewmodel.ReportViewModel
import com.example.quanlychitieu.repository.TransactionRepository
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ReportPieChartFragment : Fragment() {

    private var _binding: FragmentReportPieChartBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ReportViewModel
    private lateinit var transactionAdapter: TransactionReportAdapter

    private val monthFormat = SimpleDateFormat("MMMM", Locale("vi", "VN"))
    private val yearFormat = SimpleDateFormat("yyyy", Locale("vi", "VN"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReportPieChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ReportViewModelFactory(TransactionRepository(AppDatabase.getDatabase(requireContext()).transactionDao()))
        viewModel = ViewModelProvider(this, factory).get(ReportViewModel::class.java)

        setupPieChart()
        setupRecyclerView()
        setupEventListeners()
        setupObservers()

        // Tải dữ liệu cho tháng hiện tại khi mới mở
        viewModel.setSelectedDate(Calendar.getInstance())
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionReportAdapter()
        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionsRecyclerView.adapter = transactionAdapter
    }

    private fun setupPieChart() {
        binding.pieChart.apply {
            isDrawHoleEnabled = true
            holeRadius = 75f
            setHoleColor(Color.TRANSPARENT)
            setDrawEntryLabels(false)
            description.isEnabled = false
            legend.isEnabled = false
        }
    }

    // Cài đặt sự kiện click cho các TextView chọn ngày
    private fun setupEventListeners() {
        binding.tvMonthSelector.setOnClickListener { showMonthPickerDialog() }
        binding.tvYearSelector.setOnClickListener { showYearPickerDialog() }
    }

    // Lắng nghe dữ liệu từ ViewModel
    private fun setupObservers() {
        // Lắng nghe ngày tháng được chọn để cập nhật UI
        viewModel.selectedDate.observe(viewLifecycleOwner) { calendar ->
            binding.tvMonthSelector.text = monthFormat.format(calendar.time)
            binding.tvYearSelector.text = yearFormat.format(calendar.time)
        }

        // Lắng nghe tổng thu và chi để vẽ lại biểu đồ
        val chartObserver = { _: Any? ->
            val income = viewModel.totalIncome.value ?: 0.0
            val expense = viewModel.totalExpense.value ?: 0.0
            updatePieChartData(income, expense)
        }
        viewModel.totalIncome.observe(viewLifecycleOwner, chartObserver)
        viewModel.totalExpense.observe(viewLifecycleOwner, chartObserver)

        // Lắng nghe danh sách giao dịch chi tiết
        viewModel.transactionDetails.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter.submitList(transactions)
        }
    }

    // Cập nhật dữ liệu lên biểu đồ
    private fun updatePieChartData(income: Double, expense: Double) {
        val total = income + expense
        if (total == 0.0) {
            binding.pieChart.clear()
            binding.pieChart.centerText = formatCurrency(0.0)
            binding.pieChart.setNoDataText("Không có dữ liệu")
            binding.pieChart.invalidate()
            return
        }

        val entries = arrayListOf(
            PieEntry(income.toFloat(), "Thu"),
            PieEntry(expense.toFloat(), "Chi")
        )
        val dataSet = PieDataSet(entries, "").apply {
            colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.income_green),
                ContextCompat.getColor(requireContext(), R.color.expense_red)
            )
            setDrawValues(false)
        }

        binding.pieChart.data = PieData(dataSet)
        binding.pieChart.centerText = formatCurrency(income - expense)
        binding.pieChart.animateY(1000, Easing.EaseInOutQuad)
        binding.pieChart.invalidate()
    }

    private fun showMonthPickerDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_number_picker, null)
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.numberPicker)
        val currentCalendar = viewModel.selectedDate.value ?: Calendar.getInstance()

        numberPicker.minValue = 1
        numberPicker.maxValue = 12
        numberPicker.value = currentCalendar.get(Calendar.MONTH) + 1 // Calendar month 0-11
        numberPicker.displayedValues = (1..12).map { "Tháng $it" }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle("Chọn Tháng")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val newCalendar = viewModel.selectedDate.value ?: Calendar.getInstance()
                newCalendar.set(Calendar.MONTH, numberPicker.value - 1)
                viewModel.setSelectedDate(newCalendar)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showYearPickerDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_number_picker, null)
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.numberPicker)
        val currentCalendar = viewModel.selectedDate.value ?: Calendar.getInstance()

        numberPicker.minValue = 2020
        numberPicker.maxValue = 2030
        numberPicker.value = currentCalendar.get(Calendar.YEAR)

        AlertDialog.Builder(requireContext())
            .setTitle("Chọn Năm")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val newCalendar = viewModel.selectedDate.value ?: Calendar.getInstance()
                newCalendar.set(Calendar.YEAR, numberPicker.value)
                viewModel.setSelectedDate(newCalendar)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}