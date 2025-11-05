package com.smokecalculator.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val CIGARETTE_PRICE = floatPreferencesKey("cigarette_price")
        val PACK_SIZE = intPreferencesKey("pack_size")
        val DAILY_TARGET = intPreferencesKey("daily_target")
        val DAY_START_HOUR = intPreferencesKey("day_start_hour")
        val DESIRED_INTERVAL = intPreferencesKey("desired_interval")
    }

    val cigarettePrice: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[Keys.CIGARETTE_PRICE] ?: 10.0f
    }

    val packSize: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[Keys.PACK_SIZE] ?: 20
    }

    val dailyTarget: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[Keys.DAILY_TARGET] ?: 10
    }

    val dayStartHour: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[Keys.DAY_START_HOUR] ?: 5
    }

    val desiredInterval: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[Keys.DESIRED_INTERVAL] ?: 60
    }

    suspend fun setCigarettePrice(price: Float) {
        context.dataStore.edit { preferences ->
            preferences[Keys.CIGARETTE_PRICE] = price
        }
    }

    suspend fun setPackSize(size: Int) {
        context.dataStore.edit { preferences ->
            preferences[Keys.PACK_SIZE] = size
        }
    }

    suspend fun setDailyTarget(target: Int) {
        context.dataStore.edit { preferences ->
            preferences[Keys.DAILY_TARGET] = target
        }
    }

    suspend fun setDayStartHour(hour: Int) {
        context.dataStore.edit { preferences ->
            preferences[Keys.DAY_START_HOUR] = hour
        }
    }

    suspend fun setDesiredInterval(interval: Int) {
        context.dataStore.edit { preferences ->
            preferences[Keys.DESIRED_INTERVAL] = interval
        }
    }
}
