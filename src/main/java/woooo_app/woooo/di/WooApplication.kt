package com.woogroup.woooo_app.woooo.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.mediasoup.droid.Logger
import org.mediasoup.droid.MediasoupClient

@HiltAndroidApp
class WooApplication : Application() {

    companion object {
        var sharedInstance: WooApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        sharedInstance = this

        Logger.setLogLevel(Logger.LogLevel.LOG_DEBUG)
        Logger.setDefaultHandler()
        MediasoupClient.initialize(applicationContext)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

} /** end class. */