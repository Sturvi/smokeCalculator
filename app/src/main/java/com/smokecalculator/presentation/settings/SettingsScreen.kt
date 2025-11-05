package com.smokecalculator.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smokecalculator.R

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    var priceText by remember(settings.cigarettePrice) {
        mutableStateOf(settings.cigarettePrice.toString())
    }
    var packSizeText by remember(settings.packSize) {
        mutableStateOf(settings.packSize.toString())
    }
    var targetText by remember(settings.dailyTarget) {
        mutableStateOf(settings.dailyTarget.toString())
    }
    var hourText by remember(settings.dayStartHour) {
        mutableStateOf(settings.dayStartHour.toString())
    }
    var intervalText by remember(settings.desiredInterval) {
        mutableStateOf(settings.desiredInterval.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = priceText,
            onValueChange = {
                priceText = it
                it.toFloatOrNull()?.let { price ->
                    viewModel.updateCigarettePrice(price)
                }
            },
            label = { Text(stringResource(R.string.cigarette_price)) },
            suffix = { Text(stringResource(R.string.currency_azn)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = packSizeText,
            onValueChange = {
                packSizeText = it
                it.toIntOrNull()?.let { size ->
                    if (size > 0) viewModel.updatePackSize(size)
                }
            },
            label = { Text(stringResource(R.string.pack_size)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = targetText,
            onValueChange = {
                targetText = it
                it.toIntOrNull()?.let { target ->
                    if (target > 0) viewModel.updateDailyTarget(target)
                }
            },
            label = { Text(stringResource(R.string.daily_target)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = hourText,
            onValueChange = {
                hourText = it
                it.toIntOrNull()?.let { hour ->
                    if (hour in 0..23) viewModel.updateDayStartHour(hour)
                }
            },
            label = { Text(stringResource(R.string.day_start_time)) },
            suffix = { Text("hour (0-23)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = intervalText,
            onValueChange = {
                intervalText = it
                it.toIntOrNull()?.let { interval ->
                    if (interval > 0) viewModel.updateDesiredInterval(interval)
                }
            },
            label = { Text(stringResource(R.string.desired_interval)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
