package eu.siacs.conversations.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
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
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.FragmentWalletMainBinding
import eu.siacs.conversations.http.model.wallet.Currency
import eu.siacs.conversations.http.model.wallet.Wallet
import eu.siacs.conversations.ui.adapter.ChainAdapter
import eu.siacs.conversations.ui.wallet.SendReceiveCurrencyActivity
import eu.siacs.conversations.ui.wallet.WalletTransactionActivity
import eu.siacs.conversations.ui.wallet.WalletViewModel
import kotlinx.coroutines.launch


@AndroidEntryPoint
class WalletMainFragment : XmppFragment() {
    val TAG = "WalletMainFragment_TAG";

    private lateinit var binding: FragmentWalletMainBinding
    private var activity: ConversationsActivity? = null


    private var currentChain: Currency? = null
    var selectedWallet: Wallet? = null
    var selectedCurrency: Currency? = null
    private var progressDialog: ProgressDialog? = null


    companion object {
        lateinit var walletViewModel: WalletViewModel
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
        updateAppBar()

    }

    override fun refresh() {

    }


    override fun onBackendConnected() {
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is ConversationsActivity) {
            this.activity = activity

            if (!walletViewModel.isWalletConnected) {

//                showWalletNotConnectedDialog(requireActivity())
            }

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
        binding.transactionBtn.setOnClickListener {
            val transactionIntent = Intent(getActivity(), WalletTransactionActivity::class.java)
            startActivity(transactionIntent)
        }
        observeWalletConnection()
        populatePIChart()
        populateCurrencyDropDown()


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                walletViewModel.walletOverviewData.collect {
                    // New location! Update the map
                    Log.d(TAG, "currency Count... ${it.currency.size}")
                    Log.d(TAG, "wallet Count... ${it.wallet.size}")
                    if (it.wallet.isNotEmpty()) {
                        populatePIChart()
                        if (progressDialog?.isShowing == true) {
                            progressDialog?.hide()
                        }
                    } else {
                        showProgressDialog()
                    }
                    if (it.currency.isNotEmpty()) {
                        refreshCurrencyView()
                    }
                    updateAppBar()
                }
            }
        }

        this.activity?.binding?.toolbar?.qrCodeScanner?.setOnClickListener {
            if (walletViewModel.isWalletConnected) {
                scaneQR()
            } else {
                showWalletNotConnectedDialog(requireActivity())
            }
        }

