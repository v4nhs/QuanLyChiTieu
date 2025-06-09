package com.example.quanlychitieu.repository

import com.example.quanlychitieu.database.TransactionDao
import com.example.quanlychitieu.entity.TransactionEntity
import com.example.quanlychitieu.viewmodel.CategoryTotal
import java.util.Date

class TransactionRepository(private val transactionDao: TransactionDao) {

    // Hàm cho InputViewModel
    suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    // Hàm cho BudgetViewModel
    suspend fun getAllTransactionsSortedByDate(): List<TransactionEntity> {
        return transactionDao.getAllTransactionsSortedByDate()
    }

    // Các hàm cho ReportViewModel
    suspend fun getTransactionsBetweenDates(startDate: Date, endDate: Date): List<TransactionEntity> {
        return transactionDao.getTransactionsBetweenDates(startDate, endDate)
    }

    suspend fun getTotalIncomeForPeriod(startDate: Date, endDate: Date): Double {
        return transactionDao.getTotalIncomeForPeriod(startDate, endDate) ?: 0.0
    }

    suspend fun getTotalExpenseForPeriod(startDate: Date, endDate: Date): Double {
        return transactionDao.getTotalExpenseForPeriod(startDate, endDate) ?: 0.0
    }

    suspend fun getCategoryTotalsForPeriod(startDate: Date, endDate: Date): List<CategoryTotal> {
        return transactionDao.getCategoryTotalsForPeriod(startDate, endDate)
    }
    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }
}