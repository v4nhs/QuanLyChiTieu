package com.example.quanlychitieu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.quanlychitieu.entity.BudgetItem
import com.example.quanlychitieu.entity.TransactionEntity
import kotlinx.coroutines.Dispatchers
import java.util.Date
import com.example.quanlychitieu.repository.TransactionRepository
import kotlinx.coroutines.launch

class BudgetViewModel(private val repository: TransactionRepository) : ViewModel() {

    val budgetItems: LiveData<List<BudgetItem>> = liveData(Dispatchers.IO) {
        val transactions = repository.getAllTransactionsSortedByDate()
        // Chuyển đổi List<TransactionEntity> thành List<BudgetItem>
        val mappedItems = transactions.map { entity ->
            BudgetItem(
                id = entity.id.toString(),
                note = entity.note,
                type = entity.category,
                amount = entity.amount,
                date = entity.date,
                isExpense = entity.type == "CHI"
            )
        }
        emit(mappedItems)
    }
    fun deleteTransaction(budgetItem: BudgetItem) {
        viewModelScope.launch {
            // Chuyển đổi từ BudgetItem về TransactionEntity để xóa
            // Lưu ý: Bạn cần đảm bảo các trường cần thiết có đủ để Room nhận diện đúng đối tượng cần xóa
            val transactionToDelete = TransactionEntity(
                id = budgetItem.id.toLong(),
                amount = budgetItem.amount,
                type = if (budgetItem.isExpense) "CHI" else "THU",
                category = budgetItem.type,
                date = budgetItem.date,
                note = budgetItem.note
            )
            repository.deleteTransaction(transactionToDelete)
        }
    }
}

// Data class cho item hiển thị trên UI
data class BudgetItem(
    val id: String,
    val note: String?,
    val type: String,
    val amount: Double,
    val date: Date,
    val isExpense: Boolean
)