//        apply-font to pi chart
        val tf = Typeface.createFromAsset(resources.assets, "nasalization.otf")
        binding.pieChart.setCenterTextTypeface(tf)
    }


    private fun observeWalletConnection() {
        walletViewModel.ethereumState.observe(viewLifecycleOwner) { states ->
            run {
                Log.d(TAG, "CURRENT CHAIN ID : ${walletViewModel.getChainID()}")
                Log.d(TAG, "ETHEREUM STATES : $states")
                if (states.selectedAddress.isBlank()) {
                    this.activity?.binding?.toolbar?.connectWalletView?.visibility = View.VISIBLE
                    this.activity?.binding?.toolbar?.currentChainView?.visibility = View.GONE
                    this.activity?.binding?.toolbar?.connectWalletView?.setOnClickListener {
                        walletViewModel.connect(
                            onSuccess = {
                                Log.d(TAG, "Wallet connected successfully $it")
                                updateAppBar()
                            },
                            onError = { msg ->
                                Log.d(TAG, "Wallet Connection Error $msg")
                            })
                    }

                } else {

                    walletViewModel.isWalletConnected = true
                    this.activity?.binding?.toolbar?.connectWalletView?.visibility = View.GONE
                    this.activity?.binding?.toolbar?.currentChainView?.visibility = View.VISIBLE
                    updateAppBar()
                }


            }
        }
    }


    private fun updateAppBar() {
        currentChain =
            walletViewModel.walletOverviewData.value.currency.firstOrNull { it.hexId == walletViewModel.getChainID() }
        currentChain?.let {
            this.activity?.binding?.toolbar?.currentChainView?.setOnClickListener {
                showSelectChainDialog(this.requireActivity())
            }
            this.activity?.binding?.toolbar?.chainName?.text = it.name
            this.activity?.binding?.toolbar?.chainIc?.let { imageView ->
                Glide.with(this).load(it.imgURL ?: "").into(
                    imageView
                )
            }
        }
    }

    private fun populatePIChart() {
        val pieChart: PieChart = binding.pieChart

        val entries = ArrayList<PieEntry>()
        val colorList = ArrayList<Int>()

        if (walletViewModel.walletOverviewData.value.wallet.isEmpty()) {
            entries.add(PieEntry(100f))
            colorList.add(Color.YELLOW)
        } else {
            // Set up an OnChartValueSelectedListener to handle click events
            pieChart?.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {

                    if (e != null && h != null) {

                        val currency = e.data ?: "WOO"

                        selectedWallet =
                            walletViewModel.walletOverviewData.value.wallet.firstOrNull { it.currency == currency }

                        selectedCurrency =
                            walletViewModel.walletOverviewData.value.currency.firstOrNull { it.code == currency }


                        if (selectedCurrency == null || selectedWallet == null)
                            return

                        updatePIChart()
                    }
                }

                override fun onNothingSelected() {
                    // Handle when nothing is selected (e.g., deselect)
                }
            })
        }

        for (wallet in walletViewModel.walletOverviewData.value.wallet) {
            var walletBalance = wallet.balance ?: 0.0
            if (walletBalance == 0.0) {
                continue
            }
            if (wallet.currency == "WOO") {
                selectedWallet = wallet
            }
            entries.add(PieEntry(walletBalance.toFloat(), "", wallet.currency))
            colorList.add(Color.parseColor(wallet.colorCode))


            walletViewModel.walletOverviewData.value.currency.firstOrNull {
                wallet.currency == it.code
            }?.let { currency ->
                if (currency.code == "WOO") {
                    selectedCurrency = currency
                }
            }
        }

        if (entries.isEmpty()) {
            entries.add(PieEntry(100f))
            colorList.add(Color.YELLOW)
        }

        Log.d(TAG, "PICHART-ENTRIES : ${entries}")

        // Create a PieDataSet
        val dataSet = PieDataSet(entries, "Wallet")
        dataSet.valueTextSize = 0f
        dataSet.colors = colorList
        dataSet.valueTextSize = 12f
        dataSet.setDrawValues(false)

        // Create a PieData object

        // Create a PieData object
        val data = PieData(dataSet)

        pieChart.data = data


        pieChart.holeRadius = 60f // Set the radius of the center hole (0f for no hole)
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.transparentCircleRadius = 35f // Set the radius of the transparent circle
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false


        // Refresh the chart to apply changes
        updatePIChart()
    }


    private fun updatePIChart() {

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


    private fun showSelectChainDialog(context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.popup_dialog_theme)
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.switch_chain_dialog, null)
        val recycler = customView.findViewById<RecyclerView>(R.id.chainRecyclerview)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler.layoutManager = layoutManager

        val chainAdapter = ChainAdapter(
            context = context,
            chainID = walletViewModel.getChainID(),
            showOnDialog = true,
            chainList = walletViewModel.walletOverviewData.value.currency
        )
        recycler.adapter = chainAdapter


        recycler.addItemDecoration(
            DividerItemDecoration(
                context,
                layoutManager.orientation
            )
        )

        // Set the custom layout to the dialog
        alertDialogBuilder.setView(customView)

        // Create and show the AlertDialog
        val alertDialog = alertDialogBuilder.create()
        chainAdapter.onItemClick = { chain ->
            chain.hexId?.let {
                walletViewModel.switchChain(
                    it,
                    onSuccess = { result ->
                        Log.d(TAG, "SWITCH CHAIN onSuccess : $result")
                    },
                    onError = { message, action ->
                        Log.d(TAG, "SWITCH CHAIN onError : $message")
                    })
            }
            alertDialog.cancel()

        }
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window!!.attributes)
        layoutParams.width = (resources.displayMetrics.widthPixels * 0.5).toInt()
        layoutParams.height = (resources.displayMetrics.heightPixels * 0.7).toInt()
        alertDialog.window!!.attributes = layoutParams
        alertDialog.show()

    }

    private fun refreshCurrencyView() {

        val chainAdapter = ChainAdapter(
            context = this.requireActivity(),
            showOnDialog = false,
            chainList = walletViewModel.walletOverviewData.value.currency
        )
        binding.chainRecyclerview.adapter = chainAdapter
        chainAdapter.onItemClick = { chain ->
            run {
                val transactionIntent =
                    Intent(getActivity(), SendReceiveCurrencyActivity::class.java)
                SendReceiveCurrencyActivity.chain = chain
                SendReceiveCurrencyActivity.walletViewModel = walletViewModel
                startActivity(transactionIntent)
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


    private fun handleWalletAddress(result: String) {
        Log.d(TAG, "REQUEST_SCAN_QR_CODE : $result")
        if (isValidCryptoAddress(result)) {

            val transactionIntent =
                Intent(getActivity(), SendReceiveCurrencyActivity::class.java)
            SendReceiveCurrencyActivity.chain = selectedCurrency!!
            SendReceiveCurrencyActivity.receiverWalletAddress = result
            SendReceiveCurrencyActivity.walletViewModel = walletViewModel
            startActivity(transactionIntent)


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SCAN_QR_CODE && resultCode == Activity.RESULT_OK) {
            val result: String? = data?.getStringExtra(ScanActivity.INTENT_EXTRA_RESULT)
            handleWalletAddress(result ?: "")
        }

    }


    private fun showWalletNotConnectedDialog(context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.popup_dialog_theme)
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val customView = inflater.inflate(R.layout.title_des_ok_dialog, null)

        val okButton = customView.findViewById<Button>(R.id.okay_btn)
        val titleTV = customView.findViewById<TextView>(R.id.signup_title)
        val desTV = customView.findViewById<TextView>(R.id.signup_description)

        titleTV.text = "Not Connected"
        desTV.text = "To proceed, please connect to MetaMask."


        // Set the custom layout to the dialog
        alertDialogBuilder.setView(customView)

        // Create and show the AlertDialog
        val alertDialog = alertDialogBuilder.create()

        okButton.setOnClickListener {

            alertDialog.cancel()

            walletViewModel.connect(
                onSuccess = {
                    Log.d(TAG, "Wallet connected successfully $it")
                },
                onError = { msg ->
                    Log.d(TAG, "Wallet Connection Error $msg")
                })


        }

        val layoutParams = WindowManager.LayoutParams()

        layoutParams.copyFrom(alertDialog.window!!.attributes)
        layoutParams.width = (resources.displayMetrics.widthPixels * 0.5).toInt()
        layoutParams.height = (resources.displayMetrics.heightPixels * 0.7).toInt()
        alertDialog.window!!.attributes = layoutParams
        alertDialog.show()


    }


}