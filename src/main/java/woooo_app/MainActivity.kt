package woooo_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.AndroidEntryPoint
import eu.siacs.conversations.http.model.UserBasicInfo
import kotlinx.coroutines.runBlocking
import woooo_app.woooo.NavGraphs
import woooo_app.woooo.destinations.ForgotPasswordScreenDestination
import woooo_app.woooo.destinations.HomeScreenDestination
import woooo_app.woooo.destinations.SignUpScreenDestination
import woooo_app.woooo.goToWelcomeActivity
import woooo_app.woooo.shared.components.view_models.UserPreferencesViewModel
import woooo_app.woooo.theme.Woooo_androidTheme
import woooo_app.woooo.utils.CONST_KEY_INTENT
import woooo_app.woooo.utils.FIRST_NAME
import woooo_app.woooo.utils.FORGOT_PASSWORD_INTENT
import woooo_app.woooo.utils.HOME_INTENT
import woooo_app.woooo.utils.SIGNUP_INTENT
import woooo_app.woooo.utils.USER_INFO_KEY_INTENT
import woooo_app.woooo.utils.USER_JID
import woooo_app.woooo.utils.USER_TOKEN_KEY_INTENT

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val TAG = "MainActivityLOGS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val navIntentConst = intent?.getStringExtra("navIntentConst")

        Log.d(TAG,navIntentConst.toString())

    }

    @Composable
    fun MainScreen() {
        val context = LocalContext.current
        val navController = rememberNavController()

        val userPreferences: UserPreferencesViewModel = hiltViewModel()

        var startRoute = NavGraphs.root.startRoute

        val navIntent = intent?.getStringExtra(CONST_KEY_INTENT)


        Log.d(TAG,"INTENT : " + navIntent.toString())


        Woooo_androidTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary),
            ) {

//                navigateTo(navController = navController, startRoute = startRoute)

                if (navIntent.isNullOrBlank()) {
                    if (getDataPreferences(userPreferences).isEmpty()) {
                        goToWelcomeActivity(context)
                    } else {
                        startRoute = HomeScreenDestination
                        navigateTo(navController = navController,startRoute = startRoute)
                    }
                }

                navIntent?.let {
                    when (it) {
                        SIGNUP_INTENT -> {
                            startRoute = SignUpScreenDestination
                            navigateTo(navController = navController,startRoute = startRoute)
                            Log.d(TAG,"SHOW SIGNUP VIEW")
                        }

                        FORGOT_PASSWORD_INTENT -> {
                            startRoute = ForgotPasswordScreenDestination
                            navigateTo(navController = navController,startRoute = startRoute)
                            Log.d(TAG,"SHOW FORGOT PASSWORD VIEW")
                        }

                        HOME_INTENT -> {
                            runBlocking {
                                var userInfo = intent?.getSerializableExtra(USER_INFO_KEY_INTENT)
                                var token = intent?.getStringExtra(USER_TOKEN_KEY_INTENT)

                                if (userInfo != null && token != null) {
                                    userInfo = userInfo as UserBasicInfo
                                    Log.d(TAG,"Token : $token")
                                    Log.d(TAG,"AccountId : " + userInfo.accountId)
                                    Log.d(TAG,"Email : " + userInfo.email)
                                    Log.d(TAG,"PhoneNumber : " + userInfo.phoneNumber)
                                    Log.d(TAG,"jid : " + userInfo.jid)
                                    Log.d(TAG,"FirstName : " + userInfo.firstName)
                                    Log.d(TAG,"LastName : " + userInfo.lastName)
                                    Log.d(TAG,"DOB : " + userInfo.dateOfBirth)

                                    saveUserInfoToPreferences(userPreferences,token,userInfo)
                                    getDataPreferences(userPreferences)
                                }

                                startRoute = HomeScreenDestination
                                Log.d(TAG,"SHOW Home Page VIEW")
                            }
                            navigateTo(navController = navController,startRoute = startRoute)
                        }

                        else -> {
                            if (getDataPreferences(userPreferences).isEmpty()) {
                                Log.d(TAG,"Auth Token not found")
                                goToWelcomeActivity(context)
                            } else {
                                Log.d(TAG,"AuthToken : Found")
                                navigateTo(navController = navController,startRoute = startRoute)
                            }
                        }
                    }
                }

            }
        }


    }

    fun getDataPreferences(userPreferences: UserPreferencesViewModel): String = runBlocking {
        USER_JID = userPreferences.getJID()
        FIRST_NAME = userPreferences.getFirstName()
        userPreferences.getAuthToke()

    }

    private suspend fun saveUserInfoToPreferences(
        userPreferences: UserPreferencesViewModel,token: String,user: UserBasicInfo
    ) {
        userPreferences.setAuthToken(token)
        userPreferences.setEmail(user.email)
        userPreferences.setFirstName(user.firstName)
        userPreferences.setLastName(user.lastName)
        userPreferences.setPhone(user.phoneNumber)
        userPreferences.setProfileImage(user.imageURL)
        userPreferences.setJID(user.jid)
        userPreferences.setAbout(user.about)
        userPreferences.setAddress(user.address)
        userPreferences.setPostalCode(user.postalCode?:"")
        userPreferences.setLanguage(user.language)
        userPreferences.setLanguageCode(user.languageCode)
        userPreferences.setDOB(user.dob)


    }

    @Composable
    fun navigateTo(navController: NavHostController,startRoute: Route) {
        DestinationsNavHost(
            navController = navController,navGraph = NavGraphs.root,startRoute = startRoute
        )
    }

}