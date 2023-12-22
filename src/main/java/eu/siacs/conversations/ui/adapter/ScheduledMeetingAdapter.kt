package eu.siacs.conversations.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.siacs.conversations.R
import eu.siacs.conversations.http.model.meeting.ScheduleMeetingModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class ScheduledMeetingAdapter(
    val context: Context,
    private var meetings: List<ScheduleMeetingModel>
) :
    RecyclerView.Adapter<ScheduledMeetingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.scheduled_meeting_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return meetings.size
    }

    public fun updateList(meeting: List<ScheduleMeetingModel>) {
        meetings = meeting
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val meeting = meetings[position]
        holder.dateTime.text =
            "${meeting.meetingDate?.let { convertIsoToFormattedString(it) }}, ${meeting.meetingStartTime}"
        holder.meetingTitle.text = meeting.meetingName
        holder.meetingDuration.text = "Duration ( ${meeting.meetingDuration} minutes )"
        holder.startMeetingBtn.setOnClickListener {
            onStartMeetingClickListener?.onStartMeetingClick(meeting)
        }
    }


    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val startMeetingBtn: Button = itemView.findViewById(R.id.start_meeting_btn)
        val dateTime: TextView = itemView.findViewById(R.id.date_time)
        val meetingTitle: TextView = itemView.findViewById(R.id.meeting_title)
        val meetingDuration: TextView = itemView.findViewById(R.id.meeting_duration)

//        init {
//            itemView.setOnClickListener {
//                onItemClick?.invoke(chainList[adapterPosition])
//            }
//        }

    }


    private fun convertIsoToFormattedString(isoDateString: String): String {
        // Define the input date format with milliseconds
        val inputFormatWithMillis =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        inputFormatWithMillis.timeZone = TimeZone.getTimeZone("UTC")

        // Define the input date format without milliseconds
        val inputFormatWithoutMillis =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        inputFormatWithoutMillis.timeZone = TimeZone.getTimeZone("UTC")
        // Try parsing with milliseconds
        return try {
            val dateWithMillis: Date = inputFormatWithMillis.parse(isoDateString)
            formatToMMMdd(dateWithMillis)
        } catch (e: Exception) {
            // Try parsing without milliseconds
            try {
                val dateWithoutMillis: Date = inputFormatWithoutMillis.parse(isoDateString)
                formatToMMMdd(dateWithoutMillis)
            } catch (e: Exception) {
                // Handle parsing error as needed
                e.printStackTrace()
                "Invalid Date"
            }
        }
    }

    private fun formatToMMMdd(date: Date): String {
        // Define the output date format
        val outputFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault() // Use the device's default time zone

        // Format the date and return the result
        return outputFormat.format(date)
    }


    interface OnStartMeetingClickListener {
        fun onStartMeetingClick(meeting: ScheduleMeetingModel)
    }

    private var onStartMeetingClickListener: OnStartMeetingClickListener? = null
    fun setOnStartMeetingClickListener(listener: OnStartMeetingClickListener) {
        this.onStartMeetingClickListener = listener
    }


}