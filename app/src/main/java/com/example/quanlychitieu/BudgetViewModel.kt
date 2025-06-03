package com.example.quanlychitieu.viewmodel // Hoặc package của bạn

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quanlychitieu.database.AppDatabase
import com.example.quanlychitieu.database.TransactionDao
import com.example.quanlychitieu.database.TransactionEntity
import com.example.quanlychitieu.model.BudgetItem // Import BudgetItem
import kotlinx.coroutines.launch
import java.util.Date

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionDao: TransactionDao

    // LiveData để giữ danh sách BudgetItem
    private val _budgetItemsLiveData = MutableLiveData<List<BudgetItem>>()
    val budgetItemsLiveData: LiveData<List<BudgetItem>> get() = _budgetItemsLiveData

    init {
        val database = AppDatabase.getDatabase(application)
        transactionDao = database.transactionDao()
        // Tải tất cả giao dịch khi ViewModel được tạo
        loadAllTransactionsAsBudgetItems()
    }

    fun loadAllTransactionsAsBudgetItems() {
        viewModelScope.launch {
            // Giả sử bạn có hàm getAllTransactions() trong DAO trả về List<TransactionEntity>
            // Hoặc bạn có thể tạo một hàm trả về LiveData<List<TransactionEntity>> từ DAO
            // và dùng Transformations.map để chuyển đổi.
            // Ở đây, chúng ta sẽ lấy một lần và postValue.
            val allTransactions = transactionDao.getAllTransactionsSortedByDate() // Cần tạo hàm này trong DAO

            // Chuyển đổi List<TransactionEntity> thành List<BudgetItem>
            val budgetItems = allTransactions.map { transactionEntity ->
                BudgetItem(
                    id = transactionEntity.id.toString(), // Chuyển Long thành String cho id của BudgetItem
                    note = transactionEntity.note,
                    type = transactionEntity.category, // Sử dụng category làm 'type' cho BudgetItem
                    amount = transactionEntity.amount,
                    date = transactionEntity.date,
                    isExpense = transactionEntity.type == "CHI" // Xác định isExpense dựa trên type
                )
            }
            _budgetItemsLiveData.postValue(budgetItems)
        }
    }
}