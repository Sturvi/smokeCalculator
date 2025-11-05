package com.smokecalculator.domain.model

data class Statistics(
    val today: Int = 0,
    val averagePerDay: Float = 0f,
    val averagePerWeek: Float = 0f,
    val averagePerMonth: Float = 0f
)
