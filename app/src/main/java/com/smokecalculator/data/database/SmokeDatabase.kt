package com.smokecalculator.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CigaretteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SmokeDatabase : RoomDatabase() {
    abstract fun cigaretteDao(): CigaretteDao
}
