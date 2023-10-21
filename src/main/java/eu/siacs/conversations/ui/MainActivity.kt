package eu.siacs.conversations.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import eu.siacs.conversations.entities.Account
import eu.siacs.conversations.http.model.UserBasicInfo
import eu.siacs.conversations.services.XmppConnectionService.OnAccountUpdate
import eu.siacs.conversations.services.XmppConnectionService.OnConversationUpdate
import eu.siacs.conversations.services.XmppConnectionService.OnRosterUpdate
import kotlinx.coroutines.runBlocking
import woooo_app.woooo.NavGraphs
import woooo_app.woooo.destinations.ForgotPasswordScreenDestination
import woooo_app.woooo.destinations.HomeScreenDestination
import woooo_app.woooo.destinations.SignUpScreenDestination
import woooo_app.woooo.feature.auth.GV
import woooo_app.woooo.feature.home.viewmodel.HomeViewModel
import woooo_app.woooo.goToWelcomeActivity
import woooo_app.woooo.shared.components.view_models.UserPreferencesViewModel
import woooo_app.woooo.theme.Woooo_androidTheme
import woooo_app.woooo.utils.CONST_KEY_INTENT
import woooo_app.woooo.utils.FORGOT_PASSWORD_INTENT
import woooo_app.woooo.utils.HOME_INTENT
import woooo_app.woooo.utils.SIGNUP_INTENT
import woooo_app.woooo.utils.USER_INFO_KEY_INTENT
import woooo_app.woooo.utils.USER_JID
import woooo_app.woooo.utils.USER_TOKEN_KEY_INTENT

@AndroidEntryPoint
class MainActivity : XmppActivity(), OnAccountUpdate, OnConversationUpdate, OnRosterUpdate {
    val TAG = "MainActivityLOGS"
    private var mAccount: Account? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val navIntentConst = intent?.getStringExtra("navIntentConst")

        Log.d(TAG, navIntentConst.toString())

    }

    // do this instead
    override fun refreshUiReal() {
    }

    override fun onBackendConnected() {
        xmppConnectionService?.let {
            if (it.accounts.isNotEmpty()) {
                mAccount = it.accounts[0]
            }
            Log.d(TAG, "MY_ACCOUNT_COUNT :onBackendConnected  " + mAccount?.displayName);
            setContent {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
//        Log.d(TAG, "homeViewModel : ${homeViewModel.hashCode()}")

        val context = LocalContext.current
        val navController = rememberNavController()
        val userPreferences: UserPreferencesViewModel = hiltViewModel()

        var startRoute = NavGraphs.root.startRoute

        val navIntent = intent?.getStringExtra(CONST_KEY_INTENT)


        Log.d(TAG, "INTENT : " + navIntent.toString())


        Woooo_androidTheme {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary),
            ) {

//                navigateTo(navController = navController, startRoute = startRoute)

                if (navIntent.isNullOrBlank()) {
                    if (mAccount == null) {
                        goToWelcomeActivity(context)
                    } else {
                        runBlocking {
                            GV.getUserProfileImage.value = userPreferences.getProfileImage()
                            GV.getFirstName.value = userPreferences.getFirstName()
                            GV.uniqueId = userPreferences.getAccountUniqueId()

                            Log.d("accountUniqueId", "" + userPreferences.getAccountUniqueId())
                        }
                        startRoute = HomeScreenDestination
                        Log.d(TAG, "SHOW Home Page VIEW")
                        goToHomeActivity()
//                        navigateTo(navController = navController, startRoute = startRoute)
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

                        HOME_INTENT -> {
                            runBlocking {
                                var userInfo = intent?.getSerializableExtra(USER_INFO_KEY_INTENT)
                                var token = intent?.getStringExtra(USER_TOKEN_KEY_INTENT)

                                if (userInfo != null && token != null) {
                                    userInfo = userInfo as UserBasicInfo
                                    saveUserInfoToPreferences(userPreferences, token, userInfo)
                                    getDataPreferences(userPreferences)
                                }

//                                startRoute = HomeScreenDestination
                                goToHomeActivity()

                                Log.d(TAG, "SHOW Home Page VIEW")
                            }
                            navigateTo(navController = navController, startRoute = startRoute)
                        }

                        else -> {
                            if (getDataPreferences(userPreferences).isEmpty()) {
                                Log.d(TAG, "Auth Token not found")
                                goToWelcomeActivity(context)
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


    private fun goToHomeActivity() {

        val intent = Intent(this, HomeActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
    }

    private fun getDataPreferences(userPreferences: UserPreferencesViewModel): String =
        runBlocking {
            GV.getUserProfileImage.value = userPreferences.getProfileImage()
            GV.getFirstName.value = userPreferences.getFirstName()
            GV.uniqueId = userPreferences.getAccountUniqueId()
            USER_JID = userPreferences.getJID()
            userPreferences.getAuthToke()

        }

    private suspend fun saveUserInfoToPreferences(
        userPreferences: UserPreferencesViewModel, token: String, user: UserBasicInfo
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
        userPreferences.setPostalCode(user.postalCode ?: "")
        userPreferences.setLanguage(user.language)
        userPreferences.setLanguageCode(user.languageCode)
        userPreferences.setDOB(user.dob)
        userPreferences.setAccountUniqueId(user.accountId ?: "")
        GV.uniqueId = userPreferences.getAccountUniqueId()
    }

    @Composable
    fun navigateTo(navController: NavHostController, startRoute: Route) {
        DestinationsNavHost(
            navController = navController, navGraph = NavGraphs.root, startRoute = startRoute
        )
    }

    override fun onConversationUpdate() {

    }

    override fun onAccountUpdate() {
    }

    override fun onRosterUpdate() {

    }


}