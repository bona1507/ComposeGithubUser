package com.example.composegithubuser.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FUEntity::class], version = 1)
abstract class FURoom : RoomDatabase(){
    abstract fun favoriteDao(): FUDao

    companion object{
        @Volatile
        private var INSTANCE: FURoom? = null

        @JvmStatic
        fun getInstance(context: Context): FURoom =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FURoom::class.java, "User.db"
                ).build()
            }
    }
}