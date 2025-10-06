package com.example.contactappv1.viewmodel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.contactappv1.model.DataUser
import com.example.contactappv1.model.UserDao

@Database(entities = [DataUser::class], version = 4)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object{
        var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "contact")
                    .fallbackToDestructiveMigration(false)
                    .build()
            }
            return instance!!
        }
    }

}