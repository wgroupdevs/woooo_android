package com.woooapp.meeting.impl.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.utils.WooDirector;
import com.woooapp.meeting.impl.utils.WooEvents;
import com.woooapp.meeting.impl.views.MeView;
import com.woooapp.meeting.impl.views.PeerView;
import com.woooapp.meeting.impl.views.models.ListGridPeer;
import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.model.Peer;
import com.woooapp.meeting.net.models.RoomData;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.MediaStreamTrack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eu.siacs.conversations.R;
import pk.muneebahmad.lib.graphics.CircleDrawable;
import pk.muneebahmad.lib.graphics.SP;
import pk.muneebahmad.lib.net.Http;
import pk.muneebahmad.lib.net.HttpImageAdapter;

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
    private MeProps meProp;
    private PeerProps peer1Prop;
    private final Map<String, PeerProps> propsMap = new LinkedHashMap<>();
    private boolean destroy = false;

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
            if (meProp == null) {
                meProp = new MeProps(((AppCompatActivity) mContext).getApplication(), mStore);
            }
            meProp.connect(mLifecycleOwner);
            vhm.meView.setProps(meProp, mMeetingClient);

            vhm.peerView = convertView.findViewById(R.id.cellPeerView2);
            Peer p3 = peerList.get(position).getPeer2();
            if (p3 != null) {
                if (propsMap.get(p3.getId()) == null) {
                    PeerProps peerProp = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
                    peerProp.connect(mLifecycleOwner, p3.getId());
                    propsMap.put(p3.getId(), peerProp);
                    vhm.peerView.setProps(peerProp, mMeetingClient);
                } else {
                    vhm.peerView.setProps(propsMap.get(p3.getId()), mMeetingClient);
                }
                vhm.peerView.setName(p3.getDisplayName());
                if (WooDirector.getInstance().isCameraOn(p3.getId())) {
                    vhm.peerView.setVideoState2(true);
                } else {
                    vhm.peerView.setVideoState2(false);
                }
                vhm.peerView.setImageForPeer(p3.getId());

                if (position % 2 == 0) {
                    vhm.peerView.setTitleBgDrawable(R.drawable.bg_rounded_red);
                } else if (position % 3 == 0) {
                    vhm.peerView.setTitleBgDrawable(R.drawable.bg_rounded_gray);
                } else {
                    vhm.peerView.setTitleBgDrawable(R.drawable.bg_rounded_blue);
                }

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
        if (p1 != null) {
            if (propsMap.get(p1.getId()) == null) {
                PeerProps props1 = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
                props1.connect(mLifecycleOwner, p1.getId());
                propsMap.put(p1.getId(), props1);
                vhpp.peerView1.setProps(props1, mMeetingClient);
            } else {
                vhpp.peerView1.setProps(propsMap.get(p1.getId()), mMeetingClient);
            }
            vhpp.peerView1.setName(p1.getDisplayName());
            if (WooDirector.getInstance().isCameraOn(p1.getId())) {
                vhpp.peerView1.setVideoState2(true);
            } else {
                vhpp.peerView1.setVideoState2(false);
            }
            vhpp.peerView1.setImageForPeer(p1.getId());

            if (position % 2 == 0) {
                vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_green);
            } else if (position % 3 == 0) {
                vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_red);
            } else {
                vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_gray);
            }
        }

        vhpp.peerView2 = convertView.findViewById(R.id.cellPeerView3);
        Peer p2 = peerList.get(position).getPeer2();
        if (p2 != null) {
            if (propsMap.get(p2.getId()) == null) {
                PeerProps props2 = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
                props2.connect(mLifecycleOwner, p2.getId());
                propsMap.put(p2.getId(), props2);
                vhpp.peerView2.setProps(props2, mMeetingClient);
            } else {
                vhpp.peerView2.setProps(propsMap.get(p2.getId()), mMeetingClient);
            }
            vhpp.peerView2.setName(p2.getDisplayName());
            if (WooDirector.getInstance().isCameraOn(p2.getId())) {
                vhpp.peerView2.setVideoState2(true);
            } else {
                vhpp.peerView2.setVideoState2(false);
            }
            vhpp.peerView2.setImageForPeer(p2.getId());

            if (position % 2 == 0) {
                vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_blue);
            } else if (position % 3 == 0) {
                vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_green);
            } else {
                vhpp.peerView1.setTitleBgDrawable(R.drawable.bg_rounded_red);
            }

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
            meProp = null;
            propsMap.clear();
            this.peerList = new LinkedList<>();
            notifyDataSetChanged();
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
                case WooEvents.EVENT_ME_CAM_TURNED_OFF:
                case WooEvents.EVENT_ME_MIC_TURNED_ON:
                case WooEvents.EVENT_ME_MIC_TURNED_OFF:
                case WooEvents.EVENT_ME_HAND_RAISED:
                case WooEvents.EVENT_ME_HAND_LOWERED:
                    if (mPageNo == 1) {
                        notifyDataSetChanged();
                        return true;
                    }
                    return false;
                case WooEvents.EVENT_PEER_CAM_TURNED_ON:
                    if (mMeetingClient != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj != null) {
                            try {
                                String pId = obj.getString("socketId");
                                WooDirector.getInstance().addPeerVideoState(pId, true);
                                notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return true;
                case WooEvents.EVENT_PEER_CAM_TURNED_OFF:
                    if (mMeetingClient != null) {
                        JSONObject obj = (JSONObject) msg.obj;
                        if (obj != null) {
                            try {
                                String pId = obj.getString("socketId");
                                WooDirector.getInstance().addPeerVideoState(pId, false);
                                notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    notifyDataSetChanged();
                    return true;
                case WooEvents.EVENT_PEER_HAND_LOWERED:
                case WooEvents.EVENT_PEER_HAND_RAISED:
                case WooEvents.EVENT_PEER_MIC_MUTED:
                case WooEvents.EVENT_PEER_MIC_UNMUTED:
                case WooEvents.EVENT_PEER_ADAPTER_NOTIFY:
                    if (msg.obj != null) {
                        notifyDataSetChanged();
                    }
                    return true;
                case WooEvents.EVENT_DESTROY:
                    destroy = true;
                    peerList.clear();
                    notifyDataSetChanged();
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
