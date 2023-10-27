package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.utils.WooEvents;
import com.woooapp.meeting.impl.views.MeView;
import com.woooapp.meeting.impl.views.PeerView;
import com.woooapp.meeting.impl.views.models.ListGridPeer;
import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.model.Peer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;
import pk.muneebahmad.lib.graphics.SP;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 4:45 pm 10/10/2023
 * <code>class</code> PeerAdapter2.java
 */
public class PeerAdapter2 extends BaseAdapter implements Handler.Callback {

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
    private boolean translationEnabled = false;
    private Handler handler;
    private boolean shouldAnimate = false;

    /**
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

        this.handler = new Handler(this);
        WooEvents.getInstance().addHandler(handler);

        this.mDeviceWidth = Display.getDisplayWidth(mContext) - 40;
        this.mDeviceHeight = Display.getDisplayHeight(mContext) - (int) SP.valueOf(mContext, 80);
    }

    @Override
    public int getCount() {
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
        if (mPageNo == 1) {
            return 2;
        }
        return 1;
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

            vhm.meView.setCamEnabled(peerList.get(position).isPeer1CamOn());
            vhm.meView.setMicEnabled(peerList.get(position).isPeer1MicOn());
            vhm.meView.setHandRaisedState(peerList.get(position).isPeer1HandRaised());

            MeProps meProps = new MeProps(((AppCompatActivity) mContext).getApplication(), mStore);
            meProps.connect(mLifecycleOwner);

            vhm.meView.setProps(meProps, mMeetingClient);

            vhm.peerView = convertView.findViewById(R.id.cellPeerView2);
            Peer p3 = peerList.get(position).getPeer2();
            if (p3 != null) {
                PeerProps peerProp = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
                peerProp.connect(mLifecycleOwner, p3.getId());
                vhm.peerView.setProps(peerProp, mMeetingClient);
                vhm.peerView.setName(p3.getDisplayName());
                if (position % 2 == 0) {
                    vhm.peerView.setTitleBgDrawable(R.drawable.bg_rounded_red);
                } else if (position % 3 == 0) {
                    vhm.peerView.setTitleBgDrawable(R.drawable.bg_rounded_gray);
                } else {
                    vhm.peerView.setTitleBgDrawable(R.drawable.bg_rounded_blue);
                }

                vhm.peerView.setCameraState(peerList.get(position).isPeer2CamOn());
                vhm.peerView.setMicState(peerList.get(position).isPeer2MicOn());
                vhm.peerView.setHandRaisedState(peerList.get(position).isPeer2HandRaised());

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

        if (position % 2 == 0) {
            vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_green);
        } else if (position % 3 == 0) {
            vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_red);
        } else {
            vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_gray);
        }

        vhpp.peerView1.setCameraState(peerList.get(position).isPeer1CamOn());
        vhpp.peerView1.setMicState(peerList.get(position).isPeer1MicOn());
        vhpp.peerView1.setHandRaisedState(peerList.get(position).isPeer1HandRaised());

        vhpp.peerView2 = convertView.findViewById(R.id.cellPeerView3);
        Peer p2 = peerList.get(position).getPeer2();
        if (p2 != null) {
            PeerProps props2 = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
            props2.connect(mLifecycleOwner, p2.getId());
            vhpp.peerView2.setProps(props2, mMeetingClient);
            vhpp.peerView2.setName(p2.getDisplayName());

            if (position % 2 == 0) {
                vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_blue);
            } else if (position % 3 == 0) {
                vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_green);
            } else {
                vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_red);
            }

            vhpp.peerView2.setCameraState(peerList.get(position).isPeer2CamOn());
            vhpp.peerView2.setMicState(peerList.get(position).isPeer2MicOn());
            vhpp.peerView2.setHandRaisedState(peerList.get(position).isPeer2HandRaised());

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
                if (translationEnabled) return (mDeviceHeight / 2) - (int) SP.valueOf(mContext, 50);
                return (mDeviceHeight / 2) - 5;
            case 3:
                if (translationEnabled) return (mDeviceHeight / 3) - (int) SP.valueOf(mContext, 33);
                return (mDeviceHeight / 3) - 5;
            default:
                if (translationEnabled) return mDeviceHeight - (int) SP.valueOf(mContext, 100);
                return mDeviceHeight;
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
     * @param viewPagerHeight
     */
    public void setViewPagerHeight(int viewPagerHeight) {
//        this.mBottomBarHeight = bottomBarHeight;
//        this.mDeviceHeight = viewPagerHeight - 10;
//        notifyDataSetChanged();
    }

