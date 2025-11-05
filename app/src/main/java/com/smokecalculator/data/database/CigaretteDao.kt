package com.smokecalculator.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CigaretteDao {
    @Insert
    suspend fun insert(cigarette: CigaretteEntity): Long

    @Query("SELECT * FROM cigarettes ORDER BY timestamp DESC")
    fun getAllCigarettes(): Flow<List<CigaretteEntity>>

    @Query("SELECT * FROM cigarettes WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    fun getCigarettesSince(startTime: Long): Flow<List<CigaretteEntity>>

    @Query("SELECT * FROM cigarettes ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastCigarette(): CigaretteEntity?

    @Query("SELECT COUNT(*) FROM cigarettes WHERE timestamp >= :startTime")
    suspend fun getCigaretteCountSince(startTime: Long): Int

    @Query("SELECT COUNT(*) FROM cigarettes WHERE timestamp >= :startTime")
    fun getCigaretteCountSinceFlow(startTime: Long): Flow<Int>

    @Query("DELETE FROM cigarettes WHERE timestamp < :beforeTime")
    suspend fun deleteOldCigarettes(beforeTime: Long)
}
