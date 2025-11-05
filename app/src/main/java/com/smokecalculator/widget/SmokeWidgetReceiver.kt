package com.smokecalculator.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.smokecalculator.R
import com.smokecalculator.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class SmokeWidgetReceiver : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val pendingResult = goAsync()
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

        scope.launch {
            try {
                appWidgetIds.forEach { widgetId ->
                    updateWidget(context, appWidgetManager, widgetId)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_SMOKE_CIGARETTE -> {
                val pendingResult = goAsync()
                val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

                scope.launch {
                    try {
                        WidgetDataProvider.addCigarette(context)
                        updateAllWidgets(context)
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
            ACTION_OPEN_APP -> {
                val appIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                context.startActivity(appIntent)
            }
        }
    }

    private suspend fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        widgetId: Int
    ) {
        val data = WidgetDataProvider.getWidgetData(context)

        val views = RemoteViews(context.packageName, R.layout.smoke_widget_layout).apply {
            setTextViewText(R.id.widget_count, "${data.todayCount}")
            setTextViewText(R.id.widget_timer, data.timeSinceLast)

            setOnClickPendingIntent(
                R.id.widget_smoke_button,
                getPendingIntent(context, ACTION_SMOKE_CIGARETTE)
            )

            setOnClickPendingIntent(
                R.id.widget_open_button,
                getPendingIntent(context, ACTION_OPEN_APP)
            )
        }

        appWidgetManager.updateAppWidget(widgetId, views)
    }

    private fun updateAllWidgets(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, SmokeWidgetReceiver::class.java)
        )
        onUpdate(context, appWidgetManager, widgetIds)
    }

    private fun getPendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, SmokeWidgetReceiver::class.java).apply {
            this.action = action
        }
        return PendingIntent.getBroadcast(
            context,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val ACTION_SMOKE_CIGARETTE = "com.smokecalculator.ACTION_SMOKE_CIGARETTE"
        const val ACTION_OPEN_APP = "com.smokecalculator.ACTION_OPEN_APP"
    }
}
