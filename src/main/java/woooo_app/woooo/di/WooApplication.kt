package com.woogroup.woooo_app.woooo.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.mediasoup.droid.Logger
import org.mediasoup.droid.MediasoupClient
import pk.muneebahmad.lib.analytics.UnexpectedExceptionHandler

@HiltAndroidApp
class WooApplication : Application() {

    companion object {
        var sharedInstance: WooApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        sharedInstance = this

        Thread.setDefaultUncaughtExceptionHandler(UnexpectedExceptionHandler(applicationContext))
        MediasoupClient.initialize(applicationContext)
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

} /** end class. */