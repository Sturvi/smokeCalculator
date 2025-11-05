package com.smokecalculator.domain.repository

import com.smokecalculator.domain.model.Cigarette
import com.smokecalculator.domain.model.Settings
import com.smokecalculator.domain.model.Statistics
import kotlinx.coroutines.flow.Flow

interface CigaretteRepository {
    suspend fun addCigarette(timestamp: Long = System.currentTimeMillis()): Long
    fun getAllCigarettes(): Flow<List<Cigarette>>
    suspend fun getLastCigarette(): Cigarette?
    fun getTodayCount(dayStartHour: Int): Flow<Int>
    suspend fun getStatistics(dayStartHour: Int): Statistics

    fun getSettings(): Flow<Settings>
    suspend fun updateCigarettePrice(price: Float)
    suspend fun updatePackSize(size: Int)
    suspend fun updateDailyTarget(target: Int)
    suspend fun updateDayStartHour(hour: Int)
    suspend fun updateDesiredInterval(interval: Int)
}
