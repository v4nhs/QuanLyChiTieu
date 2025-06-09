package com.example.quanlychitieu.database

import androidx.room.*
import com.example.quanlychitieu.entity.TransactionEntity
import com.example.quanlychitieu.viewmodel.CategoryTotal
import java.util.Date

@Dao
interface TransactionDao {
    // Hàm cho InputViewModel
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    // Hàm cho BudgetViewModel
    @Query("SELECT * FROM `transaction` ORDER BY date DESC")
    suspend fun getAllTransactionsSortedByDate(): List<TransactionEntity>

    // Các hàm cho ReportViewModel
    @Query("SELECT * FROM `transaction` WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    suspend fun getTransactionsBetweenDates(startDate: Date, endDate: Date): List<TransactionEntity>

    @Query("SELECT SUM(amount) FROM `transaction` WHERE type = 'THU' AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalIncomeForPeriod(startDate: Date, endDate: Date): Double?

    @Query("SELECT SUM(amount) FROM `transaction` WHERE type = 'CHI' AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpenseForPeriod(startDate: Date, endDate: Date): Double?

    @Query("SELECT category as categoryName, SUM(amount) as totalAmount FROM `transaction` WHERE type = 'CHI' AND date BETWEEN :startDate AND :endDate GROUP BY category")
    suspend fun getCategoryTotalsForPeriod(startDate: Date, endDate: Date): List<CategoryTotal>

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}
