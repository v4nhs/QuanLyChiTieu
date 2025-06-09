package com.example.quanlychitieu.viewmodel

import androidx.lifecycle.*
import com.example.quanlychitieu.entity.TransactionEntity
import com.example.quanlychitieu.repository.TransactionRepository
import java.util.*

enum class FilterType { MONTH, YEAR } // Bỏ DAY vì màn hình mới không cần

class ReportViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _selectedDate = MutableLiveData(Calendar.getInstance())
    val selectedDate: LiveData<Calendar> get() = _selectedDate

    private fun getTrigger(): LiveData<Calendar> = _selectedDate

    val totalIncome: LiveData<Double> = getTrigger().switchMap { calendar ->
        liveData { emit(fetchTotal(calendar, "THU")) }
    }
    val totalExpense: LiveData<Double> = getTrigger().switchMap { calendar ->
        liveData { emit(fetchTotal(calendar, "CHI")) }
    }
    val transactionDetails: LiveData<List<TransactionEntity>> = getTrigger().switchMap { calendar ->
        liveData {
            val (start, end) = getMonthDateRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
            emit(repository.getTransactionsBetweenDates(start, end))
        }
    }

    private suspend fun fetchTotal(calendar: Calendar, type: String): Double {
        val (start, end) = getMonthDateRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
        return if (type == "THU") repository.getTotalIncomeForPeriod(start, end)
        else repository.getTotalExpenseForPeriod(start, end)
    }

    fun setSelectedDate(calendar: Calendar) {
        _selectedDate.value = calendar
    }

    private fun getMonthDateRange(year: Int, month: Int): Pair<Date, Date> {
        val calendar = Calendar.getInstance().apply { set(year, month, 1, 0, 0, 0) }
        val start = calendar.time
        calendar.apply { set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH)); set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59); set(Calendar.SECOND, 59) }
        val end = calendar.time
        return start to end
    }
}