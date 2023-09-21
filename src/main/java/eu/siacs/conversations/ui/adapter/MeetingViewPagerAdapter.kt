package eu.siacs.conversations.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import eu.siacs.conversations.ui.meeting.MeetingHistoryFragment
import eu.siacs.conversations.ui.meeting.NewMeetingFragment
import eu.siacs.conversations.ui.meeting.ScheduleMeetingFragment


private const val NUM_TABS = 3

public class MeetingViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return NewMeetingFragment()
            1 -> return MeetingHistoryFragment()
            2 -> return ScheduleMeetingFragment()
        }
        return NewMeetingFragment()
    }
}