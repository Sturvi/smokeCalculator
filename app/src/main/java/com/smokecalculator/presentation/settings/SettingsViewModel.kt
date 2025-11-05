package com.smokecalculator.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smokecalculator.domain.model.Settings
import com.smokecalculator.domain.repository.CigaretteRepository
import com.smokecalculator.domain.usecase.GetSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: CigaretteRepository,
    getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {

    val settings: StateFlow<Settings> = getSettingsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Settings())

    fun updateCigarettePrice(price: Float) {
        viewModelScope.launch {
            repository.updateCigarettePrice(price)
        }
    }

    fun updatePackSize(size: Int) {
        viewModelScope.launch {
            repository.updatePackSize(size)
        }
    }

    fun updateDailyTarget(target: Int) {
        viewModelScope.launch {
            repository.updateDailyTarget(target)
        }
    }

    fun updateDayStartHour(hour: Int) {
        viewModelScope.launch {
            repository.updateDayStartHour(hour)
        }
    }

    fun updateDesiredInterval(interval: Int) {
        viewModelScope.launch {
            repository.updateDesiredInterval(interval)
        }
    }
}
