package eu.siacs.conversations.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import eu.siacs.conversations.entities.Account
import eu.siacs.conversations.http.model.UserBasicInfo
import eu.siacs.conversations.services.XmppConnectionService.OnAccountUpdate
import eu.siacs.conversations.services.XmppConnectionService.OnConversationUpdate
import eu.siacs.conversations.services.XmppConnectionService.OnRosterUpdate
import kotlinx.coroutines.runBlocking


class MainActivity : XmppActivity(), OnAccountUpdate {
    val TAG = "MainActivityLOGS"
    private var mAccount: Account? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun refreshUiReal() {
    }

    override fun onBackendConnected() {

        Log.d(TAG,"onBackendConnected....")
        xmppConnectionService?.let {
            Log.d(TAG,"xmppConnectionService Initialized")
            Log.d(TAG,"xmppConnectionService ACCOUNT :${it.accounts.size}")

            if (it.accounts.isNotEmpty()) {
                mAccount = it.accounts.first()
            }
            if (mAccount == null) {
                val welcomeIntent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startNewActivity(welcomeIntent)
            } else {
                val homeIntent = Intent(this@MainActivity, HomeActivity::class.java)
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


}