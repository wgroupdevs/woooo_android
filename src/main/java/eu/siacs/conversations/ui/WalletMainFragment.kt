package eu.siacs.conversations.ui

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import eu.siacs.conversations.R
import eu.siacs.conversations.ui.wallet.WalletMainViewModel


class WalletMainFragment : XmppFragment() {

    companion object {
        fun newInstance() = WalletMainFragment()
    }

    private var activity: XmppActivity? = null

    private lateinit var viewModel: WalletMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet_main, container, false)
    }

    override fun refresh() {

    }

    override fun onBackendConnected() {
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if (activity is XmppActivity) {
            this.activity = activity
        } else {
            throw IllegalStateException("Trying to attach fragment to activity that is not an XmppActivity")
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get a reference to the PieChart view from your XML layout
        // Get a reference to the PieChart view from your XML layout
        val pieChart: PieChart? = view?.findViewById(R.id.pieChart)

        // Create a list of PieEntries

        // Create a list of PieEntries
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(60f, ""))
        entries.add(PieEntry(20f, ""))
        entries.add(PieEntry(10f, ""))
        entries.add(PieEntry(10f, ""))

        // Create a PieDataSet
        val dataSet = PieDataSet(entries, "Pie Chart Example")
        dataSet.setColors(Color.YELLOW, Color.GREEN, Color.LTGRAY, Color.CYAN)
        dataSet.valueTextSize = 12f

        // Create a PieData object

        // Create a PieData object
        val data = PieData(dataSet)
//        data.setValueFormatter(PercentFormatter(pieChart))
//        data.setValueTextSize(12f)


        // Set the data for the PieChart

        // Set the data for the PieChart
        pieChart!!.data = data

        // Customize the PieChart

        // Customize the PieChart
        pieChart.holeRadius = 60f // Set the radius of the center hole (0f for no hole)
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.centerText = "WOO\n2.0"
        pieChart.setCenterTextSize(25f)
        pieChart.setCenterTextColor(Color.WHITE)
        pieChart.transparentCircleRadius = 35f // Set the radius of the transparent circle
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false

        // Refresh the chart to apply changes
        pieChart.invalidate()


        val languages = resources.getStringArray(R.array.Currency)


        // access the spinner
        val spinner = view.findViewById<Spinner>(R.id.currency_spinner)
        if (spinner != null) {
            val adapter = activity?.let {
                ArrayAdapter(
                    it,
                    R.layout.spinner_text, languages
                )
            }
            adapter?.setDropDownViewResource(R.layout.simple_spinner_dropdown);

            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


    }

}