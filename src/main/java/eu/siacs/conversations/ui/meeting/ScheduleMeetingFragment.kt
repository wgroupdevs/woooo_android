package eu.siacs.conversations.ui.meeting

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.woooapp.meeting.impl.utils.WDirector
import com.woooapp.meeting.impl.views.MeetingActivity
import com.woooapp.meeting.impl.views.popups.WooProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.FragmentScheduleMeetingBinding
import eu.siacs.conversations.http.model.meeting.ScheduleMeetingModel
import eu.siacs.conversations.ui.MainActivity
import eu.siacs.conversations.ui.MainActivity.Companion.scheduleMeetingViewModel
import eu.siacs.conversations.ui.adapter.ScheduledMeetingAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ScheduleMeetingFragment : Fragment(),
    ScheduledMeetingAdapter.OnStartMeetingClickListener {

    lateinit var binding: FragmentScheduleMeetingBinding

    private val TAG = "ScheduleFragment_TAG"
    private var scheduleMeetingDataInISOFormat = ""
    lateinit var scheduledMeetingAdapter: ScheduledMeetingAdapter
    private var progressDialog: WooProgressDialog? = null
    private var bottomSheetDialog: BottomSheetDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewModel
        MainActivity.account?.accountId?.let { scheduleMeetingViewModel.getScheduledNewMeetings(it) }
        scheduledMeetingAdapter = ScheduledMeetingAdapter(requireContext(), emptyList())
        // Observe the LiveData in the UI
        scheduleMeetingViewModel.getScheduledMeetings().observe(this) { meetings ->
            Log.d(TAG, "Scheduled Meetings Count : ${meetings.size}")
            scheduledMeetingAdapter.updateList(meetings)
        }
        scheduleMeetingViewModel.onCreateScheduleMeeting().observe(this) { meeting ->
            progressDialog?.dismiss()
            bottomSheetDialog?.dismiss()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_schedule_meeting,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scheduledMeetingRecyclerView.adapter = scheduledMeetingAdapter
        scheduledMeetingAdapter.setOnStartMeetingClickListener(this)
        binding.scheduleMeetingBtn.setOnClickListener {
            showScheduleBottomSheet()
        }


    }

    private fun showScheduleBottomSheet() {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        val view = layoutInflater.inflate(R.layout.schedule_meeting_bottom_sheet_layout, null)
        bottomSheetDialog?.setContentView(view)

        val cancelButton = view.findViewById<Button>(R.id.cancel_btn)
        val scheduleButton = view.findViewById<Button>(R.id.schedule_meeting_btn)
        val meetingName = view.findViewById<EditText>(R.id.meeting_name_et)
        val meetingDate = view.findViewById<EditText>(R.id.meeting_date_et)
        val meetingStartTime = view.findViewById<EditText>(R.id.meeting_start_time_et)
        val meetingDuration = view.findViewById<EditText>(R.id.meeting_duration_et)
        showDatePicker(meetingDate)
        meetingStartTime.setOnClickListener { showTimePicker(meetingStartTime) }

        cancelButton.setOnClickListener { bottomSheetDialog?.dismiss() }

        scheduleButton.setOnClickListener {
            validateScheduleMeetingForm(meetingName, meetingDate, meetingStartTime, meetingDuration)
        }

        bottomSheetDialog?.show()
    }


    private fun showDatePicker(meetingDate: EditText) {
        val myCalendar = Calendar.getInstance()
        val date = OnDateSetListener { view: DatePicker?, year: Int, month: Int, day: Int ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = month
            myCalendar[Calendar.DAY_OF_MONTH] = day
            updateDate(myCalendar, meetingDate)
        }
        meetingDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                R.style.datePickerDialogTheme,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            datePickerDialog.show()
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.blue_primary300))
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.blue_primary300))
        }
    }

    private fun updateDate(myCalendar: Calendar, meetingDate: EditText) {
        val myFormat = "MM/dd/yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        scheduleMeetingDataInISOFormat = isoFormat.format(myCalendar.time)
        meetingDate.setText(dateFormat.format(myCalendar.time))
    }

    private fun showTimePicker(meetingDate: EditText) {
        val now = Calendar.getInstance()
        val hour = now.get(Calendar.HOUR_OF_DAY)
        val minute = now.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.TimePickerDialogStyle,
            { _, selectedHour, selectedMinute ->

                var hours = selectedHour;

                val amPm = if (selectedHour >= 12) {
                    if (selectedHour > 12) {
                        hours = selectedHour - 12
                    }
                    "PM"
                } else {
                    if (selectedHour == 0) {
                        hours = 12
                    }
                    "AM"
                }
                meetingDate.setText("$hours:$selectedMinute $amPm")
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()

    }

    private fun validateScheduleMeetingForm(
        meetingName: EditText,
        meetingDate: EditText,
        meetingStartTime: EditText,
        meetingDuration: EditText,
    ) {

        if (meetingName.text.isEmpty()) {
            Toast.makeText(activity, "Enter Meeting Name", Toast.LENGTH_LONG).show()
            return
        } else if (meetingDate.text.isEmpty()) {
            Toast.makeText(activity, "Select Meeting Date", Toast.LENGTH_LONG).show()
            return
        } else if (meetingStartTime.text.isEmpty()) {
            Toast.makeText(activity, "Select Meeting Start Time", Toast.LENGTH_LONG).show()
            return
        } else if (meetingDuration.text.isEmpty()) {
            Toast.makeText(activity, "Enter Meeting Duration", Toast.LENGTH_LONG).show()
            return
        }

        WDirector.getInstance().generateMeetingId()

        val scheduleMeetingModel = ScheduleMeetingModel(
            meetingId = WDirector.getInstance().meetingId,
            meetingName = meetingName.text.toString(),
            meetingDate = scheduleMeetingDataInISOFormat,
            meetingStartTime = meetingStartTime.text.toString(),
            meetingDuration = meetingDuration.text.toString(),
            accountId = MainActivity.account?.accountId,
            isDeleted = false
        )

        scheduleMeetingViewModel.scheduleNewMeeting(scheduleMeetingModel)
        progressDialog = WooProgressDialog.make(requireActivity(), "Scheduling ...")
        progressDialog?.show()

    }

    override fun onStartMeetingClick(meeting: ScheduleMeetingModel) {
        val intent = Intent(requireContext(), MeetingActivity::class.java)
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
        startActivity(intent)

    }

}