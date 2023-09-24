package eu.siacs.conversations.ui.meeting

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.woooapp.meeting.impl.utils.ClipboardCopy
import com.woooapp.meeting.impl.views.MeetingActivity
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
class NewMeetingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var mBinding: FragmentNewMeetingBinding? = null
    private var accounts: List<Account>? = null
    private var account: Account? = null
    private var meetingId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        // Database backend
        Log.d(TAG, "<< Initializing DatabaseBackend and fetching accounts")
        var databaseBackend = DatabaseBackend.getInstance(context)
        this.accounts = databaseBackend.accounts
        if (accounts != null) {
            if (accounts?.size!! > 0)
                this.account = accounts?.get(0)
        }
        Log.d(TAG, "<< Accounts Size >> " + accounts?.size)
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

        var intent: Intent? = null

        if (account != null) {
            meetingId = Utils.getRandomString(8)
            mBinding?.meetingUrlEt?.text = Editable.Factory.getInstance()
                .newEditable("https://cc.watchblock.net/meeting/$meetingId")
            val email = account!!.userEmail
            val accountUniqueId = account!!.accountId
            val picture = account!!.avatar
            val username = account!!.username
            val meetingName = mBinding?.meetingNameEt?.text.toString()
            Log.d(TAG, "Account >> MeetingId[$meetingId], Email[$email], AccountUniqueID[$accountUniqueId], Picture[$picture], Username[$username]")
            intent = Intent(context, MeetingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("email", email)
            intent.putExtra("accountUniqueId", accountUniqueId)
            intent.putExtra("picture", picture)
            intent.putExtra("username", username.toString().toLowerCase(Locale.current))
            intent.putExtra("meetingName", meetingName)
        }

        // Start Meeting
        mBinding?.startMeetingBtn?.setOnClickListener {
            if (intent != null) {
                intent.putExtra("meetingId", meetingId)
                startActivity(intent)
            }
        }

        // Join Meeting
        mBinding?.joinMeetingBtn?.setOnClickListener {
            var dialog: AlertDialog? = null;
            val builder = context?.let { it1 -> AlertDialog.Builder(it1, R.style.TransparentBgDialogStyle) }
            val dialogView: View = LayoutInflater.from(context).inflate(R.layout.layout_join_meeting_dialog, null);
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
            dialog?.show()
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