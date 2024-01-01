package eu.siacs.conversations.ui.meeting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import eu.siacs.conversations.R
import eu.siacs.conversations.databinding.FragmentMeetingHistoryBinding
import eu.siacs.conversations.ui.MainActivity
import eu.siacs.conversations.ui.adapter.MeetingHistoryAdapter

/**
 * Created by Ehsan on 06/11/2023.
 */
class MeetingHistoryFragment : Fragment() {

    private val TAG = "MeetingHistory_TAG"
    private lateinit var meetingHistoryAdapter: MeetingHistoryAdapter
    lateinit var binding: FragmentMeetingHistoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ViewModel
        MainActivity.account?.accountId?.let { MainActivity.meetingViewModel.getMeetingHistory(it) }
        meetingHistoryAdapter = MeetingHistoryAdapter(requireContext(), emptyList())

//        scheduledMeetingAdapter = ScheduledMeetingAdapter(requireContext(), emptyList())
        // Observe the LiveData in the UI
        MainActivity.meetingViewModel.onMeetingHistory().observe(this) { meetings ->
            Log.d(TAG, "History Meetings Count : ${meetings.size}")

            if (meetings.isEmpty()) {
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE

            }
            meetingHistoryAdapter.updateList(meetings)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_meeting_history,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.meetingHistoryRecyclerView.adapter = meetingHistoryAdapter

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MeetingHistoryFragment().apply {
            }
    }
}