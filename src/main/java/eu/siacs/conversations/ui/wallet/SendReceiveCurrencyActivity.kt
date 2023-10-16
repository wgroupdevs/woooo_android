package eu.siacs.conversations.ui.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivitySendReceiveCurrencyBinding
import eu.siacs.conversations.databinding.ActivityWalletTransactionBinding

class SendReceiveCurrencyActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendReceiveCurrencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySendReceiveCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backArrow.setOnClickListener { finish() }


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


    }
}