package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.impl.views.PeerView;
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

    private Context mContext;
    private List<Peer> mPeers = new LinkedList<>();
    private RoomStore mStore;
    private MeetingClient mMeetingClient;
    private final LifecycleOwner mLifecycleOwner;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_cell_peer_view, null);
            vh = new ViewHolder();
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.mPeerView = (PeerView) convertView.findViewById(R.id.remotePeerView);
        Peer peer = (Peer) getItem(position);
        PeerProps peerProps = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
        peerProps.connect(mLifecycleOwner, peer.getId());
        vh.mPeerView.setProps(peerProps, mMeetingClient);
        return convertView;
    }

    /**
     *
     * @param peers
     */
    public void replacePeers(List<Peer> peers) {
        this.mPeers = peers;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        PeerView mPeerView;
    } // end class

} /** end class. */
