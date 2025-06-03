package com.example.quanlychitieu.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Để Room tự tạo ID
    val amount: Double,
    val type: String, // "THU" hoặc "CHI"
    val date: Date,
    val category: String,
    val note: String?
)