package eu.siacs.conversations.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.drawerlayout.widget.DrawerLayout
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.ActivityHomeBinding
import eu.siacs.conversations.entities.Account
import eu.siacs.conversations.services.XmppConnectionService
import eu.siacs.conversations.ui.util.AvatarWorkerTask


class HomeActivity : XmppActivity(), XmppConnectionService.OnAccountUpdate {

    private lateinit var binding: ActivityHomeBinding

    private var mAccount: Account? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        populateCircularMenu()
        setSupportActionBar(binding.appBarHome.toolbar.toolbar)
        binding.appBarHome.toolbar.toolBarLeadingView.visibility = View.GONE
        binding.appBarHome.toolbar.toolbarProfilePhoto.visibility = View.VISIBLE
        binding.appBarHome.toolbar.toolbarNotification.visibility = View.VISIBLE


        val drawerLayout: DrawerLayout = binding.drawerLayout
        binding.appBarHome.toolbar.toolbarProfilePhoto.setOnClickListener {
            drawerLayout.open()
        }

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(eu.siacs.conversations.R.menu.home, menu)
//        return true
//    }


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

            binding.appBarHome.displayName.setText("Hi, ${mAccount?.displayName}")

            val headerLayout: View = binding.navView.getHeaderView(0) // 0-index header
            val nameTextView = headerLayout.findViewById<TextView>(R.id.header_displayName_tv)
            val emailTextView = headerLayout.findViewById<TextView>(R.id.header_email_tv)
            val profileView = headerLayout.findViewById<ImageView>(R.id.header_profile_iv)
            val editProfileButton =
                headerLayout.findViewById<LinearLayout>(R.id.header_edit_profile_bt)

            val logoutButton =
                headerLayout.findViewById<LinearLayout>(R.id.header_logout_bt)
            nameTextView.text = "${mAccount?.displayName}"
            emailTextView.text = "${mAccount?.userEmail}"
            AvatarWorkerTask.loadAvatar(
                it,
                profileView,
                R.dimen.avatar
            )


            editProfileButton.setOnClickListener {
                val intent = Intent(this, EditAccountActivity::class.java)
                intent.putExtra("jid", mAccount?.jid?.asBareJid().toString())
                intent.putExtra("init", false)
                startActivity(intent)
            }

            logoutButton.setOnClickListener {
                xmppConnectionService.databaseBackend.clearDatabase()
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
            }
        }

    }

    private fun populateCircularMenu() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels
        binding.appBarHome.outerWheel.layoutParams.height = (0.9 * width).toInt()
        binding.appBarHome.outerWheel.layoutParams.width = (0.9 * width).toInt()
        binding.appBarHome.middleWheelLayout.layoutParams.height = (0.7 * width).toInt()
        binding.appBarHome.middleWheelLayout.layoutParams.width = (0.7 * width).toInt()
        binding.appBarHome.innerWheel.layoutParams.height = (0.33 * width).toInt()
        binding.appBarHome.innerWheel.layoutParams.width = (0.33 * width).toInt()

        binding.appBarHome.meetingIv.setOnClickListener {
            rotateOuterCircle(false, 1)
        }

        binding.appBarHome.walletIv.setOnClickListener {
            rotateOuterCircle(false, 2)
        }
        binding.appBarHome.callIv.setOnClickListener {
            rotateOuterCircle(true, 3)
        }
        binding.appBarHome.chatIv.setOnClickListener {
            rotateOuterCircle(true, 4)
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
                ObjectAnimator.ofFloat(binding.appBarHome.middleWheel, View.ROTATION, 0f, -degree)
        } else {
            rotateAnimatorClockWise =
                ObjectAnimator.ofFloat(binding.appBarHome.outerWheel, View.ROTATION, 0f, -degree)
            rotateAnimatorAntiClockWise =
                ObjectAnimator.ofFloat(binding.appBarHome.middleWheel, View.ROTATION, 0f, degree)
        }

        rotateAnimatorClockWise.duration = duration
        rotateAnimatorAntiClockWise.duration = duration
        rotateAnimatorAntiClockWise.start()
        rotateAnimatorClockWise.start()

        rotateAnimatorClockWise.addListener(onEnd = {

            if (index > 0)
                goToConversationActivity(index)
        })
    }


    private fun goToConversationActivity(index: Int) {
        val newIntent = Intent(this, ConversationActivity::class.java);
        newIntent.putExtra(ConversationsActivity.EXTRA_CIRCLE_MENU_INDEX, index)
        startActivity(newIntent)
    }

    override fun onAccountUpdate() {
        runOnUiThread {
            populateView()
        }
    }
}























