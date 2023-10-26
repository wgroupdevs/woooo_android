package com.woooapp.meeting.impl.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.impl.views.adapters.PeerAdapter2;
import com.woooapp.meeting.impl.views.models.MeetingPage;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.lv.RoomStore;

import eu.siacs.conversations.R;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 1:35 pm 14/10/2023
 * <code>class</code> MeetingPageFragment.java
 */
public class MeetingPageFragment extends Fragment {

    private static final String TAG = MeetingPageFragment.class.getSimpleName() + ".java";
    private final int pageNo;
    private final LifecycleOwner mLifecycleOwner;
    private final RoomStore mRoomStore;
    private final MeetingClient mMeetingClient;
    private ListView listView;
    private PeerAdapter2 adapter;
    private MeetingPage mMeetingPage;
    private Context gContext;

    /**
     * @param pageNo
     * @param lifecycleOwner
     * @param roomStore
     * @param meetingClient
     */
    public MeetingPageFragment(int pageNo,
                               @NonNull LifecycleOwner lifecycleOwner,
                               @NonNull RoomStore roomStore,
                               @NonNull MeetingClient meetingClient,
                               @NonNull Context context) {
        this.pageNo = pageNo;
        this.mLifecycleOwner = lifecycleOwner;
        this.mRoomStore = roomStore;
        this.mMeetingClient = meetingClient;
        this.gContext = context;

        this.adapter = new PeerAdapter2(gContext, pageNo, mLifecycleOwner, mRoomStore, mMeetingClient);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_peer_view_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.listView = view.findViewById(R.id.peersListView);
        this.listView.setEnabled(false);
        this.listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.resume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) adapter.dispose();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter != null) adapter.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) adapter.dispose();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (adapter != null) adapter.dispose();
    }

    /**
     * @param bottomBarHeight
     */
    public void setBottomBarHeight(int bottomBarHeight) {
        if (this.adapter != null) this.adapter.setViewPagerHeight(bottomBarHeight);
    }

    /**
     * @param meetingPage
     */
    public void replacePage(@NonNull MeetingPage meetingPage) {
        this.mMeetingPage = meetingPage;
        if (this.adapter != null) {
            Log.v(TAG, "<< Notifying PeerAdapter2 >>");
            this.adapter.replaceList(meetingPage.getmPeers());
        } else {
            Log.w(TAG, "<< PeerAdapter2 is null >>");
        }

    }

    public void enableTranslation() {
        if (adapter != null) adapter.setTranslation(true);
    }

    public void disableTranslation() {
        if (adapter != null) adapter.setTranslation(false);
    }

    public void notifyUpdate() {
        if (this.adapter != null) this.adapter.notifyDataSetChanged();
    }

    public void clean() {
        this.mMeetingPage = null;
    }

} /**
 * end class.
 */
