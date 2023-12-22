package eu.siacs.conversations.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import eu.siacs.conversations.entities.Account
import eu.siacs.conversations.http.services.WooAPIService
import eu.siacs.conversations.persistance.WOOPrefManager
import eu.siacs.conversations.services.XmppConnectionService.OnAccountUpdate
import eu.siacs.conversations.ui.meeting.ScheduleMeetingViewModel
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : XmppActivity(), OnAccountUpdate {
    val TAG = "MainActivity_TAG"
    private var mAccount: Account? = null

    @Inject
    lateinit var wooPrefManager: WOOPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun refreshUiReal() {
    }

    override fun onBackendConnected() {

        Log.d(TAG, "onBackendConnected....")
        xmppConnectionService?.let {
            Log.d(TAG, "xmppConnectionService Initialized")
            Log.d(TAG, "xmppConnectionService ACCOUNT :${it.accounts.size}")

            if (it.accounts.isNotEmpty()) {
                mAccount = it.accounts.first()
            }
            if (mAccount == null) {
                val welcomeIntent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startNewActivity(welcomeIntent)
            } else {

                //reset-WOO-API-SERVICE-WITH_USER_TOKEN
                val token =
                    wooPrefManager.getString(WOOPrefManager.USER_TOKEN_KEY, "")
                WooAPIService.userToken = token
                Log.d(TAG, "ACCOUNT UUID :${mAccount?.accountId}")
                Log.d(TAG, "WALLET-ADDRESS:${mAccount?.walletAddress}")
                WooAPIService.resetWooAPIService()

                mAccount?.let {
                    account = mAccount!!
                    scheduleMeetingViewModel =
                        ViewModelProvider(this)[ScheduleMeetingViewModel::class.java]

                }

                val homeIntent = Intent(this@MainActivity, WooHomeActivity::class.java)
                startNewActivity(homeIntent)
            }

        }
    }

    private fun startNewActivity(intent: Intent) {
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
    }

    override fun onAccountUpdate() {
    }

    companion object {
        var account: Account? = null
        lateinit var scheduleMeetingViewModel: ScheduleMeetingViewModel

    }


}