package com.smokecalculator.di

import com.smokecalculator.data.repository.CigaretteRepositoryImpl
import com.smokecalculator.domain.repository.CigaretteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCigaretteRepository(
        repositoryImpl: CigaretteRepositoryImpl
    ): CigaretteRepository
}
