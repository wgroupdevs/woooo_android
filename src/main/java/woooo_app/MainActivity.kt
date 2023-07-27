package woooo_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.DestinationsNavHost
import com.wgroup.woooo_app.woooo.theme.Woooo_androidTheme
import dagger.hilt.android.AndroidEntryPoint
import eu.siacs.conversations.ui.WelcomeActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import woooo_app.woooo.NavGraphs
import woooo_app.woooo.data.datasource.local.UserPreferences
import woooo_app.woooo.utils.CONST_KEY_INTENT
import woooo_app.woooo.utils.FORGOT_PASSWORD_INTENT
import woooo_app.woooo.utils.SIGNUP_INTENT
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val TAG = "MainActivityLOGS"

    @Inject
    lateinit var userPreferences: UserPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navIntentConst = intent?.getStringExtra(CONST_KEY_INTENT)

        Log.d(TAG, "INTENT : " + navIntentConst.toString())

        navIntentConst?.let {
            when (it) {
                SIGNUP_INTENT -> {
                    Log.d(TAG, "SHOW SIGNUP VIEW")
                }

                FORGOT_PASSWORD_INTENT -> {
                    Log.d(TAG, "SHOW FORGOT PASSWORD VIEW")
                }

                else -> {}
            }
        }




        setContent {
            MainScreen()
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val navIntentConst = intent?.getStringExtra("navIntentConst")

        Log.d(TAG, navIntentConst.toString())

    }

    @Composable
    fun MainScreen() {
        val context = LocalContext.current
        Woooo_androidTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
            ) {

                runBlocking {
                    launch {

                        if (userPreferences.getAuthToke().isEmpty()) {
                            Log.d(TAG, "Auth Token not found")
                            val intent = Intent(context, WelcomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                            context.startActivity(intent)
                            return@launch

                        } else {
                            Log.d(TAG, "Auth Token found")

                        }
                    }
                }

                DestinationsNavHost(navGraph = NavGraphs.root)
//            ConfirmAccountScreen()
            }
        }

//    context.startActivity(Intent(context,WelcomeActivity::class.java))
    }
}