package com.example.quanlychitieu.viewmodel // Hoặc package của bạn

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quanlychitieu.database.AppDatabase
import com.example.quanlychitieu.database.TransactionDao
import com.example.quanlychitieu.database.TransactionEntity
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionDao: TransactionDao

    // LiveData cho tóm tắt THEO THÁNG (như cũ)
    private val _monthlyTotalIncome = MutableLiveData<Double>(0.0)
    val monthlyTotalIncome: LiveData<Double> get() = _monthlyTotalIncome

    private val _monthlyTotalExpense = MutableLiveData<Double>(0.0)
    val monthlyTotalExpense: LiveData<Double> get() = _monthlyTotalExpense

    private val _monthlyNetIncome = MutableLiveData<Double>(0.0)
    val monthlyNetIncome: LiveData<Double> get() = _monthlyNetIncome

    // --- LIVE DATA MỚI CHO TÓM TẮT THEO NGÀY ĐÃ CHỌN ---
    private val _dailyTotalIncome = MutableLiveData<Double>(0.0)
    val dailyTotalIncome: LiveData<Double> get() = _dailyTotalIncome

    private val _dailyTotalExpense = MutableLiveData<Double>(0.0)
    val dailyTotalExpense: LiveData<Double> get() = _dailyTotalExpense

    private val _dailyNetIncome = MutableLiveData<Double>(0.0)
    val dailyNetIncome: LiveData<Double> get() = _dailyNetIncome
    // --- HẾT PHẦN LIVE DATA MỚI CHO NGÀY ---

    private val _transactionsForSelectedDate = MutableLiveData<List<TransactionEntity>>()
    val transactionsForSelectedDate: LiveData<List<TransactionEntity>> get() = _transactionsForSelectedDate

    init {
        val database = AppDatabase.getDatabase(application)
        transactionDao = database.transactionDao()
    }

    fun loadMonthlySummary(year: Int, month: Int) {
        viewModelScope.launch {
            val calendarStart = Calendar.getInstance().apply {
                set(year, month, 1, 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startDate = calendarStart.time

            val calendarEnd = Calendar.getInstance().apply {
                set(year, month, calendarStart.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
                set(Calendar.MILLISECOND, 999)
            }
            val endDate = calendarEnd.time

            val transactionsInMonth = transactionDao.getTransactionsBetweenDates(startDate, endDate)

            var income = 0.0
            var expense = 0.0
            transactionsInMonth.forEach { transaction ->
                if (transaction.type == "THU") {
                    income += transaction.amount
                } else if (transaction.type == "CHI") {
                    expense += transaction.amount
                }
            }
            _monthlyTotalIncome.postValue(income)
            _monthlyTotalExpense.postValue(expense)
            _monthlyNetIncome.postValue(income - expense)
        }
    }

    // --- HÀM MỚI ĐỂ TẢI DỮ LIỆU CHO NGÀY CỤ THỂ ---
    fun loadDailySummaryAndTransactions(date: Date) {
        viewModelScope.launch {
            val calendarStart = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startDate = calendarStart.time

            val calendarEnd = Calendar.getInstance().apply {
                time = date
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }
            val endDate = calendarEnd.time

            val transactionsOnDate = transactionDao.getTransactionsBetweenDates(startDate, endDate)
            _transactionsForSelectedDate.postValue(transactionsOnDate) // Cập nhật danh sách giao dịch chi tiết

            var dailyIncome = 0.0
            var dailyExpense = 0.0
            transactionsOnDate.forEach { transaction ->
                if (transaction.type == "THU") {
                    dailyIncome += transaction.amount
                } else if (transaction.type == "CHI") {
                    dailyExpense += transaction.amount
                }
            }
            _dailyTotalIncome.postValue(dailyIncome)
            _dailyTotalExpense.postValue(dailyExpense)
            _dailyNetIncome.postValue(dailyIncome - dailyExpense)
        }
    }
}