package eu.siacs.conversations.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.alphawallet.app.entity.Wallet
import com.alphawallet.app.interact.FetchWalletsInteract
import com.alphawallet.app.viewmodel.CreateWalletViewModel
import com.alphawallet.app.viewmodel.WalletConnectViewModel
import com.alphawallet.app.viewmodel.WalletHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import eu.siacs.conversations.entities.Account
import eu.siacs.conversations.http.services.WooAPIService
import eu.siacs.conversations.persistance.WOOPrefManager
import eu.siacs.conversations.services.XmppConnectionService.OnAccountUpdate
import io.reactivex.SingleObserver
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
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
    }



}