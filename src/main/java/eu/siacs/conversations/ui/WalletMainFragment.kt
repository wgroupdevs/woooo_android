package eu.siacs.conversations.ui

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.FragmentWalletMainBinding
import eu.siacs.conversations.http.model.wallet.Currency
import eu.siacs.conversations.http.model.wallet.Wallet


class WalletMainFragment : XmppFragment() {
    val TAG = "WalletMainFragment_TAG";

    private lateinit var binding: FragmentWalletMainBinding
    private var activity: ConversationsActivity? = null


    private var currentChain: Currency? = null
    var selectedWallet: Wallet? = null
    var selectedCurrency: Currency? = null
    private var progressDialog: ProgressDialog? = null

    companion object {
        private const val REQUEST_SCAN_QR_CODE = 0x0001
        private const val REQUEST_CAMERA_PERMISSIONS_TO_SCAN = 0x0002
    }


    private fun showProgressDialog() {
        progressDialog = ProgressDialog(requireActivity())
        progressDialog?.setMessage("Please wait ...")
        progressDialog?.setCancelable(true) // blocks UI interaction
        progressDialog?.show()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        this.activity?.binding?.toolbar?.toolbarSearch?.visibility = View.GONE
        this.activity?.binding?.toolbar?.walletMainAppBar?.visibility = View.VISIBLE

    }

    override fun refresh() {

    }


    override fun onBackendConnected() {
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is ConversationsActivity) {
            this.activity = activity


        } else {
            throw IllegalStateException("Trying to attach fragment to activity that is not an XmppActivity")
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.activity?.binding?.toolbar?.toolbarSearch?.visibility = View.VISIBLE
        this.activity?.binding?.toolbar?.walletMainAppBar?.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        piChartViewSettings()
        populateCurrencyDropDown()


//        apply-font to pi chart
        val tf = Typeface.createFromAsset(resources.assets, "nasalization.otf")
        binding.pieChart.setCenterTextTypeface(tf)
    }




    private fun piChartViewSettings() {

        binding.pieChart.holeRadius = 60f
        binding.pieChart.setHoleColor(Color.TRANSPARENT)
        binding.pieChart.transparentCircleRadius = 35f
        binding.pieChart.legend.isEnabled = false
        binding.pieChart.description.isEnabled = false
    }

    private fun showEmptyPiChart() {
        val pieChart: PieChart = binding.pieChart
        val entries = ArrayList<PieEntry>()
        val colorList = ArrayList<Int>()
        entries.add(PieEntry(100f))
        colorList.add(Color.YELLOW)
        // Create a PieDataSet
        val dataSet = PieDataSet(entries, "Wallet")
        dataSet.valueTextSize = 0f
        dataSet.colors = colorList
        dataSet.valueTextSize = 12f
        dataSet.setDrawValues(false)
        val data = PieData(dataSet)
        pieChart.data = data
        updatePIChart()
    }

    private fun updatePIChart() {
        Log.d(TAG, "SELECTED WALLET : ${selectedWallet?.currency} : ${selectedWallet?.balance}")

        val pieChart: PieChart = binding.pieChart
        pieChart.centerText =
            "${selectedWallet?.currency ?: "WOO"}\n${selectedWallet?.balance ?: "0.0"}"
        pieChart.setCenterTextSize(25f)
        pieChart.setCenterTextColor(Color.WHITE)
        pieChart.invalidate()
        populateCurrencyDropDown()


    }


    private fun populateCurrencyDropDown() {

        val currencyList = resources.getStringArray(R.array.Currency)


        // access the spinner
        val spinner = binding.currencySpinner
        if (spinner != null) {
            val adapter = activity?.let {
                ArrayAdapter(
                    it,
                    R.layout.spinner_text, currencyList
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
                    updateCurrencyRate(currencyList[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }


    private fun updateCurrencyRate(currency: String) {

        when (currency) {
            "USD" -> {
                binding.currencyRate.text = "${selectedCurrency?.rateUSD ?: "0.0"}"
            }

            "YEN" -> {
                binding.currencyRate.text = "${selectedCurrency?.rateYEN ?: "0.0"}"
            }

            "EURO" -> {
                binding.currencyRate.text = "${selectedCurrency?.rateEUR ?: "0.0"}"
            }
        }


    }


    private fun scaneQR() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val intent = Intent(requireActivity(), ScanActivity::class.java)
            startActivityForResult(intent, REQUEST_SCAN_QR_CODE)
        } else {

            requireActivity().requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSIONS_TO_SCAN
            )
        }

    }



    private fun isValidCryptoAddress(address: String): Boolean {
        // Bitcoin addresses start with '1', '3', or 'bc1'
        val bitcoinPattern =
            "^(1|3|[13][a-km-zA-HJ-NP-Z2-9]{25,34}|bc1[ac-hj-np-z02-9]{25,39})$".toRegex()
        // Ethereum addresses are 40 characters long and start with '0x'
        val ethereumPattern = "^0x[0-9a-fA-F]{40}$".toRegex()
        // You can add more patterns for other cryptocurrency here...
        return bitcoinPattern.matches(address) || ethereumPattern.matches(address)
        // Add more checks for other cryptocurrency here...
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == REQUEST_CAMERA_PERMISSIONS_TO_SCAN) {
                    scaneQR()
                }
            } else {
                Toast.makeText(
                    activity,
                    R.string.qr_code_scanner_needs_access_to_camera,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }




}