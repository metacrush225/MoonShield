package com.example.moveshield.Lockscreen

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.RemoteViews
import com.example.moveshield.R


class LockScreenWidgetService : Service() {

    private val mBinder = LockScreenWidgetBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder //instead of null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        // Create an instance of AppWidgetManager
        // Create an instance of AppWidgetManager
        val appWidgetManager = AppWidgetManager.getInstance(this)

        // Get the ComponentName of the AppWidgetProvider you want to update

        // Get the ComponentName of the AppWidgetProvider you want to update
        val componentName = ComponentName(this, LockScreenWidgetProvider::class.java)

        // Get the appWidgetIds of the widgets associated with the ComponentName

        // Get the appWidgetIds of the widgets associated with the ComponentName
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)


        // Create a RemoteViews object to update the widget views

        // Create a RemoteViews object to update the widget views
        val remoteViews = RemoteViews(packageName, R.layout.lock_screen_widget_provider)

        // Update the widgets with the new views

        // Update the widgets with the new views
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)

        // Stop the service

        // Stop the service
        stopSelf()
        return START_NOT_STICKY
    }

    inner class LockScreenWidgetBinder : Binder() {
        fun getService(): LockScreenWidgetService = this@LockScreenWidgetService
    }
}