    /**
     * @param enabled
     */
    public void setTranslation(boolean enabled) {
        this.translationEnabled = enabled;
        this.notifyDataSetChanged();
    }

    /**
     * @param peerList
     */
    public void replaceList(final @NonNull List<ListGridPeer> peerList) {
        if (shouldReplaceList(peerList)) {
            this.shouldAnimate = true;
        } else {
            this.shouldAnimate = false;
        }
        Log.d(TAG, "<< Should Animate [" + this.shouldAnimate + "]");
        this.peerList = peerList;
        notifyDataSetChanged();
    }

    /**
     * @param peerList
     * @return
     */
    public boolean shouldReplaceList(List<ListGridPeer> peerList) {
        return getCount() != peerList.size();
    }

    public int getPageNo() {
        return mPageNo;
    }

    public void dispose() {
        if (this.handler != null) {
            WooEvents.getInstance().removeHandler(handler);
//            this.handler = null;
        }
    }

    public void resume() {
//        this.handler = new Handler(this);
//        WooEvents.getInstance().addHandler(handler);
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (peerList.size() > 0) {
            switch (msg.what) {
                case WooEvents.EVENT_ME_CAM_TURNED_ON:
                    if (mPageNo == 1) {
                        Log.d(TAG, "<<< ME CAM TURNED ON >>>");
                        if (peerList.size() > 0) {
                            peerList.get(0).setPeer1CamOn(true);
                            notifyDataSetChanged();
                        }
                    }
                    return true;
                case WooEvents.EVENT_ME_CAM_TURNED_OFF:
                    if (mPageNo == 1) {
                        Log.d(TAG, "<<< ME CAM TURNED OFF >>>");
                        if (peerList.size() > 0) {
                            peerList.get(0).setPeer1CamOn(false);
                            notifyDataSetChanged();
                        }
                    }
                    return true;
                case WooEvents.EVENT_ME_MIC_TURNED_ON:
                    if (mPageNo == 1) {
                        Log.d(TAG, "<< ME MIC TURNED ON >>");
                        if (peerList.size() > 0) {
                            peerList.get(0).setPeer1MicOn(true);
                            notifyDataSetChanged();
                        }
                    }
                    return true;
                case WooEvents.EVENT_ME_MIC_TURNED_OFF:
                    if (mPageNo == 1) {
                        Log.d(TAG, "<< ME MIC TURNED OFF >>");
                        if (peerList.size() > 0) {
                            peerList.get(0).setPeer1MicOn(false);
                            notifyDataSetChanged();
                        }
                    }
                    return true;
                case WooEvents.EVENT_PEER_CAM_TURNED_ON:
                    if (msg.obj != null) {
                        try {
                            JSONObject camOn = (JSONObject) msg.obj;
                            String pId = camOn.getString("socketId");
                            int peer1Index = findPeer1Index(pId);
                            int peer2Index = findPeer2Index(pId);
                            if (peer1Index != -1) {
                                peerList.get(peer1Index).setPeer1CamOn(true);
                                Log.d(TAG, "<< PEER 1 CAM TURNED ON >> " + pId);
                                notifyDataSetChanged();
                            }
                            if (peer2Index != -1) {
                                peerList.get(peer2Index).setPeer2CamOn(true);
                                Log.d(TAG, "<< PEER 2 CAM TURNED ON >> " + pId);
                                notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                case WooEvents.EVENT_PEER_CAM_TURNED_OFF:
                    if (msg.obj != null) {
                        try {
                            JSONObject camOn = (JSONObject) msg.obj;
                            String pId = camOn.getString("socketId");
                            int peer1Index = findPeer1Index(pId);
                            int peer2Index = findPeer2Index(pId);
                            if (peer1Index != -1) {
                                peerList.get(peer1Index).setPeer1CamOn(false);
                                Log.d(TAG, "<< PEER 1 CAM TURNED OFF >> " + pId);
                                notifyDataSetChanged();
                            }
                            if (peer2Index != -1) {
                                peerList.get(peer2Index).setPeer2CamOn(false);
                                Log.d(TAG, "<< PEER 2 CAM TURNED OFF >> " + pId);
                                notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                case WooEvents.EVENT_PEER_MIC_MUTED:
                    if (msg.obj != null) {
                        try {
                            JSONObject camOn = (JSONObject) msg.obj;
                            String pId = camOn.getString("socketId");
                            int peer1Index = findPeer1Index(pId);
                            int peer2Index = findPeer2Index(pId);
                            if (peer1Index != -1) {
                                peerList.get(peer1Index).setPeer1MicOn(false);
                                Log.d(TAG, "<< PEER 1 MIC TURNED ON >> " + pId);
                                notifyDataSetChanged();
                            }
                            if (peer2Index != -1) {
                                peerList.get(peer2Index).setPeer2MicOn(false);
                                Log.d(TAG, "<< PEER 2 MIC TURNED ON >> " + pId);
                                notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                case WooEvents.EVENT_PEER_MIC_UNMUTED:
                    if (msg.obj != null) {
                        try {
                            JSONObject camOn = (JSONObject) msg.obj;
                            String pId = camOn.getString("socketId");
                            int peer1Index = findPeer1Index(pId);
                            int peer2Index = findPeer2Index(pId);
                            if (peer1Index != -1) {
                                peerList.get(peer1Index).setPeer1MicOn(true);
                                Log.d(TAG, "<< PEER 1 MIC TURNED OFF >> " + pId);
                                notifyDataSetChanged();
                            }
                            if (peer2Index != -1) {
                                peerList.get(peer2Index).setPeer2MicOn(true);
                                Log.d(TAG, "<< PEER 2 MIC TURNED OFF >> " + pId);
                                notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                case WooEvents.EVENT_PEER_HAND_RAISED:
                    if (msg.obj != null) {
                        try {
                            JSONObject camOn = (JSONObject) msg.obj;
                            String pId = camOn.getString("socketId");
                            int peer1Index = findPeer1Index(pId);
                            int peer2Index = findPeer2Index(pId);
                            if (peer1Index != -1) {
                                peerList.get(peer1Index).setPeer1HandRaised(true);
                                Log.d(TAG, "<< PEER 1 RAISED HAND >> " + pId + " for index [" + peer1Index + "]");
                                notifyDataSetChanged();
                            }
                            if (peer2Index != -1) {
                                peerList.get(peer2Index).setPeer2HandRaised(true);
                                Log.d(TAG, "<< PEER 2 RAISED HAND >> " + pId + " for index [" + peer2Index + "]");
                                notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                case WooEvents.EVENT_PEER_HAND_LOWERED:
                    if (msg.obj != null) {
                        try {
                            JSONObject camOn = (JSONObject) msg.obj;
                            String pId = camOn.getString("socketId");
                            int peer1Index = findPeer1Index(pId);
                            int peer2Index = findPeer2Index(pId);
                            if (peer1Index != -1) {
                                peerList.get(peer1Index).setPeer1HandRaised(false);
                                Log.d(TAG, "<< PEER 1 LOWERED HAND >> " + pId);
                                notifyDataSetChanged();
                            }
                            if (peer2Index != -1) {
                                peerList.get(peer2Index).setPeer2HandRaised(false);
                                Log.d(TAG, "<< PEER 2 LOWERED HAND >> " + pId);
                                notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;

                default:
                    return false;
            }
        }
        return false;
    }

    /**
     *
     * @param peerId
     * @return
     */
    private Peer findPeerWithId(String peerId) {
        for (ListGridPeer p : peerList) {
            if (p.getPeer1() != null) {
                if (p.getPeer1().getId().equals(peerId)) {
                    return p.getPeer1();
                }
            }
            if (p.getPeer2() != null) {
                if (p.getPeer2().getId().equals(peerId)) {
                    return p.getPeer2();
                }
            }
        }
        return null;
    }

    private int findPeer1Index(String peerId) {
        if (peerList.size() > 0) {
            for (int i = 0; i < peerList.size(); i++) {
                if (peerList.get(i).getPeer1() != null) {
                    if (peerList.get(i).getPeer1().getId().equals(peerId)) {
                        Log.d(TAG, "Updating Peer 1 for index " + i);
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private int findPeer2Index(String peerId) {
        if (peerList.size() > 0) {
            for (int i = 0; i < peerList.size(); i++) {
                if (peerList.get(i).getPeer2() != null) {
                    if (peerList.get(i).getPeer2().getId().equals(peerId)) {
                        Log.d(TAG, "Updating Peer 2 for index " + i);
                        return i;
                    }
                }
            }
        }
        return -1;
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
