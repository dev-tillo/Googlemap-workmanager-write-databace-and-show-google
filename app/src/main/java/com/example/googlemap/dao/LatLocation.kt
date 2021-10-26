package com.example.googlemap.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LatLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lenga: Double,
    val longw: Double,
)