package eu.siacs.conversations.ui

import android.Manifest
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.alphawallet.app.C.ErrorCode.UNKNOWN
import com.alphawallet.app.entity.ActivityMeta
import com.alphawallet.app.entity.TransactionMeta
import com.alphawallet.app.entity.Wallet
import com.alphawallet.app.interact.ActivityDataInteract
import com.alphawallet.app.repository.entity.RealmTransaction
import com.alphawallet.app.repository.entity.RealmTransfer
import com.alphawallet.app.ui.WalletHomeActivity
import com.alphawallet.app.ui.widget.adapter.ActivityAdapter
import com.alphawallet.app.ui.widget.entity.TokenTransferData
import com.alphawallet.app.viewmodel.ActivityViewModel
import com.alphawallet.app.viewmodel.CreateWalletViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.woooapp.meeting.impl.utils.WDirector
import com.woooapp.meeting.impl.views.MeetingActivity
import dagger.hilt.android.AndroidEntryPoint
import eu.siacs.conversations.Config
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivityHomeBinding
import eu.siacs.conversations.entities.Account
import eu.siacs.conversations.entities.CallLog
import eu.siacs.conversations.entities.Conversation
import eu.siacs.conversations.entities.Message
import eu.siacs.conversations.http.model.meeting.ScheduleMeetingModel
import eu.siacs.conversations.services.XmppConnectionService
import eu.siacs.conversations.services.XmppConnectionService.OnConversationUpdate
import eu.siacs.conversations.ui.adapter.CallLogAdapter
import eu.siacs.conversations.ui.adapter.CallLogAdapter.OnAudioClickListener
import eu.siacs.conversations.ui.adapter.CallLogAdapter.OnChatClickListener
import eu.siacs.conversations.ui.adapter.CallLogAdapter.OnInfoClickListener
import eu.siacs.conversations.ui.adapter.CallLogAdapter.OnVideoClickListener
import eu.siacs.conversations.ui.adapter.ConversationAdapter
import eu.siacs.conversations.ui.adapter.MeetingHistoryAdapter
import eu.siacs.conversations.ui.adapter.ScheduledMeetingAdapter
import eu.siacs.conversations.ui.adapter.WooNotificationAdapter
import eu.siacs.conversations.ui.util.AvatarWorkerTask
import eu.siacs.conversations.ui.util.PresenceSelector
import eu.siacs.conversations.ui.util.ShareUtil
import eu.siacs.conversations.xml.Namespace
import eu.siacs.conversations.xmpp.Jid
import eu.siacs.conversations.xmpp.jingle.RtpCapability
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import java.util.Arrays


