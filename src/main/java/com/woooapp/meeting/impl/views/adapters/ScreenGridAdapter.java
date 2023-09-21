package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.woooapp.meeting.impl.views.PeerView;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.RoomClient;
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
public class ScreenGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<Peer> mPeers = new LinkedList<>();

    private RoomStore mStore;
    private RoomClient mRoomClient;

    /**
     *
     * @param mContext
     * @param mPeers
     * @param mStore
     * @param mRoomClient
     */
    public ScreenGridAdapter(
            @NonNull Context mContext,
            @NonNull List<Peer> mPeers,
            @NonNull RoomStore mStore,
            @NonNull RoomClient mRoomClient) {
        this.mContext = mContext;
        this.mPeers = mPeers;
        this.mStore = mStore;
        this.mRoomClient = mRoomClient;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_cell_me_view, null);
            vh = new ViewHolder();
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.mPeerView = (PeerView) convertView.findViewById(R.id.remotePeer);
        PeerProps peerProps = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
//        vh.mPeerView.setProps(peerProps, mRoomClient);
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
