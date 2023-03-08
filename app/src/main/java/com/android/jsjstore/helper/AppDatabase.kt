package com.android.jsjstore.helper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.jsjstore.interfaces.ClientOrderDao
import com.android.jsjstore.model.ClientOrder

@Database(entities = [ClientOrder::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientOrderDao(): ClientOrderDao

    companion object {
        private const val DATABASE_NAME = "JJStoreDB"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}
