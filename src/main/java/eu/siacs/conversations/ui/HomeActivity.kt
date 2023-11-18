package eu.siacs.conversations.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.woooapp.meeting.impl.utils.WDirector
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivityHomeBinding
import eu.siacs.conversations.entities.Account
import eu.siacs.conversations.services.XmppConnectionService
import eu.siacs.conversations.ui.util.AvatarWorkerTask
import pk.muneebahmad.lib.analytics.CrashChecker

class HomeActivity : XmppActivity(), XmppConnectionService.OnAccountUpdate {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var pieChart: PieChart? = null
    private var circleIndex = 0
//    private var crashChecker: CrashChecker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        populateCircularMenu()
        setSupportActionBar(binding.appBarHome.toolbar.toolbar)
        binding.appBarHome.toolbar.toolBarLeadingView.visibility = View.GONE
        binding.appBarHome.toolbar.toolbarProfilePhoto.visibility = View.VISIBLE
        binding.appBarHome.toolbar.toolbarNotification.visibility = View.VISIBLE


//        crashChecker = CrashChecker(this)
//        crashChecker?.checkForCrash()

        WDirector.getInstance().generateMeetingId()

        binding.appBarHome.toolbar.toolbarProfilePhoto.setOnClickListener {
            binding.drawerLayout.open()
        }

        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.appBarHome.homeBottomSheet.bottomSheet)
        initBottomSheet()

        populatePIChart()

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(eu.siacs.conversations.R.menu.home, menu)
//        return true
//    }


    private fun initBottomSheet() {
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

    private fun populatePIChart() {
        pieChart = binding.appBarHome.pieChart

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
                }
            }
        }
    }


    private fun populateView() {
        mAccount?.let {
            AvatarWorkerTask.loadAvatar(
                it,
                binding.appBarHome.toolbar.toolbarProfilePhoto,
                R.dimen.avatar
            )

            binding.appBarHome.toolbar.toolbarSearch.setOnClickListener {
                startActivity(Intent(this, SearchActivity::class.java))
            }

            binding.appBarHome.displayName.text = "Hi, ${getDisplayName()}"

            val headerLayout: View = binding.navView.getHeaderView(0) // 0-index header
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
                binding.drawerLayout.close()
                val intent = Intent(this, EditAccountActivity::class.java)
                intent.putExtra("jid", mAccount?.jid?.asBareJid().toString())
                intent.putExtra("init", false)
                startActivity(intent)
            }

            logoutButton.setOnClickListener {
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
        binding.appBarHome.outerWheel.layoutParams.height = (0.9 * width).toInt()
        binding.appBarHome.outerWheel.layoutParams.width = (0.9 * width).toInt()
        binding.appBarHome.middleWheelLayout.layoutParams.height = (0.7 * width).toInt()
        binding.appBarHome.middleWheelLayout.layoutParams.width = (0.7 * width).toInt()
        binding.appBarHome.innerWheel.layoutParams.height = (0.33 * width).toInt()
        binding.appBarHome.innerWheel.layoutParams.width = (0.33 * width).toInt()

        binding.appBarHome.meetingIv.setOnClickListener {
            rotateOuterCircle(false, 1)
            circleIndex = 0
            updatePiChart()

        }

        binding.appBarHome.walletIv.setOnClickListener {

            rotateOuterCircle(false, 2)
            circleIndex = 1
            updatePiChart()
        }
        binding.appBarHome.callIv.setOnClickListener {
            rotateOuterCircle(true, 3)
            circleIndex = 2
            updatePiChart()
        }
        binding.appBarHome.chatIv.setOnClickListener {
            rotateOuterCircle(true, 4)
            circleIndex = 3
            updatePiChart()
        }
        binding.appBarHome.innerWheel.setOnClickListener {
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
                ObjectAnimator.ofFloat(binding.appBarHome.outerWheel, View.ROTATION, 0f, degree)
            rotateAnimatorAntiClockWise =
                ObjectAnimator.ofFloat(
                    binding.appBarHome.middleWheel,
                    View.ROTATION,
                    0f,
                    -degree
                )
        } else {
            rotateAnimatorClockWise =
                ObjectAnimator.ofFloat(
                    binding.appBarHome.outerWheel,
                    View.ROTATION,
                    0f,
                    -degree
                )
            rotateAnimatorAntiClockWise =
                ObjectAnimator.ofFloat(
                    binding.appBarHome.middleWheel,
                    View.ROTATION,
                    0f,
                    degree
                )
        }

        rotateAnimatorClockWise.duration = duration
        rotateAnimatorAntiClockWise.duration = duration
        rotateAnimatorAntiClockWise.start()
        rotateAnimatorClockWise.start()

        rotateAnimatorClockWise.addListener(onEnd = {
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
//            newIntent = Intent(this, WooDappActivity::class.java)
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


    companion object {
        var mAccount: Account? = null

    }
}























