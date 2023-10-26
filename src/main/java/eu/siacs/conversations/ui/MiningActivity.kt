package eu.siacs.conversations.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivityMiningBinding

class MiningActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMiningBinding
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMiningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.emptyToolBar)
        binding.toolbar.emptyToolBar.setNavigationIcon(R.drawable.back_icon)
        binding.toolbar.emptyToolBar.setNavigationOnClickListener {
            finish()
        }


        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                // set the limitations for the numeric
                // text under the progress bar
                if (i <= 100) {
                    binding.progressText.setText("" + i)
                    binding.progressBar.setProgress(i)
                    i++
                    handler.postDelayed(this, 200)
                } else {
                    handler.removeCallbacks(this)
                }
            }
        }, 200)


    }
}