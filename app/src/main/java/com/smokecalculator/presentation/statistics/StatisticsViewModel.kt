package com.smokecalculator.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smokecalculator.domain.model.Statistics
import com.smokecalculator.domain.usecase.GetSettingsUseCase
import com.smokecalculator.domain.usecase.GetStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getStatisticsUseCase: GetStatisticsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {

    private val _statistics = MutableStateFlow(Statistics())
    val statistics = _statistics.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            val settings = getSettingsUseCase().firstOrNull()
            val dayStartHour = settings?.dayStartHour ?: 5
            _statistics.value = getStatisticsUseCase(dayStartHour)
        }
    }

    fun refresh() {
        loadStatistics()
    }
}
