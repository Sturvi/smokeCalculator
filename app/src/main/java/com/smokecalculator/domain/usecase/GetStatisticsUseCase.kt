package com.smokecalculator.domain.usecase

import com.smokecalculator.domain.model.Statistics
import com.smokecalculator.domain.repository.CigaretteRepository
import javax.inject.Inject

class GetStatisticsUseCase @Inject constructor(
    private val repository: CigaretteRepository
) {
    suspend operator fun invoke(dayStartHour: Int): Statistics {
        return repository.getStatistics(dayStartHour)
    }
}
