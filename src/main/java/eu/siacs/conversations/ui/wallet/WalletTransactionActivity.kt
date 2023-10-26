package eu.siacs.conversations.ui.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.siacs.conversations.databinding.ActivityWalletTransactionBinding
import eu.siacs.conversations.ui.WalletMainFragment
import eu.siacs.conversations.ui.adapter.WalletTransactionAdapter

class WalletTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalletTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backArrow.setOnClickListener { finish() }

        val payments = WalletMainFragment.walletViewModel.walletOverviewData.value.payments

        if (payments.isNotEmpty()) {

            val transactionAdapter = WalletTransactionAdapter(this, payments)
            binding.trRecyclerview.adapter = transactionAdapter
        }


    }
}