package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.views.MeView;
import com.woooapp.meeting.impl.views.PeerView;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.views.models.GridPeer;
import com.woooapp.meeting.impl.views.models.ListGridPeer;
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
 * Created On 4:45 pm 10/10/2023
 * <code>class</code> PeerAdapter2.java
 */
public class PeerAdapter2 extends BaseAdapter {

    private static final String TAG = PeerAdapter2.class.getSimpleName() + ".java";
    private final Context mContext;
    private final int mPageNo;
    private final LifecycleOwner mLifecycleOwner;
    private final RoomStore mStore;
    private final MeetingClient mMeetingClient;
    private List<ListGridPeer> peerList = new LinkedList<>();
    private int mDeviceWidth;
    private int mDeviceHeight;
    private int mBottomBarHeight = 90;

    /**
     *
     * @param mContext
     * @param pageNo
     * @param mLifecycleOwner
     * @param mStore
     * @param mMeetingClient
     */
    public PeerAdapter2(@NonNull Context mContext,
                        int pageNo,
                        @NonNull LifecycleOwner mLifecycleOwner,
                        @NonNull RoomStore mStore,
                        @NonNull MeetingClient mMeetingClient) {
        this.mContext = mContext;
        this.mPageNo = pageNo;
        this.mLifecycleOwner = mLifecycleOwner;
        this.mStore = mStore;
        this.mMeetingClient = mMeetingClient;

        this.mDeviceWidth = Display.getDisplayWidth(mContext) - 40;
        this.mDeviceHeight = Display.getDisplayHeight(mContext) - mBottomBarHeight;
    }

    @Override
    public int getCount() {
        Log.v(TAG, "Peers size >>> " + peerList.size());
        return peerList.size();
    }

    @Override
    public Object getItem(int position) {
        return peerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return peerList.get(position).getViewType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == ListGridPeer.VIEW_TYPE_ME_PEER) {
            ViewHolderMePeer vhm = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_list_me_peers, null);
                vhm = new ViewHolderMePeer();
                convertView.setTag(vhm);
            } else {
                vhm = (ViewHolderMePeer) convertView.getTag();
            }

            vhm.meView = convertView.findViewById(R.id.cellMeView);
            MeProps meProps = new MeProps(((AppCompatActivity) mContext).getApplication(), mStore);
            meProps.connect(mLifecycleOwner);
            vhm.meView.setProps(meProps, mMeetingClient);

            vhm.peerView = convertView.findViewById(R.id.cellPeerView2);
            Peer p3 = peerList.get(position).getPeer2();
            if (p3 != null) {
                Log.d(TAG, "Showing Peer3 for position >>> " + position);
                PeerProps peerProps = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
                peerProps.connect(mLifecycleOwner, p3.getId());
                vhm.peerView.setProps(peerProps, mMeetingClient);
                vhm.peerView.setName(p3.getDisplayName());

                vhm.peerView.getLayoutParams().width = getColumnWidth(2);
                vhm.peerView.getLayoutParams().height = getRowHeight();
                vhm.meView.getLayoutParams().width = getColumnWidth(2);
                vhm.meView.getLayoutParams().height = getRowHeight();
            } else {
                vhm.meView.getLayoutParams().width = getColumnWidth(1);
                vhm.meView.getLayoutParams().height = getRowHeight();
                vhm.peerView.getLayoutParams().width = 0;
            }
            return convertView;
        }
        ViewHolderPeerPeer vhpp = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_list_peers, null);
            vhpp = new ViewHolderPeerPeer();
            convertView.setTag(vhpp);
        } else {
            vhpp = (ViewHolderPeerPeer) convertView.getTag();
        }

        vhpp.peerView1 = convertView.findViewById(R.id.cellPeerView1);
        Peer p1 = peerList.get(position).getPeer1();
        PeerProps props1 = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
        props1.connect(mLifecycleOwner, p1.getId());
        vhpp.peerView1.setProps(props1, mMeetingClient);
        vhpp.peerView1.setName(p1.getDisplayName());

        vhpp.peerView2 = convertView.findViewById(R.id.cellPeerView3);
        Peer p2 = peerList.get(position).getPeer2();
        if (p2 != null) {
            Log.d(TAG, "Showing Peer2 for position >>> " + position);
            PeerProps props2 = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
            props2.connect(mLifecycleOwner, p2.getId());
            vhpp.peerView2.setProps(props2, mMeetingClient);
            vhpp.peerView2.setName(p2.getDisplayName());

            vhpp.peerView1.getLayoutParams().width = getColumnWidth(2);
            vhpp.peerView1.getLayoutParams().height = getRowHeight();
            vhpp.peerView2.getLayoutParams().width = getColumnWidth(2);
            vhpp.peerView2.getLayoutParams().height = getRowHeight();
        } else {
            vhpp.peerView1.getLayoutParams().width = getColumnWidth(1);
            vhpp.peerView1.getLayoutParams().height = getRowHeight();
            vhpp.peerView2.getLayoutParams().width = 0;
        }
        return convertView;
    }

    private int getRowHeight() {
        if (peerList.size() == 2) {
            return getRowHeight(2);
        } else if (peerList.size() == 3) {
            return getRowHeight(3);
        }
        return getRowHeight(1);
    }

    /**
     * @param rowCount
     * @return
     */
    private int getRowHeight(int rowCount) {
        switch (rowCount) {
            case 2:
                return (mDeviceHeight / 2) - 40;
            case 3:
                return (mDeviceHeight / 3) - 40;
            default:
                return mDeviceHeight - 50;
        }
    }

    /**
     * @param colCount
     * @return
     */
    private int getColumnWidth(int colCount) {
        if (colCount == 2) {
            return mDeviceWidth / 2;
        }
        return mDeviceWidth;
    }

    /**
     *
     * @param bottomBarHeight
     */
    public void setBottomBarHeight(int bottomBarHeight) {
        this.mBottomBarHeight = bottomBarHeight;
    }

    /**
     *
     * @param peerList
     */
    public void replaceList(final @NonNull List<ListGridPeer> peerList) {
        this.peerList = peerList;
        Log.v(TAG, "Reloading peers anyway ... for size >> " + peerList.size());
        notifyDataSetChanged();
    }

    /**
     *
     * @param peerList
     * @return
     */
    public boolean shouldReload(List<ListGridPeer> peerList) {
        return getCount() != peerList.size();
    }

    public int getPageNo() {
        return mPageNo;
    }

    static class ViewHolderMePeer {
        MeView meView;
        PeerView peerView;
    }

    static class ViewHolderPeerPeer {
        PeerView peerView1;
        PeerView peerView2;
    }

} /** end class. */
