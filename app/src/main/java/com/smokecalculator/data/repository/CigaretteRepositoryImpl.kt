package com.smokecalculator.data.repository

import com.smokecalculator.data.database.CigaretteDao
import com.smokecalculator.data.database.CigaretteEntity
import com.smokecalculator.data.preferences.UserPreferences
import com.smokecalculator.domain.model.Cigarette
import com.smokecalculator.domain.model.Settings
import com.smokecalculator.domain.model.Statistics
import com.smokecalculator.domain.repository.CigaretteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CigaretteRepositoryImpl @Inject constructor(
    private val dao: CigaretteDao,
    private val preferences: UserPreferences
) : CigaretteRepository {

    override suspend fun addCigarette(timestamp: Long): Long {
        return dao.insert(CigaretteEntity(timestamp = timestamp))
    }

    override fun getAllCigarettes(): Flow<List<Cigarette>> {
        return dao.getAllCigarettes().map { entities ->
            entities.map { Cigarette(it.id, it.timestamp) }
        }
    }

    override suspend fun getLastCigarette(): Cigarette? {
        return dao.getLastCigarette()?.let { Cigarette(it.id, it.timestamp) }
    }

    override fun getTodayCount(dayStartHour: Int): Flow<Int> {
        val startOfDay = getStartOfDay(dayStartHour)
        return dao.getCigaretteCountSinceFlow(startOfDay)
    }

    override suspend fun getStatistics(dayStartHour: Int): Statistics {
        val now = System.currentTimeMillis()
        val startOfDay = getStartOfDay(dayStartHour)
        val startOfWeek = getStartOfWeek(dayStartHour)
        val startOfMonth = getStartOfMonth(dayStartHour)

        val today = dao.getCigaretteCountSince(startOfDay)

        val weekCigarettes = dao.getCigaretteCountSince(startOfWeek)
        val daysInWeek = ((now - startOfWeek) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
        val avgPerWeek = weekCigarettes.toFloat() / daysInWeek

        val monthCigarettes = dao.getCigaretteCountSince(startOfMonth)
        val daysInMonth = ((now - startOfMonth) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
        val avgPerMonth = monthCigarettes.toFloat() / daysInMonth

        // Calculate average per day from all history
        val allCigarettes = dao.getCigaretteCountSince(0)
        val avgPerDay = if (allCigarettes > 0) {
            val allCigarettesList = dao.getAllCigarettes().first()
            val oldestCigarette = allCigarettesList.lastOrNull()
            val days = oldestCigarette?.let {
                ((now - it.timestamp) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
            } ?: 1
            allCigarettes.toFloat() / days
        } else {
            0f
        }

        return Statistics(
            today = today,
            averagePerDay = avgPerDay,
            averagePerWeek = avgPerWeek,
            averagePerMonth = avgPerMonth
        )
    }

    override fun getSettings(): Flow<Settings> {
        return combine(
            preferences.cigarettePrice,
            preferences.packSize,
            preferences.dailyTarget,
            preferences.dayStartHour,
            preferences.desiredInterval
        ) { price, pack, target, dayStart, interval ->
            Settings(price, pack, target, dayStart, interval)
        }
    }

    override suspend fun updateCigarettePrice(price: Float) {
        preferences.setCigarettePrice(price)
    }

    override suspend fun updatePackSize(size: Int) {
        preferences.setPackSize(size)
    }

    override suspend fun updateDailyTarget(target: Int) {
        preferences.setDailyTarget(target)
    }

    override suspend fun updateDayStartHour(hour: Int) {
        preferences.setDayStartHour(hour)
    }

    override suspend fun updateDesiredInterval(interval: Int) {
        preferences.setDesiredInterval(interval)
    }

    private fun getStartOfDay(dayStartHour: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, dayStartHour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.timeInMillis > System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        return calendar.timeInMillis
    }

    private fun getStartOfWeek(dayStartHour: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, dayStartHour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.timeInMillis > System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
        }

        return calendar.timeInMillis
    }

    private fun getStartOfMonth(dayStartHour: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, dayStartHour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.timeInMillis > System.currentTimeMillis()) {
            calendar.add(Calendar.MONTH, -1)
        }

        return calendar.timeInMillis
    }
}
