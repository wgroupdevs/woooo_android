package eu.siacs.conversations

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.mediasoup.droid.MediasoupClient

@HiltAndroidApp
class WooApplication : Application() {

    companion object {
        var sharedInstance: WooApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        try {
            sharedInstance = this
            MediasoupClient.initialize(applicationContext)
        } catch (e: Throwable) {

        }

    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

}
/** end class. */