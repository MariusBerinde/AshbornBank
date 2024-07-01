package com.example.ashborn.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ashborn.dao.UserDao
import com.example.ashborn.data.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class UserDb:RoomDatabase() {
   abstract fun userDao(): UserDao
   // abstract val dao:UserDao
   companion object{
       @Volatile
       private var Instance:UserDb?=null
       fun getDatabase(context: Context):UserDb{
           return Instance?: synchronized(this){
               Room.databaseBuilder(
                   context,
                   UserDb::class.java,
                   "Ashborn_db"
               ).createFromAsset("Ashborn_db.db").build().also { Instance = it }
           }
       }
   }

}
