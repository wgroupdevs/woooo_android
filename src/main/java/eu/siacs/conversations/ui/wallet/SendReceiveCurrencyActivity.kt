package eu.siacs.conversations.ui.wallet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivitySendReceiveCurrencyBinding
import eu.siacs.conversations.http.model.wallet.Currency
import eu.siacs.conversations.http.model.wallet.PaymentReqModel
import eu.siacs.conversations.http.model.wallet.Wallet
import eu.siacs.conversations.services.BarcodeProvider
import eu.siacs.conversations.ui.MainActivity.Companion.account
import eu.siacs.conversations.ui.MainActivity.Companion.walletViewModel

class SendReceiveCurrencyActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendReceiveCurrencyBinding
    private var walletAddress: String = ""
    lateinit var bitmap: Bitmap
    private var wallet: Wallet? = null

    // on below line we are creating
    // a variable for qr encoder.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendReceiveCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backArrow.setOnClickListener { finish() }
        Glide.with(this).load(chain.imgURL ?: "").into(
            binding.chainIc
        )
        binding.sendLabel.setOnClickListener {
            binding.sendLabel.background = resources.getDrawable(R.drawable.button)
            binding.receiveLabel.background = resources.getDrawable(R.drawable.bg_border)
            binding.sendCurrencyView.visibility = View.VISIBLE
            binding.receiveCurrencyView.visibility = View.GONE
        }
        binding.receiveLabel.setOnClickListener {
            binding.receiveLabel.background = resources.getDrawable(R.drawable.button)
            binding.sendLabel.background = resources.getDrawable(R.drawable.bg_border)
            binding.receiveCurrencyView.visibility = View.VISIBLE
            binding.sendCurrencyView.visibility = View.GONE

        }
        binding.sendCurrencyBtn.setOnClickListener {

            if (walletViewModel.isWalletConnected) {
                sendTransaction()
            } else {
                walletViewModel.showWalletNotConnectedDialog(this, onSuccess = {}, onError = {})
            }
        }
        binding.copyAddressBtn.setOnClickListener {
            if (walletViewModel.isWalletConnected) {
                copyTextToClipboard(this, walletAddress)
            } else {
                walletViewModel.showWalletNotConnectedDialog(this, onSuccess = {}, onError = {})
            }
        }

        getWalletByCurrency()
    }


    private fun getWalletByCurrency() {
        //        find-currency-wallet
        wallet = walletViewModel.walletOverviewData.value.wallet.firstOrNull {
            chain.code == it.currency
        }
        updateWalletBalance()

    }

    private fun updateWalletBalance() {

        wallet?.let {

            binding.balanceTv.text = "${it.balance}"
        }

    }

    private fun copyTextToClipboard(context: Context, text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // Create a ClipData object to hold the text
        val clipData = ClipData.newPlainText("Wallet Address ", text)
        // Set the data on the clipboard
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()

    }

    override fun onStart() {
        super.onStart()

        generateQRCOde()
        walletViewModel.ethereumState.observe(this) { states ->
            run {
                if (states.selectedAddress.isNotBlank()) {
                    walletAddress = states.selectedAddress
                    binding.walletAddressTv.text = walletAddress

                }
            }
        }

        binding.addressEt.setText(receiverWalletAddress)
    }


    private fun generateQRCOde() {
        // on below line we are getting service for window manager
        val windowManager: WindowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val display: Display = windowManager.defaultDisplay

        val point: Point = Point()
        display.getSize(point)

        val width = point.x
        val height = point.y

        var dimen = if (width < height) width else height
        dimen = dimen * 3 / 4

        // on below line we are initializing our qr encoder
        if (walletAddress.isBlank()) {
//            walletAddress = "0xb39FE02E0dd2115666EA3dee275C497EF28148b9"
            walletAddress = "No wallet address found"
        }
        try {
            bitmap = BarcodeProvider.create2dBarcodeBitmap(walletAddress, dimen)
            binding.walletAddressQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    companion object {
        lateinit var chain: Currency
        var receiverWalletAddress: String = ""
        val TAG = "SendCurrency_TAG"
    }


    private fun sendTransaction() {
        var amount = binding.amountEt.text?.trim().toString()
        val from = walletAddress
        val to = binding.addressEt.text?.trim().toString()
        if (to.isBlank()) {
            Toast.makeText(this, "Enter recipient wallet address", Toast.LENGTH_LONG).show()
            return
        } else if (amount.isBlank()) {
            Toast.makeText(this, "Enter amount", Toast.LENGTH_LONG).show()
            return
        }

        walletViewModel.sendTransaction(
            amount,
            from,
            to,
            onSuccess = {
                Log.d(TAG, "sendTransaction onSuccess : $it")

                val payment = PaymentReqModel(
                    accountId = account?.accountId,
                    currency = chain.code,
                    transactionHash = it.toString(),
                    amount = amount.toDouble(),
                    paymentType = TransactionType.WITHDRAW.name
                )

                walletViewModel.createPayment(payment)


            }, onError = {
                Log.d(TAG, "sendTransaction onError : $it")

            })

        binding.amountEt.text?.clear()
        binding.addressEt.text?.clear()

    }

}