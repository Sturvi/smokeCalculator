package com.smokecalculator.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cigarettes")
data class CigaretteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long
)
