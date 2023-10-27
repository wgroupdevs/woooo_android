package eu.siacs.conversations.ui.wallet

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.siacs.conversations.R
import eu.siacs.conversations.entities.Account
import eu.siacs.conversations.http.model.wallet.BlockChainAPIModel
import eu.siacs.conversations.http.model.wallet.CryptoNetwork
import eu.siacs.conversations.http.model.wallet.PaymentReqModel
import eu.siacs.conversations.http.model.wallet.WalletOverviewApiModel
import eu.siacs.conversations.http.model.wallet.WalletOverviewData
import eu.siacs.conversations.http.services.BaseModelAPIResponse
import eu.siacs.conversations.http.services.WooAPIService
import eu.siacs.conversations.persistance.WOOPrefManager
import eu.siacs.conversations.ui.MainActivity
import eu.siacs.conversations.ui.MainActivity.Companion.account
import io.metamask.androidsdk.Dapp
import io.metamask.androidsdk.ErrorType
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.EthereumMethod
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.EthereumState
import io.metamask.androidsdk.Logger
import io.metamask.androidsdk.Network
import io.metamask.androidsdk.RequestError
import kotlinx.coroutines.flow.MutableStateFlow
import java.math.BigDecimal
import java.util.Timer
import java.util.TimerTask
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val ethereum: Ethereum,
    private val wooPrefManager: WOOPrefManager,
) : ViewModel(), WooAPIService.OnGetBlockChainApiResult,
    WooAPIService.OnWalletOverviewAPiResult,
    WooAPIService.OnUpdateWalletAddressResult,
    WooAPIService.OnCreatePaymentResult {
    val TAG = "EthereumViewModel_TAG"
    private val wooAPIService: WooAPIService = WooAPIService.getInstance()
    var isWalletConnected = false;
    val ethereumState = MediatorLiveData<EthereumState>().apply {
        addSource(ethereum.ethereumState) { newEthereumState ->
            value = newEthereumState
        }
    }
    val chainList = MutableStateFlow(emptyList<CryptoNetwork>())
    val walletOverviewData = MutableStateFlow(WalletOverviewData())
    val dapp = Dapp("WOOOO", "https://woooo.world")

    init {
        Log.d(TAG, "EthereumViewModel started...")
        getWalletOverViewData()
    }



    private fun getWalletOverViewData() {
        Log.d(TAG, "getWalletOverViewData Started....")

        wooAPIService.getWalletOverviewData(account.accountId, this)
    }


    fun getChainID(): String {
        if (ethereum.chainId.isBlank()) {
            return "0x810"
        }
        return ethereum.chainId
    }


//    private fun getWalletBalance() {
//        val params: List<String> = listOf(ethereum.selectedAddress, "latest")
//
//        val getBalanceRequest = EthereumRequest(
//            "",
//            EthereumMethod.ETH_GET_BALANCE.value, params
//        )
//        ethereum.sendRequest(getBalanceRequest) { result ->
//            Log.d(TAG, "getBalance1: " + result)
//            if (result is RequestError) {
//                balance = result.toString()
//                Log.d(TAG, "getBalance2: " + balance)
//            } else {
//                balance = result.toString()
//                Log.d(TAG, "getBalance3: " + balance)
//            }
//        }
//    }


    fun connect(
        onSuccess: (message: String) -> Unit,
        onError: (message: String) -> Unit
    ) {
        disconnect()

        Log.d(TAG, "connecting started...")

        ethereum.connect(dapp) { result ->
            if (result is RequestError) {
                Logger.log("Ethereum connection error: ${result.message}")
                onError(result.message)
            } else {
                isWalletConnected = true
                Logger.log("Ethereum connection result: $result")
                val walletAddress = wooPrefManager.getString(WOOPrefManager.WALLET_ADDRESS_KEY, "")
                if (walletAddress != result) {
                    Logger.log("updateWalletAddress CALLED: $result")
                    wooPrefManager.saveString(WOOPrefManager.WALLET_ADDRESS_KEY, result.toString())
                    wooAPIService.updateWalletAddress(account?.accountId, result.toString(), this)
                }
                onSuccess(result.toString())
            }
        }
    }

    fun disconnect() {
        ethereum.disconnect()
    }

    fun clearSession() {
        ethereum.clearSession()
    }

    fun signMessage(
        message: String,
        address: String,
        onSuccess: (Any?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val params: List<String> = listOf(address, message)

        val signRequest = EthereumRequest(
            UUID.randomUUID().toString(),
            EthereumMethod.ETH_SIGN_TYPED_DATA_V4.value,
            params
        )

        ethereum.sendRequest(signRequest) { result ->
            if (result is RequestError) {
                onError(result.message)
                Logger.log("Ethereum sign error: ${result.message}")
            } else {
                Logger.log("Ethereum sign result: $result")
                onSuccess(result)
            }
        }
    }

    fun sendTransaction(
        amount: String, // Amount in Ether as a string
        from: String,
        to: String,
        onSuccess: (Any?) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val etherAmount = BigDecimal(amount) // Convert the input amount to BigDecimal
        val weiAmount =
            etherAmount.multiply(BigDecimal("1000000000000000000")).toBigInteger() // Convert to Wei

        val params: MutableMap<String, Any> = mutableMapOf(
            "from" to from,
            "to" to to,
            "value" to "0x${weiAmount.toString(16)}" // The value field should be in Wei and as a hexadecimal string
        )

        Logger.log("EthereumRequest PARAMS : $params")

        val transactionRequest = EthereumRequest(
            UUID.randomUUID().toString(),
            EthereumMethod.ETH_SEND_TRANSACTION.value,
            listOf(params)
        )



        ethereum.sendRequest(transactionRequest) { result ->
            if (result is RequestError) {
                Logger.log("Ethereum transaction error: ${result.message}")
                onError(result.message)
            } else {
                Logger.log("Ethereum transaction result: $result")
                onSuccess(result)
            }
        }
    }


    fun createPayment(payment: PaymentReqModel) {
        wooAPIService.createPayment(payment, this@WalletViewModel)

    }

    fun switchChain(
        chainId: String,
        onSuccess: (message: String) -> Unit,
        onError: (message: String, action: (() -> Unit)?) -> Unit
    ) {
        val switchChainParams: Map<String, String> = mapOf("chainId" to chainId)

        val switchChainRequest = EthereumRequest(
            method = EthereumMethod.SWITCH_ETHEREUM_CHAIN.value,
            params = listOf(switchChainParams)
        )
        ethereum.sendRequest(switchChainRequest) { result ->
            if (result is RequestError) {
                if (result.code == ErrorType.UNRECOGNIZED_CHAIN_ID.code || result.code == ErrorType.SERVER_ERROR.code) {
                    val message =
                        "${Network.chainNameFor(chainId)} ($chainId) has not been added to your MetaMask wallet. Add chain?"

                    val action: () -> Unit = {
                        addEthereumChain(
                            chainId,
                            onSuccess = { result ->
                                onSuccess(result)
                            },
                            onError = { error ->
                                onError(error, null)
                            }
                        )
                    }
                    onError(message, action)
                } else {
                    onError("Switch chain error: ${result.message}", null)
                }
            } else {
                onSuccess("Successfully switched to ${Network.chainNameFor(chainId)} ($chainId)")
            }
        }
    }

    private fun addEthereumChain(
        chainId: String,
        onSuccess: (message: String) -> Unit,
        onError: (message: String) -> Unit
    ) {
        Logger.log("Adding chainId: $chainId")

        val addChainParams: Map<String, Any> = mapOf(
            "chainId" to chainId,
            "chainName" to Network.chainNameFor(chainId),
            "rpcUrls" to Network.rpcUrls(Network.fromChainId(chainId))
        )
        val addChainRequest = EthereumRequest(
            method = EthereumMethod.ADD_ETHEREUM_CHAIN.value,
            params = listOf(addChainParams)
        )

        ethereum.sendRequest(addChainRequest) { result ->
            if (result is RequestError) {
                onError("Add chain error: ${result.message}")
            } else {
                if (chainId == ethereum.chainId) {
                    onSuccess("Successfully switched to ${Network.chainNameFor(chainId)} ($chainId)")
                } else {
                    onSuccess("Successfully added ${Network.chainNameFor(chainId)} ($chainId)")
                }
            }
        }
    }


    override fun <T : Any?> OnGetBlockChainResultFound(result: T) {
        when (result) {
            is BlockChainAPIModel -> {
                if (result.success == true) {
                    chainList.value = result.data
                    Log.d(TAG, "OnGetBlockChainResultFound: ${result.data.size}")
                }
            }

            else -> {
                Log.d(TAG, "ECEPTION FOUND... $result")
            }
        }
    }

    override fun <T : Any?> onWalletOverviewResultFound(result: T) {
        when (result) {
            is WalletOverviewApiModel -> {
                if (result.success == true) {
                    result.data?.let {
                        walletOverviewData.value = result.data!!

                        Log.d(TAG, "onWalletOverviewResultFound ${result?.data?.wallet?.size}")

                    }

                }
            }

            else -> {
                Log.d(TAG, "ECEPTION FOUND... $result")

                if (result is BaseModelAPIResponse) {
                    Log.d(TAG, "ECEPTION FOUND... ${result.Message}")
                }

            }
        }
    }

    override fun <T : Any?> onUpdateWalletAddressFound(result: T) {
    }

    override fun <T : Any?> onCreatePaymentFound(result: T) {

        when (result) {
            is BaseModelAPIResponse -> {
                if (result.Success) {
                    getWalletOverViewData()
                }
                Log.d(TAG, "onCreatePaymentFoundECEPTION FOUND... ${result.Message}")
            }

            else -> {
                Log.d(TAG, "onCreatePaymentFoundECEPTION FOUND... $result")

            }
        }


    }


    fun showWalletNotConnectedDialog(
        context: Context,
        title: String = "",
        onSuccess: (message: String) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.popup_dialog_theme)
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.title_des_ok_dialog, null)

        val okButton = customView.findViewById<Button>(R.id.okay_btn)
        val titleTV = customView.findViewById<TextView>(R.id.signup_title)
        val desTV = customView.findViewById<TextView>(R.id.signup_description)
        if (title.isBlank()) {
            titleTV.text = "Wallet Alert"
        } else {
            titleTV.text = title
        }
        desTV.text = "To proceed, please connect to Wallet."

        // Set the custom layout to the dialog
        alertDialogBuilder.setView(customView)
        // Create and show the AlertDialog
        val alertDialog = alertDialogBuilder.create()
        okButton.setOnClickListener {
            alertDialog.cancel()
            connect(
                onSuccess = onSuccess,
                onError = onError
            )
        }

        alertDialog.show()


    }


}