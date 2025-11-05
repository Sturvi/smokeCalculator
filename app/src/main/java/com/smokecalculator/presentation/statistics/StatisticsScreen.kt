package com.smokecalculator.presentation.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smokecalculator.R

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel
) {
    val statistics by viewModel.statistics.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.nav_statistics),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        StatisticCard(
            title = stringResource(R.string.stats_today),
            value = statistics.today.toString(),
            unit = stringResource(R.string.cigarettes)
        )

        StatisticCard(
            title = stringResource(R.string.stats_avg_day),
            value = String.format("%.1f", statistics.averagePerDay),
            unit = stringResource(R.string.cigarettes)
        )

        StatisticCard(
            title = stringResource(R.string.stats_avg_week),
            value = String.format("%.1f", statistics.averagePerWeek),
            unit = stringResource(R.string.cigarettes)
        )

        StatisticCard(
            title = stringResource(R.string.stats_avg_month),
            value = String.format("%.1f", statistics.averagePerMonth),
            unit = stringResource(R.string.cigarettes)
        )
    }
}

@Composable
private fun StatisticCard(
    title: String,
    value: String,
    unit: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
