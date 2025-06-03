package com.example.quanlychitieu.model // Hoặc package bạn đã chọn

import java.util.Date // Đảm bảo import java.util.Date

data class BudgetItem(
    val id: String, // Hoặc Long nếu từ database
    val note: String?,
    val type: String, // Ví dụ: "Loại phí y tế", "Loại phí đồ ăn"
    val amount: Double,
    val date: Date,
    val isExpense: Boolean // Để xác định màu cho số tiền
)