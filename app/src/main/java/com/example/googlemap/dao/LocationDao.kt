package com.example.googlemap.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {

    @Insert
    fun addLocation(location: LatLocation)

    @Query("SELECT * FROM locations")
    fun getLocations(): List<LatLocation>
}