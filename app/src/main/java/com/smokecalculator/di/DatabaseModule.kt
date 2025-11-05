package com.smokecalculator.di

import android.content.Context
import androidx.room.Room
import com.smokecalculator.data.database.CigaretteDao
import com.smokecalculator.data.database.SmokeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SmokeDatabase {
        return Room.databaseBuilder(
            context,
            SmokeDatabase::class.java,
            "smoke_database"
        ).build()
    }

    @Provides
    fun provideCigaretteDao(database: SmokeDatabase): CigaretteDao {
        return database.cigaretteDao()
    }
}
