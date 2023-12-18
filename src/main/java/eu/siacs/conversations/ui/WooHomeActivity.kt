package eu.siacs.conversations.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.alphawallet.app.ui.WalletHomeActivity
import com.alphawallet.app.viewmodel.CreateWalletViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.woooapp.meeting.impl.utils.WDirector
import dagger.hilt.android.AndroidEntryPoint
import eu.siacs.conversations.Config
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivityHomeBinding
import eu.siacs.conversations.entities.Account
import eu.siacs.conversations.entities.Conversation
import eu.siacs.conversations.entities.Message
import eu.siacs.conversations.services.XmppConnectionService
import eu.siacs.conversations.services.XmppConnectionService.OnConversationUpdate
import eu.siacs.conversations.ui.adapter.CallLogAdapter
import eu.siacs.conversations.ui.adapter.ConversationAdapter
import eu.siacs.conversations.ui.interfaces.OnConversationSelected
import eu.siacs.conversations.ui.util.AvatarWorkerTask
import java.util.Arrays


@AndroidEntryPoint
class WooHomeActivity : XmppActivity(), XmppConnectionService.OnAccountUpdate,
    OnConversationSelected, OnConversationUpdate {

    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var pieChart: PieChart? = null
    private var circleIndex = 0
    val REQUEST_CALL_Permission = 0x213

    private var conversationsAdapter: ConversationAdapter? = null
    private var callLogAdapter: CallLogAdapter? = null
    private var conversations: List<Conversation> = java.util.ArrayList();
    private var callLogs: List<Message> = java.util.ArrayList();

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var callLogsRecyclerView: RecyclerView


    companion object {
        var mAccount: Account? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        populateCircularMenu()
        setSupportActionBar(homeBinding.appBarHome.toolbar.toolbar)
        homeBinding.appBarHome.toolbar.toolBarLeadingView.visibility = View.GONE
        homeBinding.appBarHome.toolbar.toolbarProfilePhoto.visibility = View.VISIBLE
        homeBinding.appBarHome.toolbar.toolbarNotification.visibility = View.VISIBLE
        chatRecyclerView = homeBinding.appBarHome.homeBottomSheet.homeBottomSheetChatRecyclerView
        callLogsRecyclerView =
            homeBinding.appBarHome.homeBottomSheet.homeBottomSheetCallLogsRecyclerView

//        crashChecker = CrashChecker(this)
//        crashChecker?.checkForCrash()

        WDirector.getInstance().generateMeetingId()

        homeBinding.appBarHome.toolbar.toolbarProfilePhoto.setOnClickListener {
            homeBinding.drawerLayout.open()
        }

        bottomSheetBehavior =
            BottomSheetBehavior.from(homeBinding.appBarHome.homeBottomSheet.bottomSheet)
        initBottomSheet()

        populatePIChart()

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(eu.siacs.conversations.R.menu.home, menu)
//        return true
//    }


    override fun onResume() {
        super.onResume()
        checkPermission();
    }

    private fun initBottomSheet() {

        initConversationsView()
        initCallLogsView()

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {}

                    BottomSheetBehavior.STATE_EXPANDED -> {}

                    BottomSheetBehavior.STATE_DRAGGING -> {}

                    BottomSheetBehavior.STATE_SETTLING -> {}

                    BottomSheetBehavior.STATE_HIDDEN -> {}

                    else -> {}
                }
            }
        })
    }

    private fun initConversationsView() {
        conversationsAdapter = ConversationAdapter(this, conversations,true)
        chatRecyclerView.adapter = conversationsAdapter;
        conversationsAdapter?.setConversationClickListener { view: View?, conversation: Conversation ->
            val newIntent = Intent(this@WooHomeActivity, ConversationActivity::class.java);
            newIntent.putExtra(ConversationsActivity.EXTRA_CONVERSATION, conversation.uuid)
            startActivity(newIntent)
        }
    }

    private fun initCallLogsView() {
        this.callLogAdapter = CallLogAdapter(this, this.callLogs, true)
        this.callLogsRecyclerView.adapter = this.callLogAdapter
    }

    private fun populatePIChart() {
        pieChart = homeBinding.appBarHome.pieChart

        // Create a list of PieEntries

        // Create a list of PieEntries
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(25f, ""))
        entries.add(PieEntry(25f, ""))
        entries.add(PieEntry(25f, ""))
        entries.add(PieEntry(25f, ""))

        // Create a PieDataSet
        val dataSet = PieDataSet(entries, "")
        dataSet.setColors(
            Color.TRANSPARENT,
            Color.TRANSPARENT,
            Color.TRANSPARENT,
            Color.TRANSPARENT
        )
        dataSet.setDrawValues(false)
        // Create a PieData object
        val data = PieData(dataSet)
        // Set the data for the PieChart
        pieChart?.data = data

        // Customize the PieChart

        // Customize the PieChart
        pieChart?.holeRadius = 65f // Set the radius of the center hole (0f for no hole)
        pieChart?.setHoleColor(Color.TRANSPARENT)
        pieChart?.transparentCircleRadius = 35f // Set the radius of the transparent circle
        pieChart?.legend?.isEnabled = false
        pieChart?.description?.isEnabled = false
        pieChart?.setDrawEntryLabels(false)


    }

    private fun updatePiChart(reset: Boolean = false) {
        val colors: MutableList<Int>? = pieChart?.data?.getDataSetByIndex(0)?.colors
        if (reset) {
            colors?.set(0, Color.TRANSPARENT)
            colors?.set(1, Color.TRANSPARENT)
            colors?.set(2, Color.TRANSPARENT)
            colors?.set(3, Color.TRANSPARENT)
        } else {
            colors?.set(
                circleIndex,
                resources.getColor(R.color.white_a50)
            )
        }
        pieChart?.invalidate()
    }

    override fun onSupportNavigateUp(): Boolean {
        return false
    }

    override fun refreshUiReal() {

    }

    override fun onBackendConnected() {
        xmppConnectionService?.let {
            if (it.accounts.isNotEmpty()) {
                mAccount = it.accounts[0]
                runOnUiThread {
                    populateView()
                    fetchConversations()
                    fetchCallLogs()
                }
            }
        }
    }

    private fun fetchConversations() {
        this.xmppConnectionService.populateWithOrderedConversations(this.conversations)
        this.conversationsAdapter?.notifyDataSetChanged()
    }

    private fun fetchCallLogs() {
        this.xmppConnectionService.getRTPMessages(this.callLogs)
        Log.d(TAG, "CALL LOGS COUNT : " + callLogs.size)
        this.callLogAdapter?.notifyDataSetChanged()
    }

    private fun populateView() {
        mAccount?.let {
            AvatarWorkerTask.loadAvatar(
                it,
                homeBinding.appBarHome.toolbar.toolbarProfilePhoto,
                R.dimen.avatar
            )

            homeBinding.appBarHome.toolbar.toolbarSearch.setOnClickListener {
                startActivity(Intent(this, SearchActivity::class.java))
            }

            homeBinding.appBarHome.displayName.text = "Hi, ${getDisplayName()}"

            val headerLayout: View = homeBinding.navView.getHeaderView(0) // 0-index header
            val nameTextView = headerLayout.findViewById<TextView>(R.id.header_displayName_tv)
            val emailTextView = headerLayout.findViewById<TextView>(R.id.header_email_tv)
            val profileView = headerLayout.findViewById<ImageView>(R.id.header_profile_iv)
            val editProfileButton =
                headerLayout.findViewById<LinearLayout>(R.id.header_edit_profile_bt)

            val logoutButton =
                headerLayout.findViewById<LinearLayout>(R.id.header_logout_bt)
            nameTextView.text = getDisplayName()
            emailTextView.text = "${mAccount?.userEmail}"
            AvatarWorkerTask.loadAvatar(
                it,
                profileView,
                R.dimen.avatar
            )

            editProfileButton.setOnClickListener {
                homeBinding.drawerLayout.close()
                val intent = Intent(this, EditAccountActivity::class.java)
                intent.putExtra("jid", mAccount?.jid?.asBareJid().toString())
                intent.putExtra("init", false)
                startActivity(intent)
            }

            logoutButton.setOnClickListener {
                var createWallet: CreateWalletViewModel? = null
                //detect previous launch
                createWallet = ViewModelProvider(this)[CreateWalletViewModel::class.java]
                createWallet.cleanAuxData(applicationContext)
                createWallet.clearDatabase()


                xmppConnectionService.logoutAndSave(true);
                xmppConnectionService.databaseBackend.clearDatabase()
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
            }
        }

    }

    private fun getDisplayName(): String {
        return mAccount?.displayName ?: "${mAccount?.firstName} ${mAccount?.lastName}"
    }

    private fun populateCircularMenu() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        homeBinding.appBarHome.outerWheel.layoutParams.height = (0.9 * width).toInt()
        homeBinding.appBarHome.outerWheel.layoutParams.width = (0.9 * width).toInt()
        homeBinding.appBarHome.middleWheelLayout.layoutParams.height = (0.7 * width).toInt()
        homeBinding.appBarHome.middleWheelLayout.layoutParams.width = (0.7 * width).toInt()
        homeBinding.appBarHome.innerWheel.layoutParams.height = (0.33 * width).toInt()
        homeBinding.appBarHome.innerWheel.layoutParams.width = (0.33 * width).toInt()

        homeBinding.appBarHome.meetingIv.setOnClickListener {
            rotateOuterCircle(false, 1)
            circleIndex = 0
            updatePiChart()

        }

        homeBinding.appBarHome.walletIv.setOnClickListener {

            rotateOuterCircle(false, 2)
            circleIndex = 1
            updatePiChart()
        }
        homeBinding.appBarHome.callIv.setOnClickListener {
            rotateOuterCircle(true, 3)
            circleIndex = 2
            updatePiChart()
        }
        homeBinding.appBarHome.chatIv.setOnClickListener {
            rotateOuterCircle(true, 4)
            circleIndex = 3
            updatePiChart()
        }
        homeBinding.appBarHome.innerWheel.setOnClickListener {
            rotateOuterCircle(true, 0)
        }


    }

    private fun rotateOuterCircle(clockwise: Boolean, index: Int) {

        val degree = 90f;
        val duration: Long = 1000;
        val rotateAnimatorClockWise: ObjectAnimator?
        val rotateAnimatorAntiClockWise: ObjectAnimator?;

        if (clockwise) {
            rotateAnimatorClockWise =
                ObjectAnimator.ofFloat(homeBinding.appBarHome.outerWheel, View.ROTATION, 0f, degree)
            rotateAnimatorAntiClockWise =
                ObjectAnimator.ofFloat(
                    homeBinding.appBarHome.middleWheel,
                    View.ROTATION,
                    0f,
                    -degree
                )
        } else {
            rotateAnimatorClockWise =
                ObjectAnimator.ofFloat(
                    homeBinding.appBarHome.outerWheel,
                    View.ROTATION,
                    0f,
                    -degree
                )
            rotateAnimatorAntiClockWise =
                ObjectAnimator.ofFloat(
                    homeBinding.appBarHome.middleWheel,
                    View.ROTATION,
                    0f,
                    degree
                )
        }

        rotateAnimatorClockWise.duration = duration
        rotateAnimatorAntiClockWise.duration = duration
        rotateAnimatorAntiClockWise.start()
        rotateAnimatorClockWise.start()

        rotateAnimatorClockWise.addListener(
            onEnd = {
                updatePiChart(true)
                if (index > 0) {
                    goToActivity(index)
                } else {
                    var newIntent = Intent(this, MiningActivity::class.java);
                    startActivity(newIntent)

                }
            })
    }


    private fun goToActivity(index: Int) {
        var newIntent = Intent(this, ConversationActivity::class.java);
        if (index == 2) {
            newIntent = Intent(
                this,
                WalletHomeActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        newIntent.putExtra(ConversationsActivity.EXTRA_CIRCLE_MENU_INDEX, index)
        startActivity(newIntent)
    }


    override fun onAccountUpdate() {
        runOnUiThread {
            xmppConnectionService?.let {
                if (it.accounts.isNotEmpty()) {
                    mAccount = it.accounts.first()
                    runOnUiThread {
                        populateView()
                    }
                }
            }
            populateView()
        }
    }


    private fun checkPermission() {

        val permissions: List<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Arrays.asList(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            Arrays.asList(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
        }

        hasPermissions(REQUEST_CALL_Permission, permissions)

    }

    private fun hasPermissions(requestCode: Int, permissions: List<String>): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val missingPermissions: MutableList<String> = ArrayList()
            for (permission in permissions) {
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU || Config.ONLY_INTERNAL_STORAGE) && permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    continue
                }
                if (this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    missingPermissions.add(permission)
                }
            }
            if (missingPermissions.size == 0) {
                true
            } else {
                requestPermissions(missingPermissions.toTypedArray<String>(), requestCode)
                false
            }
        } else {
            true
        }
    }

    override fun onConversationSelected(conversation: Conversation?) {
        TODO("Not yet implemented")
    }

    override fun onConversationUpdate() {
        runOnUiThread {
            fetchConversations()
            fetchCallLogs()
        }
    }


}























