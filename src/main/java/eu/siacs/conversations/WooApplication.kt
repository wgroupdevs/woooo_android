package eu.siacs.conversations

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.mediasoup.droid.MediasoupClient
import pk.muneebahmad.lib.analytics.UnexpectedExceptionHandler

@HiltAndroidApp
class WooApplication : Application() {

    companion object {
        var sharedInstance: WooApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        try {
            sharedInstance = this
//            Thread.setDefaultUncaughtExceptionHandler(UnexpectedExceptionHandler(applicationContext))
            MediasoupClient.initialize(applicationContext)
        } catch (e: Throwable) {

        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

}
/** end class. */