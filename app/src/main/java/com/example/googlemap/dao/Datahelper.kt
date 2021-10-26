package com.example.googlemap.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [LatLocation::class], version = 1)
abstract class Datahelper : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {
        private var appDatabase: Datahelper? = null

        @Synchronized
        fun getInstance(context: Context): Datahelper {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, Datahelper::class.java, "map_db")
                    .allowMainThreadQueries()
                    .build()
            }
            return appDatabase!!
        }
    }
}