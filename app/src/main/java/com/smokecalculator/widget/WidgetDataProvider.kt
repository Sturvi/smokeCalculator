package com.smokecalculator.widget

import android.content.Context
import androidx.room.Room
import com.smokecalculator.data.database.CigaretteEntity
import com.smokecalculator.data.database.SmokeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

object WidgetDataProvider {

    data class WidgetData(
        val todayCount: Int,
        val timeSinceLast: String
    )

    suspend fun addCigarette(context: Context) {
        withContext(Dispatchers.IO) {
            val database = getDatabase(context)
            database.cigaretteDao().insert(CigaretteEntity(timestamp = System.currentTimeMillis()))
        }
    }

    suspend fun getWidgetData(context: Context): WidgetData {
        return withContext(Dispatchers.IO) {
            val database = getDatabase(context)
            val dao = database.cigaretteDao()

            val startOfDay = getStartOfDay()
            val todayCount = dao.getCigaretteCountSince(startOfDay)
            val lastCigarette = dao.getLastCigarette()

            val timeSinceLast = if (lastCigarette != null) {
                formatTimeSince(lastCigarette.timestamp)
            } else {
                "--:--"
            }

            WidgetData(todayCount, timeSinceLast)
        }
    }

    private fun getDatabase(context: Context): SmokeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SmokeDatabase::class.java,
            "smoke_database"
        ).build()
    }

    private fun getStartOfDay(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 5)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.timeInMillis > System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        return calendar.timeInMillis
    }

    private fun formatTimeSince(lastTime: Long): String {
        val diffSeconds = (System.currentTimeMillis() - lastTime) / 1000
        val hours = diffSeconds / 3600
        val minutes = (diffSeconds % 3600) / 60

        return if (hours > 0) {
            String.format("%dh %dm", hours, minutes)
        } else {
            String.format("%dm", minutes)
        }
    }
}
