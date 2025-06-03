package com.example.quanlychitieu.viewmodel // Đảm bảo đúng package

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlychitieu.database.AppDatabase
import com.example.quanlychitieu.database.TransactionDao
import com.example.quanlychitieu.database.TransactionEntity
import kotlinx.coroutines.launch

class InputViewModel(application: Application) : AndroidViewModel(application) {

    private val transactionDao: TransactionDao

    init {
        val database = AppDatabase.getDatabase(application)
        transactionDao = database.transactionDao()
    }

    fun addTransaction(transaction: TransactionEntity) { // Đảm bảo hàm này tồn tại
        viewModelScope.launch {
            transactionDao.insertTransaction(transaction)
        }
    }
}