package com.smokecalculator.domain.model

data class Settings(
    val cigarettePrice: Float = 10.0f,
    val packSize: Int = 20,
    val dailyTarget: Int = 10,
    val dayStartHour: Int = 5,
    val desiredInterval: Int = 60
)
