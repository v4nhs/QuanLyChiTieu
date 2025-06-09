package com.example.quanlychitieu.connectdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.example.quanlychitieu.converters.Converters
import com.example.quanlychitieu.database.TransactionDao
import com.example.quanlychitieu.entity.TransactionEntity

// SỬA LẠI VERSION Ở ĐÂY
@Database(entities = [TransactionEntity::class], version = 2, exportSchema = false) // <-- TĂNG LÊN 2 (HOẶC SỐ TIẾP THEO)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_tracker_db" // Tên file database
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}