package com.example.flightmobileapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Url::class], version = 1)
abstract class UrlDataBase : RoomDatabase() {

    abstract val urlDAO : UrlDAO

    companion object {
        @Volatile
        private var INSTANCE : UrlDataBase? = null
        fun getInstance(context : Context) : UrlDataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, UrlDataBase:: class.java, "URL_data_table"
                    ).build()
                }
                return instance
            }
        }
    }
}