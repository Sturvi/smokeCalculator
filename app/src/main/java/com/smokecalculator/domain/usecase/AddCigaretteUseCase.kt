package com.smokecalculator.domain.usecase

import com.smokecalculator.domain.repository.CigaretteRepository
import javax.inject.Inject

class AddCigaretteUseCase @Inject constructor(
    private val repository: CigaretteRepository
) {
    suspend operator fun invoke(timestamp: Long = System.currentTimeMillis()): Long {
        return repository.addCigarette(timestamp)
    }
}
