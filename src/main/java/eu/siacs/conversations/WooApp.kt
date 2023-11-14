package eu.siacs.conversations

import android.app.Activity
import android.app.Application
import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.alphawallet.app.C
import com.alphawallet.app.util.TimberInit
import com.alphawallet.app.walletconnect.AWWalletConnectClient
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.plugins.RxJavaPlugins
import io.realm.Realm
import org.mediasoup.droid.MediasoupClient
import pk.muneebahmad.lib.analytics.UnexpectedExceptionHandler
import timber.log.Timber
import java.util.EmptyStackException
import java.util.Stack
import javax.inject.Inject

@HiltAndroidApp
class WooApp : Application() {
    @Inject
    lateinit var awWalletConnectClient: AWWalletConnectClient

    private val activityStack: Stack<Activity> = Stack()

    companion object {
        private var mInstance: WooApp? = null
        val TAG = "WooApp_TAG"
        fun getInstance(): WooApp? {
            return mInstance
        }
    }

    fun getTopActivity(): Activity? {
        return try {
            activityStack.peek()
        } catch (e: EmptyStackException) {
            null
        }
    }

    override fun onCreate() {
        super.onCreate()
        try {
            mInstance = this
            Realm.init(this)
            TimberInit.configTimber()
            Thread.setDefaultUncaughtExceptionHandler(UnexpectedExceptionHandler(applicationContext))
            MediasoupClient.initialize(applicationContext)


            Log.d(TAG, "Realm initialized.....")
        } catch (e: Throwable) {

        }

        val defaultTheme = PreferenceManager.getDefaultSharedPreferences(this)
            .getInt("theme", C.THEME_AUTO)

        when (defaultTheme) {
            C.THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            C.THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> {
                val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                val mode = uiModeManager.nightMode
                when (mode) {
                    UiModeManager.MODE_NIGHT_YES -> AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                    )

                    UiModeManager.MODE_NIGHT_NO -> AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO
                    )
                }
            }
        }

        RxJavaPlugins.setErrorHandler { Timber.e(it) }

        try {
            awWalletConnectClient.init(this)
        } catch (e: Exception) {
            Timber.tag("WalletConnect").e(e)
        }

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityDestroyed(activity: Activity) {}

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {
                activityStack.push(activity)
            }

            override fun onActivityPaused(activity: Activity) {
                pop()
            }

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        })
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        awWalletConnectClient?.shutdown()
    }

    override fun onTerminate() {
        super.onTerminate()
        activityStack.clear()
        awWalletConnectClient?.shutdown()
    }

    private fun pop() {
        activityStack.pop()
    }
}
