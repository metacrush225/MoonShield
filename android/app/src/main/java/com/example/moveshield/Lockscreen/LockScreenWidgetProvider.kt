package com.example.moveshield.Lockscreen

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log


class LockScreenWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context,appWidgetManager: AppWidgetManager,appWidgetIds: IntArray) {
        Log.d("STARTNOTSITCKYY", "onUpdate called" )

    }

    override fun onDisabled(context: Context) {
        // Do nothing
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.d("STARTNOTSITCKYY", "onReceive called" )

        // Handle widget click events here
    }
}