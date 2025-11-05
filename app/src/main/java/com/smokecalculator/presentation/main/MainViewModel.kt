package com.smokecalculator.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smokecalculator.domain.model.Settings
import com.smokecalculator.domain.repository.CigaretteRepository
import com.smokecalculator.domain.usecase.AddCigaretteUseCase
import com.smokecalculator.domain.usecase.GetSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addCigaretteUseCase: AddCigaretteUseCase,
    private val repository: CigaretteRepository,
    getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {

    private val settings = getSettingsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Settings())

    private val _lastCigaretteTime = MutableStateFlow<Long?>(null)
    val lastCigaretteTime = _lastCigaretteTime.asStateFlow()

    val uiState: StateFlow<MainUiState> = combine(
        settings,
        repository.getTodayCount(5)
    ) { settings, todayCount ->
        val remaining = settings.dailyTarget - todayCount
        MainUiState(
            todayCount = todayCount,
            remainingCount = remaining,
            dailyTarget = settings.dailyTarget,
            lastCigaretteTime = _lastCigaretteTime.value
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainUiState()
    )

    init {
        loadLastCigarette()
    }

    fun onSmokeCigarette() {
        viewModelScope.launch {
            addCigaretteUseCase()
            loadLastCigarette()
        }
    }

    private fun loadLastCigarette() {
        viewModelScope.launch {
            val last = repository.getLastCigarette()
            _lastCigaretteTime.value = last?.timestamp
        }
    }
}

data class MainUiState(
    val todayCount: Int = 0,
    val remainingCount: Int = 0,
    val dailyTarget: Int = 10,
    val lastCigaretteTime: Long? = null
)
