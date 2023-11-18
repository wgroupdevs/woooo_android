package eu.siacs.conversations.ui.meeting

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Vibrator
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.woooapp.meeting.impl.utils.ClipboardCopy
import com.woooapp.meeting.impl.utils.WDirector
import com.woooapp.meeting.impl.utils.WEvents
import com.woooapp.meeting.impl.views.MeetingActivity
import com.woooapp.meeting.impl.views.UIManager
import com.woooapp.meeting.impl.views.UIManager.DialogCallback
import com.woooapp.meeting.lib.Utils
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.FragmentNewMeetingBinding
import eu.siacs.conversations.entities.Account
import eu.siacs.conversations.persistance.DatabaseBackend

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewMeetingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewMeetingFragment : Fragment(), Handler.Callback {
    // TODO: Rename and change types of parameters
    private val PERMISSION_MIC_CODE = 0x01
    private val PERMISSION_CAM_CODE = 0x02;
    private val PERMISSION_NOTIFICATION_CODE = 0x03;

    private var param1: String? = null
    private var param2: String? = null

    private var mBinding: FragmentNewMeetingBinding? = null
    private var accounts: List<Account>? = null
    private var account: Account? = null
    private var meetingId: String? = null

    private var vibrator: Vibrator? = null
    private val handler: Handler = Handler(this)
    private var camEnabled = false
    private var micEnabled = true

    var micPermissionGranted = false;
    var notificationPermissionGranted = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        WEvents.getInstance().addHandler(handler)

        // Database backend
        Log.d(TAG, "<< Initializing DatabaseBackend and fetching accounts")
        var databaseBackend = DatabaseBackend.getInstance(context)
        vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        this.accounts = databaseBackend.accounts
        if (accounts != null) {
            if (accounts?.size!! > 0)
                this.account = accounts?.get(0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_new_meeting, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initComponents()

        micPermissionGranted = if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            askForPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_MIC_CODE)
            false
        } else {
            true
        }

        var intent: Intent? = null
        if (account != null) {
            meetingId = WDirector.getInstance().meetingId;
            mBinding?.meetingUrlEt?.text = Editable.Factory.getInstance()
                .newEditable("https://cc.watchblock.net/meeting/$meetingId")
            val email = account!!.userEmail
            val accountUniqueId = account!!.accountId
            val picture = account!!.avatar
            val username = account!!.displayName
            Log.d(TAG, "Account >> MeetingId[$meetingId], Email[$email], AccountUniqueID[$accountUniqueId], Picture[$picture], Username[$username]")
            intent = Intent(context, MeetingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.putExtra("email", email)
            intent.putExtra("accountUniqueId", accountUniqueId)
            intent.putExtra("picture", picture)
            intent.putExtra("username", username)
            intent.putExtra("camOn", camEnabled)
            intent.putExtra("micOn", micEnabled)
        }

        // Start Meeting
        mBinding?.startMeetingBtn?.setOnClickListener {
            if (micPermissionGranted) {
                if (mBinding?.meetingNameEt?.text.toString() != null) {
                    if (mBinding?.meetingNameEt?.text.toString().isNotEmpty()) {
                        if (intent != null) {
                            intent.putExtra("meetingName", mBinding?.meetingNameEt?.text.toString())
                            intent.putExtra("meetingId", meetingId)
                            intent.putExtra("joining", false)
                            startActivity(intent)
                            mBinding?.meetingNameEt?.text =
                                Editable.Factory.getInstance().newEditable("")
                        }
                    } else {
                        meetingEtEmptyError()
                    }
                } else {
                    meetingEtEmptyError()
                }
            } else {
                askForPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_MIC_CODE)
            }
        }

        // Join Meeting
        mBinding?.joinMeetingBtn?.setOnClickListener {
            if (micPermissionGranted) {
                var dialog: AlertDialog? = null;
                val builder = context?.let { it1 ->
                    AlertDialog.Builder(
                        it1,
                        R.style.TransparentBgDialogStyle
                    )
                }
                val dialogView: View =
                    LayoutInflater.from(context).inflate(R.layout.layout_join_meeting_dialog, null);
                var etMeetingId = dialogView.findViewById<AppCompatEditText>(R.id.etDialog)
                val buttonJoin = dialogView.findViewById<Button>(R.id.btnOkDialog)
                val buttonCancel = dialogView.findViewById<Button>(R.id.btnCancelDialog)
                builder?.setView(dialogView)

                buttonJoin.setOnClickListener {
                    if (intent != null) {
                        meetingId = etMeetingId.text.toString()
                        if (meetingId!!.isEmpty()) {
                            etMeetingId.hint = "Meeting ID is required"
                            etMeetingId.setHintTextColor(Color.parseColor("#ff0000"))
                        } else {
                            intent.putExtra("meetingId", meetingId)
                            intent.putExtra("joining", true)
                            startActivity(intent)
                            dialog?.dismiss()
                        }
                    }
                }

                buttonCancel.setOnClickListener {
                    dialog?.dismiss()
                }
                dialog = builder?.create()
                dialog?.setCanceledOnTouchOutside(false)
                dialog?.setCancelable(false)
                dialog?.show()
            } else {
                askForPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_MIC_CODE)
            }
        }

        // Copy Button
        mBinding?.meetingCopyUrlBtn?.setOnClickListener {
            val link = mBinding?.meetingUrlEt?.text.toString()
            ClipboardCopy.clipboardCopy(context, link)
            context?.let { it1 ->
                Snackbar.make(
                    it1,
                    mBinding?.startMeetingBtn!!,
                    "Meeting Link $link copied to clipboard",
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }

    /**
     *
     */
    private fun initComponents() {
        if (micEnabled) {
            mBinding?.buttonMic?.setImageResource(R.drawable.ic_mic_white_48dp)
            mBinding?.tvMic?.text = "Mic is On"
        } else {
            mBinding?.buttonMic?.setImageResource(R.drawable.ic_mic_off_gray)
            mBinding?.tvMic?.text = "Mic is Off"
        }
        if (camEnabled) {
            mBinding?.buttonCam?.setImageResource(R.drawable.ic_video_camera_white)
            mBinding?.tvCam?.text = "Camera is On"
        } else {
            mBinding?.buttonCam?.setImageResource(R.drawable.ic_camera_off_gray)
            mBinding?.tvCam?.text = "Camera is Off"
        }

        mBinding?.buttonMic?.setOnClickListener {
            micEnabled = if (micEnabled) {
                mBinding?.buttonMic?.setImageResource(R.drawable.ic_mic_off_gray)
                mBinding?.tvMic?.text = "Mic is Off"
                mBinding?.tvMic?.setTextColor(Color.parseColor("#dddddd"))
                false
            } else {
                mBinding?.buttonMic?.setImageResource(R.drawable.ic_mic_white_48dp)
                mBinding?.tvMic?.text = "Mic is On"
                mBinding?.tvMic?.setTextColor(Color.parseColor("#ffffff"))
                true
            }
        }

        mBinding?.buttonCam?.setOnClickListener {
            camEnabled = if (camEnabled) {
                mBinding?.buttonCam?.setImageResource(R.drawable.ic_camera_off_gray)
                mBinding?.tvCam?.text = "Camera is Off"
                mBinding?.tvCam?.setTextColor(Color.parseColor("#dddddd"))
                false
            } else {
                mBinding?.buttonCam?.setImageResource(R.drawable.ic_video_camera_white)
                mBinding?.tvCam?.text = "Camera is On"
                mBinding?.tvCam?.setTextColor(Color.parseColor("#ffffff"))
                true
            }
        }
    }

    @SuppressWarnings("deprecation")
    fun meetingEtEmptyError() {
        mBinding?.meetingNameEt?.setHintTextColor(Color.parseColor("#ff0000"))
        mBinding?.meetingNameEt?.hint = "* Meeting name is required!"
        vibrator?.vibrate(100L)
    }

    override fun handleMessage(msg: Message): Boolean {
        if (msg.what == WEvents.EVENT_TYPE_SOCKET_DISCONNECTED) {
            meetingId = Utils.getNumericString(9);
            mBinding?.meetingUrlEt?.text = Editable.Factory.getInstance()
                .newEditable("https://cc.watchblock.net/meeting/$meetingId")
            return true
        }
        return false
    }

    @SuppressWarnings("deprecation")
    fun askForPermission(permission: String, reqCode: Int) {
        if (shouldShowRequestPermissionRationale(permission)) {
            val dialogCallback: DialogCallback = object : DialogCallback {
                override fun onPositiveButton(sender: Any?, data: Any?) {
                    requestPermissions(arrayOf(permission), reqCode)
                }

                override fun onNeutralButton(sender: Any?, data: Any?) {
                    TODO("Not yet implemented")
                }

                override fun onNegativeButton(sender: Any?, data: Any?) {
                    TODO("Not yet implemented")
                }

            }
            UIManager.showInfoDialog(requireContext(),
                "Permission Required",
                "Meeting requires at least mic permission to start up. You can turn off mic later on from with in. Kindly grant it to continue.",
                dialogCallback)
        } else {
            requestPermissions(arrayOf(permission), reqCode);
        }
    }

    @SuppressWarnings("deprecation")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_MIC_CODE -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        micPermissionGranted = true
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            askForPermission(Manifest.permission.POST_NOTIFICATIONS, PERMISSION_NOTIFICATION_CODE)
                        }
                    }
                }
            }
            PERMISSION_NOTIFICATION_CODE -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        notificationPermissionGranted = true
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        WEvents.getInstance().removeHandler(handler)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {

        const val TAG: String = "NewMeetingFragment.kt"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewMeetingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewMeetingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    } // end companion object

} /** end class. */