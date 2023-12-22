package eu.siacs.conversations.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivityMiningBinding
import eu.siacs.conversations.ui.adapter.DailyRewardAdapter


class MiningActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMiningBinding
    private lateinit var dailyRewardAdapter: DailyRewardAdapter
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


        dailyRewardAdapter = DailyRewardAdapter(listOf(1, 2, 3, 4, 5, 6, 7))

        binding.dailyRewardRecyclerView.adapter = dailyRewardAdapter

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                // set the limitations for the numeric
                // text under the progress bar
                if (i <= 100) {
                    binding.progressText.setText("" + i)
                    binding.progressBar.setProgress(i)
                    i++
                    handler.postDelayed(this, 50)
                } else {
                    handler.removeCallbacks(this)
                }
            }
        }, 50)

        initBarchart()

    }


    fun initBarchart() {
        val barChart = binding.miningBarchart

        val entries: MutableList<BarEntry> = ArrayList()
        entries.add(BarEntry(1f, 4f))
        entries.add(BarEntry(2f, 7f))
        entries.add(BarEntry(3f, 3f))
        entries.add(BarEntry(4f, 9f))

        val dataSet = BarDataSet(entries, "Minning")
        dataSet.color =
            resources.getColor(R.color.cyan) // Customize the bar color if needed

        val barData = BarData(dataSet)
        barChart.data = barData

        // Customize X-axis labels
        // Customize X-axis labels
        val xAxis = barChart.xAxis
        xAxis.granularity = 1f;
        xAxis.setCenterAxisLabels(true);
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                // Convert float value to your desired X-axis label
                return when (value.toInt()) {
                    1 -> "Chat"
                    2 -> "Call"
                    3 -> "Meeting"
                    4 -> "Wallet"
                    else -> ""
                }
            }
        }

        // Customize Y-axis
        barChart.axisLeft.setAxisMinimum(0F);
        barChart.axisRight.setDrawLabels(false);
        barChart.legend.isEnabled = false


        // Optional: Customize chart appearance
        barChart.description.isEnabled = false;
        barChart.setFitBars(true);


        // Customize background grid color
        barChart.setDrawGridBackground(true);
        barChart.setGridBackgroundColor(resources.getColor(R.color.blue_primary300));

        barChart.invalidate(); // Refresh the chart


    }
}