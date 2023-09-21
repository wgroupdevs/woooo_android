package eu.siacs.conversations.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import eu.siacs.conversations.R
import eu.siacs.conversations.ui.adapter.MeetingViewPagerAdapter


class MeetingMainFragment : XmppFragment() {
    private var activity: XmppActivity? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view?.findViewById<TabLayout>(R.id.tablayout)
        val viewPager = view?.findViewById<ViewPager2>(R.id.viewpager)

        val adapter = activity?.let {
            MeetingViewPagerAdapter(it.supportFragmentManager, it.lifecycle)
        }
        viewPager?.adapter = adapter

        if (tabLayout != null && viewPager != null) {
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = meetingArray[position]
            }.attach()
        }

    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if (activity is XmppActivity) {
            this.activity = activity
        } else {
            throw IllegalStateException("Trying to attach fragment to activity that is not an XmppActivity")
        }
    }

    public override fun refresh() {}
    override fun onBackendConnected() {}

    companion object {
        val meetingArray = arrayOf(
            "New",
            "History",
            "Schedule"
        )

    }
}