@AndroidEntryPoint
class WooHomeActivity : XmppActivity(), XmppConnectionService.OnAccountUpdate,
    OnConversationUpdate, OnAudioClickListener, OnVideoClickListener, OnChatClickListener,
    OnInfoClickListener, View.OnClickListener, ActivityDataInteract,
    ScheduledMeetingAdapter.OnStartMeetingClickListener {

    private lateinit var homeBinding: ActivityHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var pieChart: PieChart? = null
    private var circleIndex = 0
    val REQUEST_CALL_Permission = 0x213

    private var conversationsAdapter: ConversationAdapter? = null
    private var callLogAdapter: CallLogAdapter? = null
    private var scheduledMeetingAdapter: ScheduledMeetingAdapter? = null
    private var meetingHistoryAdapter: MeetingHistoryAdapter? = null

    private var conversations: List<Conversation> = java.util.ArrayList();
    private var callLogs: List<CallLog> = java.util.ArrayList();

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var callLogsRecyclerView: RecyclerView
    private lateinit var meetingRecyclerView: RecyclerView
    private lateinit var walletActivityRecyclerView: RecyclerView

    private var walletActivityViewModel: ActivityViewModel? = null

    private var walletActivityAdapter: ActivityAdapter? = null
    private var realm: Realm? = null
    private var lastUpdateTime: Long = 0
    private val isVisible = false
    private var checkTimer = false
    private var realmUpdates: RealmResults<RealmTransaction>? = null

    companion object {
        var mAccount: Account? = null
        val TAG = "HomeActivity_TAG"
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
        walletActivityRecyclerView =
            homeBinding.appBarHome.homeBottomSheet.homeBottomSheetWalletActivityRecyclerView

        meetingRecyclerView =
            homeBinding.appBarHome.homeBottomSheet.homeBottomSheetMeetingsRecyclerView



        WDirector.getInstance().generateMeetingId()
        homeBinding.appBarHome.toolbar.toolbarProfilePhoto.setOnClickListener {
            homeBinding.drawerLayout.open()
        }

        bottomSheetBehavior =
            BottomSheetBehavior.from(homeBinding.appBarHome.homeBottomSheet.bottomSheet)
        initWalletActivityViewModel()

        initBottomSheet()

        populatePIChart()



        homeBinding.appBarHome.toolbar.toolbarNotification.setOnClickListener {


            Log.d(TAG, "Click on Notification Icon.....")
            showPopupWindow()
        }

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(eu.siacs.conversations.R.menu.home, menu)
//        return true
//    }

    private fun showPopupWindow() {

        val notificationAdapter = WooNotificationAdapter(listOf(1, 2, 3, 4, 5, 6, 7))


        val builder = AlertDialog.Builder(this, R.style.popup_dialog_theme)
        // Inflate the custom layout
        val inflater = LayoutInflater.from(this)
        val customView: View = inflater.inflate(R.layout.notification_panel_view, null)


        val recyclerView = customView.findViewById<RecyclerView>(R.id.notification_recycler_view)

        recyclerView.adapter = notificationAdapter
        builder.setView(customView)
        val alertDialog = builder.create()
        alertDialog.show()
        val layoutParams = WindowManager.LayoutParams()

        layoutParams.copyFrom(alertDialog.window!!.attributes)
        layoutParams.gravity = Gravity.TOP or Gravity.RIGHT
//        layoutParams.x = 100 // Horizontal offset
//
        layoutParams.y = 150 // Vertical offset

        alertDialog.window!!.attributes = layoutParams
    }


    private fun initWalletActivityViewModel() {

        // Initialize ViewModel
        if (walletActivityViewModel == null) {
            walletActivityViewModel = ViewModelProvider(this)[ActivityViewModel::class.java]

            walletActivityViewModel?.onActivityError()?.observe(this) {
                if (it.code == UNKNOWN) {
                    homeBinding.appBarHome.homeBottomSheet.noTransactionFoundLabel.visibility =
                        View.VISIBLE
                }

            }
            walletActivityViewModel?.defaultWallet()?.observe(
                this
            ) { wallet: Wallet? ->
                if (wallet != null) {
                    this.onDefaultWallet(
                        wallet
                    )
                }
            }
            walletActivityViewModel?.activityItems()?.observe(
                this
            ) { activityItems: Array<ActivityMeta> ->
                this.onItemsLoaded(activityItems)
            }
        }
    }

    private fun onItemsLoaded(activityItems: Array<ActivityMeta>) {

        walletActivityViewModel?.getRealmInstance().use { realm ->
            walletActivityAdapter?.updateActivityItems(
                buildTransactionList(
                    realm!!,
                    activityItems
                ).toTypedArray<ActivityMeta>()
            )
            if (walletActivityAdapter?.isEmpty == true) {
                homeBinding.appBarHome.homeBottomSheet.noTransactionFoundLabel.visibility =
                    View.VISIBLE
            } else {
                homeBinding.appBarHome.homeBottomSheet.noTransactionFoundLabel.visibility =
                    View.GONE
            }
            for (am in activityItems) {
                if (am is TransactionMeta && am.getTimeStampSeconds() > lastUpdateTime) lastUpdateTime =
                    am.getTimeStampSeconds() - 60
            }
        }
//        if (isVisible) startTxListener()
    }

    private fun startTxListener() {

        walletActivityViewModel?.let {

            if (walletActivityViewModel!!.defaultWallet().getValue() == null) return
            if (realm == null || realm!!.isClosed) realm =
                walletActivityViewModel!!.getRealmInstance()
            if (realmUpdates != null) realmUpdates!!.removeAllChangeListeners()
            if (walletActivityViewModel == null || walletActivityViewModel!!.defaultWallet()
                    .getValue() == null || TextUtils.isEmpty(
                    walletActivityViewModel!!.defaultWallet().getValue()?.address
                )
            ) return
            realmUpdates =
                realm!!.where(RealmTransaction::class.java).greaterThan("timeStamp", lastUpdateTime)
                    .findAllAsync()

            realmUpdates?.addChangeListener(RealmChangeListener<RealmResults<RealmTransaction>> { realmTransactions: RealmResults<RealmTransaction> ->
                val metas: MutableList<TransactionMeta> = ArrayList()
                //make list
                if (realmTransactions.size == 0) return@RealmChangeListener
                for (item in realmTransactions) {
                    if (walletActivityViewModel!!.getTokensService().getNetworkFilters()
                            .contains(item.chainId)
                    ) {
                        val newMeta = TransactionMeta(
                            item.hash,
                            item.timeStamp,
                            item.to,
                            item.chainId,
                            item.blockNumber
                        )
                        metas.add(newMeta)
                        lastUpdateTime = newMeta.timeStampSeconds + 1
                    }
                }
                if (metas.size > 0) {
                    val metaArray = metas.toTypedArray<TransactionMeta>()
                    walletActivityAdapter?.updateActivityItems(
                        buildTransactionList(
                            realm!!,
                            metaArray as Array<ActivityMeta>
                        ).toTypedArray<ActivityMeta>()
                    )
//                    systemView.hide()
                }
            })

        }

    }

    private fun buildTransactionList(
        realm: Realm,
        activityItems: Array<ActivityMeta>
    ): List<ActivityMeta> {
        //selectively filter the items with the following rules:
        // - allow through all normal transactions with no token transfer consequences
        // - for any transaction with token transfers; if there's only one token transfer, only show the transfer
        // - for any transaction with more than one token transfer, show the transaction and show the child transfer consequences
        val filteredList: MutableList<ActivityMeta> = ArrayList()
        for (am in activityItems) {
            if (am is TransactionMeta) {
                val tokenTransfers: List<TokenTransferData> = getTokenTransfersForHash(
                    realm,
                    am
                )
                if (tokenTransfers.size != 1) {
                    filteredList.add(am)
                } //only 1 token transfer ? No need to show the underlying transaction
                filteredList.addAll(tokenTransfers)
            }
        }
        return filteredList
    }

    private fun getTokenTransfersForHash(
        realm: Realm,
        tm: TransactionMeta
    ): List<TokenTransferData> {
        val transferData: MutableList<TokenTransferData> = ArrayList()
        //summon realm items
        //get matching entries for this transaction
        val transfers = realm.where(RealmTransfer::class.java)
            .equalTo("hash", RealmTransfer.databaseKey(tm.chainId, tm.hash))
            .findAll()
        if (transfers != null && transfers.size > 0) {
            //list of transfers, descending in time to give ordered list
            var nextTransferTime =
                if (transfers.size == 1) tm.timeStamp else tm.timeStamp - 1 // if there's only 1 transfer, keep the transaction timestamp
            for (rt in transfers) {
                val ttd = TokenTransferData(
                    rt.hash, tm.chainId,
                    rt.tokenAddress, rt.eventName, rt.transferDetail, nextTransferTime
                )
                transferData.add(ttd)
                nextTransferTime--
            }
        }
        return transferData
    }


    private fun onDefaultWallet(wallet: Wallet) {

        Log.d(TAG, "onDefaultWallet : $wallet")


        walletActivityAdapter?.setDefaultWallet(wallet)
    }


    override fun onResume() {
        super.onResume()
        checkPermission();
        if (walletActivityViewModel == null) {
            this.recreate()
        } else {
            walletActivityViewModel?.prepare()
        }

        checkTimer = true
    }

    private fun initBottomSheet() {

        initConversationsView()
        initCallLogsView()
        initMeetingView()
        initWalletActivityView()


        homeBinding.appBarHome.homeBottomSheet.homeSheetNavMeetingBtn.setOnClickListener {
            goToActivity(1)
        }
        homeBinding.appBarHome.homeBottomSheet.homeSheetNavWalletBtn.setOnClickListener {
            goToActivity(2)
        }
        homeBinding.appBarHome.homeBottomSheet.homeSheetNavCallBtn.setOnClickListener {
            goToActivity(3)
        }
        homeBinding.appBarHome.homeBottomSheet.homeSheetNavChatBtn.setOnClickListener {
            goToActivity(4)
        }


        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        homeBinding.appBarHome.homeBottomSheet.tvTitle.text = "Slide up"
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        homeBinding.appBarHome.homeBottomSheet.tvTitle.text = "Slide down"

                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {}

                    BottomSheetBehavior.STATE_SETTLING -> {}

                    BottomSheetBehavior.STATE_HIDDEN -> {}

                    else -> {}
                }
            }
        })
    }

    private fun hideBottomSheet() {
        if (::bottomSheetBehavior.isInitialized) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            Log.d(TAG, "hideBottomSheet ..... ");

        }
    }


    private fun initConversationsView() {
        conversationsAdapter = ConversationAdapter(this, conversations, true)
        chatRecyclerView.adapter = conversationsAdapter;
        conversationsAdapter?.setConversationClickListener { view: View?, conversation: Conversation ->
            goToContactChat(conversation.uuid)
        }
    }

    private fun initWalletActivityView() {
        walletActivityAdapter = ActivityAdapter(
            walletActivityViewModel?.tokensService,
            walletActivityViewModel?.provideTransactionsInteract(),
            walletActivityViewModel?.assetDefinitionService,
            this
        )

        walletActivityRecyclerView.adapter = walletActivityAdapter
    }

    private fun initMeetingView() {

        MainActivity.account?.accountId?.let {
            MainActivity.meetingViewModel.getScheduledNewMeetings(it)
            MainActivity.meetingViewModel.getMeetingHistory(it)
        }
        scheduledMeetingAdapter = ScheduledMeetingAdapter(this, emptyList())
        meetingHistoryAdapter = MeetingHistoryAdapter(this, emptyList())

        scheduledMeetingAdapter?.setOnStartMeetingClickListener(this)
        meetingRecyclerView.adapter = scheduledMeetingAdapter

        MainActivity.meetingViewModel.getScheduledMeetings().observe(this) { meetings ->

            if (meetings.isEmpty()) {
                meetingRecyclerView.adapter = meetingHistoryAdapter

            } else {
                meetingRecyclerView.adapter = scheduledMeetingAdapter
            }

            scheduledMeetingAdapter?.updateList(meetings)
        }

        MainActivity.meetingViewModel.onMeetingHistory().observe(this) { meetings ->
            if (meetings.isEmpty()) {
                homeBinding.appBarHome.homeBottomSheet.noMeetingFoundLabel.visibility = View.VISIBLE
            } else {
                if (scheduledMeetingAdapter?.itemCount!! > 0) {
                    meetingRecyclerView.adapter = scheduledMeetingAdapter
                } else {
                    meetingRecyclerView.adapter = meetingHistoryAdapter
                }

                homeBinding.appBarHome.homeBottomSheet.noMeetingFoundLabel.visibility = View.GONE
            }
            meetingHistoryAdapter?.updateList(meetings)
        }


    }

    private fun initCallLogsView() {
        this.callLogAdapter = CallLogAdapter(this, this.callLogs, true)
        callLogAdapter!!.setAudioClickListener(this)
        callLogAdapter!!.setOnVideoClickListener(this)
        callLogAdapter!!.setOnChatClickListener(this)
        callLogAdapter!!.setOnInfoClickListener(this)
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

        if (this.conversations.isEmpty()) {
            homeBinding.appBarHome.homeBottomSheet.noChatFoundLabel.visibility = View.VISIBLE
        } else {
            homeBinding.appBarHome.homeBottomSheet.noChatFoundLabel.visibility = View.GONE
        }

        this.conversationsAdapter?.notifyDataSetChanged()
    }

    private fun fetchCallLogs() {
        this.xmppConnectionService.getRTPMessages(this.callLogs)

        if (this.callLogs.isEmpty()) {
            homeBinding.appBarHome.homeBottomSheet.noCallFoundLabel.visibility = View.VISIBLE
        } else {
            homeBinding.appBarHome.homeBottomSheet.noCallFoundLabel.visibility = View.GONE
        }


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

            val inviteFriendButton =
                headerLayout.findViewById<LinearLayout>(R.id.header_invite_friend_bt)

            val feedbackButton =
                headerLayout.findViewById<LinearLayout>(R.id.header_help_bt)

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



            inviteFriendButton.setOnClickListener {
                ShareUtil.shareAPPLink(this@WooHomeActivity)

            }
            feedbackButton.setOnClickListener {
                ShareUtil.openMail(this@WooHomeActivity)

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
        hideBottomSheet()

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
                Manifest.permission.BLUETOOTH_CONNECT,
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


    override fun onConversationUpdate() {
        runOnUiThread {
            fetchConversations()
            fetchCallLogs()
        }
    }


    private fun triggerRtpSession(conversation: Conversation, action: String) {
        if (this.xmppConnectionService.getJingleConnectionManager().isBusy()) {
            Toast.makeText(this, R.string.only_one_call_at_a_time, Toast.LENGTH_LONG)
                .show()
            return
        }
        val contact = conversation.contact
        if (contact.presences.anySupport(Namespace.JINGLE_MESSAGE)) {
            triggerRtpSession(contact.account, contact.jid.asBareJid(), action)
        } else {
            val capability: RtpCapability.Capability
            capability = if (action == RtpSessionActivity.ACTION_MAKE_VIDEO_CALL) {
                RtpCapability.Capability.VIDEO
            } else {
                RtpCapability.Capability.AUDIO
            }
            PresenceSelector.selectFullJidForDirectRtpConnection(
                this, contact, capability
            ) { fullJid: Jid ->
                triggerRtpSession(
                    contact.account,
                    fullJid,
                    action
                )
            }
        }
    }

    private fun triggerRtpSession(account: Account, with: Jid, action: String) {
        val intent = Intent(this, RtpSessionActivity::class.java)
        intent.setAction(action)
        intent.putExtra(EXTRA_ACCOUNT, account.jid.toEscapedString())
        intent.putExtra(RtpSessionActivity.EXTRA_WITH, with.toEscapedString())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToContactChat(uuid: String) {
        val newIntent = Intent(this@WooHomeActivity, ConversationActivity::class.java);
        newIntent.putExtra(ConversationsActivity.EXTRA_CONVERSATION, uuid)
        startActivity(newIntent)
    }

    override fun onAudioClick(message: Message) {
        val conversation = message.conversation as Conversation
        triggerRtpSession(conversation, RtpSessionActivity.ACTION_MAKE_VOICE_CALL)
    }

    override fun onVideoClick(message: Message) {
        val conversation = message.conversation as Conversation
        triggerRtpSession(conversation, RtpSessionActivity.ACTION_MAKE_VIDEO_CALL)
    }

    override fun onChatClick(message: Message) {
        val conversation = message.conversation as Conversation
        goToContactChat(conversation.uuid)
    }

    override fun onInfoClick(message: Message) {
        val conversation = message.conversation as Conversation

        val intent = Intent(this, CallLogsDetailActivity::class.java)
        intent.putExtra(CallLogsDetailActivity.EXTRA_CONVERSATION, conversation.uuid)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
    }

    override fun fetchMoreData(latestDate: Long) {
    }

    override fun onStartMeetingClick(meeting: ScheduleMeetingModel) {
        val intent = Intent(this, MeetingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra("email", MainActivity.account?.userEmail)
        intent.putExtra("accountUniqueId", MainActivity.account?.accountId)
        intent.putExtra("picture", MainActivity.account?.avatar)
        intent.putExtra("username", MainActivity.account?.username)
        intent.putExtra("camOn", false)
        intent.putExtra("micOn", false)
        intent.putExtra("meetingName", meeting.meetingName)
        intent.putExtra("meetingId", meeting.meetingId)
        intent.putExtra("joining", false)
        intent.putExtra(MeetingActivity.EXTRA_MEETING_SCHEDULED, meeting.scheduleUniqueId)
        MainActivity.meetingViewModel.removeMeeting(meeting)
        startActivity(intent)

    }

}























