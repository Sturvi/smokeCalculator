package com.smokecalculator.domain.usecase

import com.smokecalculator.domain.model.Settings
import com.smokecalculator.domain.repository.CigaretteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: CigaretteRepository
) {
    operator fun invoke(): Flow<Settings> {
        return repository.getSettings()
    }
}
