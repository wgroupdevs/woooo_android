package eu.siacs.conversations.ui.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import eu.siacs.conversations.databinding.ActivityWalletTransactionBinding
import eu.siacs.conversations.http.model.wallet.Transaction
import eu.siacs.conversations.http.model.wallet.TransactionAPIModel
import eu.siacs.conversations.http.services.BaseModelAPIResponse
import eu.siacs.conversations.http.services.WooAPIService
import eu.siacs.conversations.ui.MainActivity.Companion.account
import eu.siacs.conversations.ui.adapter.WalletTransactionAdapter

class WalletTransactionActivity : AppCompatActivity(), WooAPIService.OnGetTransactionPiResult {

    private lateinit var binding: ActivityWalletTransactionBinding

    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private lateinit var wooAPIService: WooAPIService
    private val TAG = "TransactionActivity_TAG"
    private lateinit var transactionAdapter: WalletTransactionAdapter
    var transactions: ArrayList<Transaction> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        wooAPIService = WooAPIService.getInstance()
        binding.backArrow.setOnClickListener { finish() }
        getTransactions()
        transactionAdapter = WalletTransactionAdapter(this, transactions)
        binding.trRecyclerview.adapter = transactionAdapter

    }


    private fun getTransactions() {
        isLoading = true;
        account?.let {
            wooAPIService.getTransactions(it.accountId, currentPage, 10, this)
        }
    }

    override fun <T : Any?> onGetTransactionFound(result: T) {
        runOnUiThread {
            isLoading = false

            when (result) {
                is TransactionAPIModel -> {
                    if (result.Success == true) {

                        if (result.transactions.isNotEmpty()) {
                            transactions.addAll(result.transactions)
                            currentPage++
                            transactionAdapter.notifyDataSetChanged()

                        } else {
                            isLastPage = true

                        }

                    }
                    Log.d(TAG, "onGetTransactionFound FOUND... ${result.Message}")

                }

                else -> {
                    Log.d(TAG, "onGetTransactionFound FOUND... $result")

                    if (result is BaseModelAPIResponse) {
                        Log.d(TAG, "ECEPTION FOUND... ${result.Message}")
                    }

                }
            }

        }


    }
}