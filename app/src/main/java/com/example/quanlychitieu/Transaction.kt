package com.example.quanlychitieu

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: String, // "expense" hoặc "income"
    val date: Long    // Lưu thời gian dưới dạng timestamp
)
