package woooo_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.Route
import com.wgroup.woooo_app.woooo.theme.Woooo_androidTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import woooo_app.woooo.NavGraphs
import woooo_app.woooo.data.datasource.local.UserPreferences
import woooo_app.woooo.destinations.ForgotPasswordScreenDestination
import woooo_app.woooo.destinations.SignUpScreenDestination
import woooo_app.woooo.goToWelcomeActivity
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
        val navController = rememberNavController()
        // 👇 this avoids a jump in the UI that would happen if we relied only on ShowLoginWhenLoggedOut
        var startRoute = NavGraphs.root.startRoute

        val navIntent = intent?.getStringExtra(CONST_KEY_INTENT)

        Log.d(TAG, "INTENT : " + navIntent.toString())
        startRoute = SignUpScreenDestination

        Woooo_androidTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
            ) {


//                navigateTo(navController = navController, startRoute = startRoute)

                if (navIntent.isNullOrBlank()) {
                    if (getToken().isEmpty()) {
                        goToWelcomeActivity(context)
                        return@Box
                    }
                }

                navIntent?.let {
                    when (it) {
                        SIGNUP_INTENT -> {
                            startRoute = SignUpScreenDestination
                            navigateTo(navController = navController, startRoute = startRoute)
                            Log.d(TAG, "SHOW SIGNUP VIEW")
                        }

                        FORGOT_PASSWORD_INTENT -> {
                            startRoute = ForgotPasswordScreenDestination
                            navigateTo(navController = navController, startRoute = startRoute)
                            Log.d(TAG, "SHOW FORGOT PASSWORD VIEW")
                        }

                        else -> {
                            if (getToken().isEmpty()) {
                                Log.d(TAG, "Auth Token not found")
                                goToWelcomeActivity(context)
                                return@Box
                            } else {
                                Log.d(TAG, "AuthToken : Found")
                                navigateTo(navController = navController, startRoute = startRoute)
                            }
                        }
                    }
                }


            }
        }


    }

    fun getToken(): String = runBlocking {
        userPreferences.getAuthToke()
    }

    @Composable
    fun navigateTo(navController: NavHostController, startRoute: Route) {
        DestinationsNavHost(
            navController = navController,
            navGraph = NavGraphs.root,
            startRoute = startRoute
        )
    }
}