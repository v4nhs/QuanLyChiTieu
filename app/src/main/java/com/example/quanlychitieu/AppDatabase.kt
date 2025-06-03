package com.example.quanlychitieu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters // Quan trọng nếu bạn dùng Date

// Nếu bạn có Date trong Entity, bạn cần TypeConverter
@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // Thêm dòng này
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
                    "expense_tracker_db" // Tên database file
                )
                    // .fallbackToDestructiveMigration() // Nếu bạn thay đổi schema và không muốn viết migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}