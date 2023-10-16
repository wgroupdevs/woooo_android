package eu.siacs.conversations.ui.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivitySignUpBinding
import eu.siacs.conversations.databinding.ActivityWalletTransactionBinding

class WalletTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalletTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backArrow.setOnClickListener { finish() }
    }
}