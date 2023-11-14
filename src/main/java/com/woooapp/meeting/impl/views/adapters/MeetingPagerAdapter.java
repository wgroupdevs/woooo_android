package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.impl.views.fragments.MeetingPageFragment;
import com.woooapp.meeting.impl.views.fragments.MeetingPageFragment2;
import com.woooapp.meeting.impl.views.models.MeetingPage;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.lv.RoomStore;

import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;
import eu.siacs.conversations.entities.Room;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 12:29 pm 14/10/2023
 * <code>class</code> MeetingPagerAdapter.java
 */
public class MeetingPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = MeetingPagerAdapter.class.getSimpleName() + ".java";
    private List<MeetingPage> pageList = new LinkedList<>();
    private final Context mContext;
    private final LifecycleOwner mLifecycleOwner;
    private final RoomStore mStore;
    private final MeetingClient mClient;
    private List<MeetingPageFragment2> fragments = new LinkedList<>();

    /**
     * @param fm
     * @param lifecycleOwner
     * @param store
     * @param client
     */
    public MeetingPagerAdapter(@NonNull FragmentManager fm,
                               @NonNull LifecycleOwner lifecycleOwner,
                               @NonNull RoomStore store,
                               @NonNull MeetingClient client,
                               @NonNull Context context) {
        super(fm);
        this.mLifecycleOwner = lifecycleOwner;
        this.mStore = store;
        this.mClient = client;
        this.mContext = context;
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        try {
            super.finishUpdate(container);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * @param bottomBarHeight
     */
    public void setBottomBarHeight(int bottomBarHeight) {
        for (int i = 0; i < pageList.size(); i++) {
            MeetingPageFragment2 f = (MeetingPageFragment2) getItem(i);
            f.setBottomBarHeight(bottomBarHeight);
        }
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_NONE;
//    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * @param pageList
     */
    public void replacePages(List<MeetingPage> pageList) {
        this.pageList = pageList;
        notifyDataSetChanged();
    }

    public void replaceFragments(@NonNull List<MeetingPageFragment2> fragments) {
        this.fragments = fragments;
//        for (int i = 0; i < fragments.size(); i++) {
//            Log.v(TAG, "Replacing page for page no. >>> ");
//            fragments.get(i).replacePage(pageList.get(i));
//        }
        notifyDataSetChanged();
    }

} /**
 * end class.
 */
