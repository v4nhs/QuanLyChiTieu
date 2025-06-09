package com.example.quanlychitieu.entity // Package phải là ".model"

import java.util.Date

data class BudgetItem(
    val id: String,
    val note: String?,
    val type: String, // Đây là category
    val amount: Double,
    val date: Date,
    val isExpense: Boolean
)