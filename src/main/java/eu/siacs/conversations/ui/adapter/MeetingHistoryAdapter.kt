package eu.siacs.conversations.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import eu.siacs.conversations.R
import eu.siacs.conversations.http.model.meeting.HistoryMeetingModel
import eu.siacs.conversations.http.model.meeting.ScheduleMeetingModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class MeetingHistoryAdapter(
    val context: Context,
    private var meetings: List<HistoryMeetingModel>
) :
    RecyclerView.Adapter<MeetingHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.meeting_history_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return meetings.size
    }

    public fun updateList(meeting: List<HistoryMeetingModel>) {
        meetings = meeting
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val meeting = meetings[position]
        holder.dateTime.text =
            "${meeting.startTime?.let { convertIsoToFormattedString(it) }}"
        holder.meetingTitle.text = meeting.meetingName

        holder.meetingDuration.text = "Duration\n ${meeting.duration} "

    }


    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
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
            formatToMMMDHHMMA(dateWithMillis)
        } catch (e: Exception) {
            // Try parsing without milliseconds
            try {
                val dateWithoutMillis: Date = inputFormatWithoutMillis.parse(isoDateString)
                formatToMMMDHHMMA(dateWithoutMillis)
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
    private fun formatToMMMDHHMMA(date: Date): String {
        // Define the output date format
        val outputFormat = SimpleDateFormat("MMM d, hh:mm a", Locale.US)

//        val outputFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
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