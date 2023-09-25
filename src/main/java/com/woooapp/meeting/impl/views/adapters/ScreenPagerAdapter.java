package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.PagerAdapter;

import com.woooapp.meeting.lib.RoomClient;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.model.Peer;
import com.woooapp.meeting.lib.model.PeersScreen;

import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 12:48 pm 13/09/2023
 * <code>class</code> PagerAdapter.java
 */
public class ScreenPagerAdapter extends PagerAdapter {

    private static final String TAG = ScreenPagerAdapter.class.getName().toUpperCase();
    private List<PeersScreen> mPeersScreens = new LinkedList<>();

    private final Context mContext;
    private RoomStore mStore;
    private RoomClient mRoomClient;
    private LifecycleOwner mLifecycleOwner;

    /**
     *
     * @param context
     * @param roomStore
     * @param owner
     * @param roomClient
     */
    public ScreenPagerAdapter(
            @NonNull Context context,
            @NonNull RoomStore roomStore,
            @NonNull LifecycleOwner owner,
            @NonNull RoomClient roomClient) {
        this.mContext = context;
        this.mStore = roomStore;
        this.mLifecycleOwner = owner;
        this.mRoomClient = roomClient;
    }

    @Override
    public int getCount() {
        return mPeersScreens.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View screenView = LayoutInflater.from(mContext).inflate(R.layout.layout_meet_screen_slider, null);
        GridView gv = (GridView) screenView.findViewById(R.id.meetGridView);
//        gv.setAdapter(new MeetingGridAdapter(mContext, getPeersList(position), mStore, mRoomClient));
        gv.setOnItemClickListener((parent, view, position1, id) -> {
            // TODO On Peer Clicked
        });
        return screenView;
    }

    private List<Peer> getPeersList(int position) {
        final List<Peer> peers = new LinkedList<>();
        peers.add(mPeersScreens.get(position).getPeer1());
        peers.add(mPeersScreens.get(position).getPeer2());
        peers.add(mPeersScreens.get(position).getPeer3());
        peers.add(mPeersScreens.get(position).getPeer4());
        return peers;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    /**
     *
     * @param peersScreens
     */
    public void replacePeers(List<PeersScreen> peersScreens) {
        this.mPeersScreens = peersScreens;
        notifyDataSetChanged();
    }
} // end class
