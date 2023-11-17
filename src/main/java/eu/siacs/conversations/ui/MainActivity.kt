package eu.siacs.conversations.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.alphawallet.app.viewmodel.CustomNetworkViewModel
import com.alphawallet.app.viewmodel.WalletConnectViewModel
import com.alphawallet.app.viewmodel.WalletHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import eu.siacs.conversations.entities.Account
import eu.siacs.conversations.http.services.WooAPIService
import eu.siacs.conversations.persistance.WOOPrefManager
import eu.siacs.conversations.services.XmppConnectionService.OnAccountUpdate
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : XmppActivity(), OnAccountUpdate {
    val TAG = "MainActivityLOGS"
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
                Log.d(TAG, "USER TOKEN FROM PREF : $token")
                WooAPIService.resetWooAPIService()

                mAccount?.let {
                    account = mAccount!!
                }
//                init Wallet
                inItWalletHomeViewModel()

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
        lateinit var viewModelWH: WalletHomeViewModel
        lateinit var viewModelWC: WalletConnectViewModel
        var account: Account? = null
    }


    private fun inItWalletHomeViewModel() {
        viewModelWH = ViewModelProvider(this)[WalletHomeViewModel::class.java]
        viewModelWC = ViewModelProvider(this)[WalletConnectViewModel::class.java]


    }


    private fun saveWooNetwork() {

        val customNetworkViewModel = ViewModelProvider(this)
            .get<CustomNetworkViewModel>(CustomNetworkViewModel::class.java)

        Log.d(TAG, "saveWooNetwork");

        customNetworkViewModel.saveNetwork(
            false,
            "Woooo",
            "https://dataseed.woooo.world",
            2064,
            "WOO",
            "https://scan.woooo.world/",
            "https://block.woooo.world/api-docs",
            false,
            null
        )

//        val list = arrayListOf<Long>(2064)
//
//        val networkToggleViewModel: NetworkToggleViewModel = ViewModelProvider(this)
//            .get<NetworkToggleViewModel>(NetworkToggleViewModel::class.java)
//
//        networkToggleViewModel.setFilterNetworks(list, true, false)

    }


}