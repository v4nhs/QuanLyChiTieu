package com.example.quanlychitieu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlychitieu.entity.TransactionEntity
import com.example.quanlychitieu.repository.TransactionRepository
import kotlinx.coroutines.launch

class InputViewModel(private val repository: TransactionRepository) : ViewModel() {
    fun insertTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }
}