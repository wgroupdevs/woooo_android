package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.views.MeView;
import com.woooapp.meeting.impl.views.PeerView;
import com.woooapp.meeting.impl.views.models.GridPeer;
import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.model.Peer;

import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 1:33 pm 13/09/2023
 * <code>class</code> ScreenGridAdapter.java
 */
public class MeetingGridAdapter extends BaseAdapter {

    private static final String TAG = MeetingGridAdapter.class.getSimpleName() + ".java";
    private Context mContext;
    private List<GridPeer> mPeers = new LinkedList<>();
    private RoomStore mStore;
    private MeetingClient mMeetingClient;
    private final LifecycleOwner mLifecycleOwner;
    private int tabBarHeight;

    /**
     *
     * @param mContext
     * @param lifecycleOwner
     * @param mStore
     * @param meetingClient
     */
    public MeetingGridAdapter(
            @NonNull Context mContext,
            @NonNull LifecycleOwner lifecycleOwner,
            @NonNull RoomStore mStore,
            @NonNull MeetingClient meetingClient) {
        this.mContext = mContext;
        this.mLifecycleOwner = lifecycleOwner;
        this.mStore = mStore;
        this.mMeetingClient = meetingClient;
    }

    @Override
    public int getCount() {
        return mPeers.size();
    }

    @Override
    public Object getItem(int position) {
        return mPeers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mPeers.get(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case GridPeer.VIEW_TYPE_ME:
                MeViewHolder mVh = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_cell_me_view, null);
                    mVh = new MeViewHolder();
                    convertView.setTag(mVh);
                } else {
                    mVh = (MeViewHolder) convertView.getTag();
                }
                mVh.mMeView = convertView.findViewById(R.id.meView);

                setViewHeight(convertView);

                MeProps meProps = new MeProps(((AppCompatActivity) mContext).getApplication(), mStore);
                meProps.connect(mLifecycleOwner);
                mVh.mMeView.setProps(meProps, mMeetingClient);
                return convertView;
            default:
                PeerViewHolder vh = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_cell_peer_view, null);
                    vh = new PeerViewHolder();
                    convertView.setTag(vh);
                } else {
                    vh = (PeerViewHolder) convertView.getTag();
                }

                vh.mPeerView = convertView.findViewById(R.id.remotePeerView);
                vh.mTvPeerName = convertView.findViewById(R.id.tvPeerName);

                setViewHeight(convertView);

                Peer peer = ((GridPeer) getItem(position)).getPeer();
                PeerProps peerProps = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
                peerProps.connect(mLifecycleOwner, peer.getId());
                vh.mPeerView.setProps(peerProps, mMeetingClient);
                if (peer.getDisplayName() != null) {
                    vh.mTvPeerName.setText(peer.getDisplayName().isEmpty() ? "Empty" : peer.getDisplayName());
                }
                return convertView;
        }
    }

    /**
     *
     * @param view
     * @return
     */
    private boolean setViewHeight(@NonNull final View view) {
        int height = Display.getDisplayHeight(mContext) - (tabBarHeight + 200);
        if (mPeers.size() == 2) {
            height = height / 2 - 50;
        } else if (mPeers.size() > 2 && mPeers.size() < 5) {
            height = height / 2 - 50;
        }
        if (view.getLayoutParams() != null) {
            Log.d(TAG, "<< Convert view layout params not null >>");
            view.getLayoutParams().height = height;
            view.invalidate();
            return true;
        }
        return false;
    }

    /**
     *
     * @param peers
     */
    public void replacePeers(List<GridPeer> peers) {
        this.mPeers = peers;
        notifyDataSetChanged();
    }

    /**
     *
     * @param height
     */
    public void setTabBarHeight(int height) {
        this.tabBarHeight = tabBarHeight;
    }

    static class MeViewHolder {
        MeView mMeView;
    } // end class

    static class PeerViewHolder {
        PeerView mPeerView;
        TextView mTvPeerName;
    } // end class

} /** end class. */
