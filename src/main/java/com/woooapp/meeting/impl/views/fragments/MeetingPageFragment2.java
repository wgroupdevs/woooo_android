package com.woooapp.meeting.impl.views.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.utils.WEvents;
import com.woooapp.meeting.impl.views.MeView;
import com.woooapp.meeting.impl.views.PeerView;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.views.models.GridPeer;
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
import okhttp3.internal.http2.Http2Reader;
import pk.muneebahmad.lib.graphics.SP;
import pk.muneebahmad.lib.views.containers.FlowLayout;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 4:44 pm 19/10/2023
 * <code>class</code> MeetingPageFragment2.java
 */
public class MeetingPageFragment2 extends Fragment implements Handler.Callback {

    private static final String TAG = MeetingPageFragment2.class.getSimpleName() + ".java";
    private final int pageNo;
    private final LifecycleOwner lifecycleOwner;
    private final RoomStore mStore;
    private final MeetingClient mMeetingClient;
    private Context mContext;
    private int mDeviceWidth;
    private int mDeviceHeight;
    private int mBottomBarHeight = 90;
    private boolean translationEnabled = false;
    private FrameLayout mFrameLayout;
    private List<GridPeer> peerList = new LinkedList<>();
    private final Handler handler;
    private MeView meView;

    /**
     * @param context
     * @param pageNo
     * @param lifecycleOwner
     * @param store
     * @param meetingClient
     */
    public MeetingPageFragment2(@NonNull Context context,
                                int pageNo,
                                @NonNull LifecycleOwner lifecycleOwner,
                                @NonNull RoomStore store,
                                @NonNull MeetingClient meetingClient) {
        this.mContext = context;
        this.pageNo = pageNo;
        this.lifecycleOwner = lifecycleOwner;
        this.mStore = store;
        this.mMeetingClient = meetingClient;

        this.handler = new Handler(this);
        WEvents.getInstance().addHandler(handler);

        this.mDeviceWidth = Display.getDisplayWidth(mContext);
        this.mDeviceHeight = Display.getDisplayHeight(mContext) - mBottomBarHeight;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meeting3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initComponents(view);
    }

    /**
     * @param view
     */
    private void initComponents(@NonNull View view) {
        this.mFrameLayout = view.findViewById(R.id.flowLayout);

        if (peerList.size() > 0 && pageNo == 1) {
            if (peerList.get(0).getViewType() == GridPeer.VIEW_TYPE_ME) {
                addMeView();
            }
        }
    }

    private int getRowHeight() {
        if (peerList.size() == 2 || peerList.size() == 3 || peerList.size() == 4) {
            return getRowHeight(2);
        } else if (peerList.size() == 5 || peerList.size() == 6) {
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
                if (translationEnabled)
                    return (mDeviceHeight / 2) - (int) SP.valueOf(mContext, 60);
                return (mDeviceHeight / 2) - 40;
            case 3:
                if (translationEnabled)
                    return (mDeviceHeight / 3) - (int) SP.valueOf(mContext, 40);
                return (mDeviceHeight / 3) - 40;
            default:
                if (translationEnabled) return mDeviceHeight - (int) SP.valueOf(mContext, 120);
                return mDeviceHeight - (int) SP.valueOf(mContext, 32.5f);
        }
    }

    /**
     * @return
     */
    private int getColumnWidth(int index) {
        switch (peerList.size()) {
            case 3:
                if (index == 2) {
                    return mDeviceWidth;
                } else {
                    return mDeviceWidth / 2;
                }
            case 4:
            case 5:
                if (index == 0 || index == 1 || index == 2 || index == 3) {
                    return mDeviceWidth / 2;
                } else {
                    return mDeviceWidth;
                }
            case 6:
                return mDeviceWidth / 2;
            default:
                return mDeviceWidth;
        }
    }

    /**
     *
     * @param index
     * @param addMargin
     */
    private void resetLocation(int index, boolean addMargin) {
        View child = mFrameLayout.getChildAt(index);
        switch (peerList.size()) {
            case 2:
                if (index == 1) {
                    if (child != null) {
                        WooAnimationUtil.resetLocation(child,
                                0,
                                0,
                                mDeviceHeight,
                                child.getLayoutParams().height + (addMargin ? 5 : 0),
                                null);
                    }
                }
                break;
            case 3:
                if (index == 1) {
                    if (child != null) {
                        WooAnimationUtil.resetLocation(child,
                                0,
                                child.getLayoutParams().width,
                                mDeviceHeight,
                                mFrameLayout.getChildAt(0).getY(),
                                null);
                    }
                } else if (index == 2) {
                    if (child != null) {
                        WooAnimationUtil.resetLocation(child,
                                0,
                                0,
                                mDeviceHeight,
                                mFrameLayout.getChildAt(0).getY() + mFrameLayout.getChildAt(0).getLayoutParams().height + (addMargin ? 5 : 0),
                                null);
                    }
                }
                break;
            case 4:
                if (index == 2) {
                    if (child != null && !addMargin) {
                        WooAnimationUtil.resetLocation(child,
                                0,
                                0,
                                mDeviceHeight,
                                mFrameLayout.getChildAt(0).getY() + mFrameLayout.getChildAt(0).getLayoutParams().height,
                                null);
                    }
                } else if (index == 3) {
                    if (child != null) {
                        WooAnimationUtil.resetLocation(child,
                                0,
                                mFrameLayout.getChildAt(2).getX() + mFrameLayout.getChildAt(2).getLayoutParams().width,
                                mDeviceHeight,
                                mFrameLayout.getChildAt(0).getY() + mFrameLayout.getChildAt(0).getLayoutParams().height + (addMargin ? 5 : 0),
                                null);
                    }
                }
                break;
            case 5:
                if (index == 4) {
                    if (child != null) {
                        WooAnimationUtil.resetLocation(mFrameLayout.getChildAt(2),
                                mFrameLayout.getChildAt(2).getX(),
                                mFrameLayout.getChildAt(2).getX(),
                                mFrameLayout.getChildAt(2).getY(),
                                mFrameLayout.getChildAt(0).getY() + mFrameLayout.getChildAt(2).getLayoutParams().height + (addMargin ? 5 : 0),
                                null);

                        WooAnimationUtil.resetLocation(mFrameLayout.getChildAt(3),
                                mFrameLayout.getChildAt(3).getX(),
                                mFrameLayout.getChildAt(3).getX(),
                                mFrameLayout.getChildAt(3).getY(),
                                mFrameLayout.getChildAt(0).getY() + mFrameLayout.getChildAt(3).getLayoutParams().height + (addMargin ? 5 : 0),
                                new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        WooAnimationUtil.resetLocation(child,
                                                0,
                                                0,
                                                mDeviceHeight,
                                                mFrameLayout.getChildAt(3).getY() + child.getLayoutParams().height + (addMargin ? 5 : 0),
                                                null);
                                    }
                                });
                    }
                }
                break;
            case 6:
                if (!addMargin) {
                    if (index == 2) {
                        if (child != null) {
                            WooAnimationUtil.resetLocation(child,
                                    0,
                                    0,
                                    mDeviceHeight,
                                    mFrameLayout.getChildAt(0).getY() + mFrameLayout.getChildAt(0).getLayoutParams().height,
                                    null);
                        }
                    } else if (index == 3) {
                        if (child != null) {
                            WooAnimationUtil.resetLocation(child,
                                    0,
                                    mFrameLayout.getChildAt(0).getX() + mFrameLayout.getChildAt(0).getLayoutParams().width,
                                    mDeviceHeight,
                                    mFrameLayout.getChildAt(0).getY() + mFrameLayout.getChildAt(0).getLayoutParams().height,
                                    null);
                        }
                    } else if (index == 4) {
                        if (child != null) {
                            WooAnimationUtil.resetLocation(child,
                                    0,
                                    0,
                                    mDeviceHeight,
                                    mFrameLayout.getChildAt(0).getY() + (child.getLayoutParams().height * 2),
                                    null);
                        }
                    } else if (index == 5) {
                        if (child != null) {
                            WooAnimationUtil.resetLocation(child,
                                    0,
                                    child.getLayoutParams().width,
                                    mDeviceHeight,
                                    mFrameLayout.getChildAt(0).getY() + (child.getLayoutParams().height * 2),
                                    null);
                        }
                    }
                } else {
                    if (index == 5) {
                        if (child != null) {
                            WooAnimationUtil.resetLocation(child,
                                    0,
                                    child.getLayoutParams().width,
                                    mDeviceHeight,
                                    mFrameLayout.getChildAt(4).getY(),
                                    null);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * @param bottomBarHeight
     */
    public void setBottomBarHeight(int bottomBarHeight) {
        this.mBottomBarHeight = bottomBarHeight;
    }

    /**
     * @param enabled
     */
    public void setTranslation(boolean enabled) {
        this.translationEnabled = enabled;
    }

    /**
     * @param peerId
     */
    private void updateViewOnNewPeer(@NonNull String peerId) {
        for (int i = 0; i < peerList.size(); i++) {
            GridPeer gridPeer = peerList.get(i);
            if (gridPeer != null) {
                if (gridPeer.getViewType() == GridPeer.VIEW_TYPE_PEER) {
                    if (gridPeer.getPeer().getId().equals(peerId)) {
                        FrameLayout.LayoutParams childParams = new FrameLayout.LayoutParams(getColumnWidth(i), getRowHeight());
                        childParams.setMargins(1, 1, 1, 1);
                        PeerView p = new PeerView(mContext);
                        p.setElevation(15f);
                        p.setTag(peerId);
                        p.setImageForPeer(gridPeer.getPeer().getId());
                        if (p.getProps() == null) {
                            PeerProps props = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
                            p.setProps(props, mMeetingClient, gridPeer.getPeer().getId());
                            int finalI = i;
                            mStore.getPeers().postValue(peers -> {
                                Peer peer = peers.getPeer(peerId);
                                if (peer != null) {
                                    p.setName(peer.getDisplayName());
                                }
                            });
                            mFrameLayout.addView(p, childParams);
                            props.connect(lifecycleOwner, peerId);
                        }
                        updateCanvas(true);
                        break;
                    }
                }
            }
        }
    }

    private void addMeView() {
        FrameLayout.LayoutParams childParams = new FrameLayout.LayoutParams(getColumnWidth(0), getRowHeight());
        childParams.setMargins(1, 1, 1, 1);
        meView = new MeView(mContext);
        meView.setElevation(10f);
        MeProps props = new MeProps(((AppCompatActivity) mContext).getApplication(), mStore);
        meView.setProps(props, mMeetingClient);
        meView.setLayoutParams(childParams);
        meView.setTag("me");
        mFrameLayout.addView(meView, childParams);
        props.connect(lifecycleOwner);
        Log.d(TAG, "Added Me View 2 ...");
        updateCanvas(false);
    }

    /**
     *
     * @param resetLocation
     */
    private void updateCanvas(boolean resetLocation) {
        if (mFrameLayout != null) {
            for (int i = 0; i < mFrameLayout.getChildCount(); i++) {
                View child = mFrameLayout.getChildAt(i);
                Log.d(TAG, "Updating Canvas View at index " + i);
                if (child != null) {
                    if (child.getLayoutParams() != null) {
                        child.getLayoutParams().width = getColumnWidth(i);
                        child.getLayoutParams().height = getRowHeight();
                        Log.d(TAG, "Set height of peer to " + child.getLayoutParams().height);
                        child.invalidate();
                        if (!resetLocation) {
                            resetLocation(i, false);
                        } else {
                            resetLocation(i, true);
                        }
                    }
                }
            }
            mFrameLayout.invalidate();
            if (!resetLocation) mFrameLayout.requestLayout();
        }
    }

    /**
     * @param peerId
     */
    private void removeView(@NonNull String peerId) {
        for (int i = 0; i < mFrameLayout.getChildCount(); i++) {
            if (!mFrameLayout.getChildAt(i).getTag().equals("me")) {
                PeerView child = (PeerView) mFrameLayout.getChildAt(i);
                if (child != null) {
                    if (child.getTag() != null) {
                        if (child.getTag().equals(peerId)) {
                            WooAnimationUtil.hideView(child, new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    if (child.getProps() != null) {
                                        child.getProps().disconnect(lifecycleOwner, peerId);
                                    }
                                    mFrameLayout.removeView(child);
                                    updateCanvas(true);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    /**
     * @param gridPeer
     */
    public void addPeerItem(@NonNull GridPeer gridPeer) {
        if (peerList.size() > 0) {
            if (gridPeer.getPeer() != null) {
                if (!gridPeer.getPeer().isScreenShareConsumer()) {
                    if (gridPeer.getViewType() == GridPeer.VIEW_TYPE_PEER) {
                        for (GridPeer p : peerList) {
                            if (p.getViewType() == GridPeer.VIEW_TYPE_PEER) {
                                if (p.getPeer().getId() != null) {
                                    if (p.getPeer().getId().equals(gridPeer.getPeer().getId())) {
                                        Log.w(TAG, "<< View already exists for Peer with ID >> " + gridPeer.getPeer().getId() + " >> Skipping this one ...");
                                        return;
                                    }
                                }
                            }
                        }
                        peerList.add(gridPeer);
                        updateViewOnNewPeer(gridPeer.getPeer().getId());
                    }
                }
            }
        } else {
            if (meView == null) {
                peerList.add(gridPeer);
                Log.d(TAG, "Added a new me to list ...");
            }
        }
    }

    /**
     * @param peerId
     * @return
     */
    public boolean removePeerItem(@NonNull String peerId) {
        for (int i = 0; i < peerList.size(); i++) {
            if (peerList.get(i).getViewType() == GridPeer.VIEW_TYPE_PEER) {
                if (peerList.get(i).getPeer().getId().equals(peerId)) {
                    try {
                        peerList.remove(i);
                        Log.d(TAG, "<< Removed local peer at index >> " + i);
                        removeView(peerId);
                        return true;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @param socketId
     * @return
     */
    @Nullable
    private PeerView getPeerView(@NonNull String socketId) {
        if (mFrameLayout != null) {
            for (int i = 0; i < mFrameLayout.getChildCount(); i++) {
                if (mFrameLayout.getChildAt(i).getTag() != null) {
                    if (mFrameLayout.getChildAt(i).getTag().equals(socketId)) {
                        return (PeerView) mFrameLayout.getChildAt(i);
                    }
                }
            }
        }
        return null;
    }

    public void enableTranslation() {
        this.translationEnabled = true;
        Log.d(TAG, "<< Setting layout dimensions on translation enabled ...");
        updateCanvas(false);
    }

    public void disableTranslation() {
        this.translationEnabled = false;
        updateCanvas(false);
    }

    public void dispose() {
        if (this.handler != null) {
            WEvents.getInstance().removeHandler(handler);
        }
        this.mFrameLayout.removeAllViews();
        this.peerList.clear();
    }


    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case WEvents.EVENT_ME_CAM_TURNED_ON:
                if (pageNo == 1) {
                    if (meView != null) {
                        meView.setCamEnabled(true);
                    }
                    return true;
                }
                return false;
            case WEvents.EVENT_ME_CAM_TURNED_OFF:
                if (pageNo == 1) {
                    if (meView != null) {
                        meView.setCamEnabled(false);
                    }
                    return true;
                }
                return false;
            case WEvents.EVENT_ME_MIC_TURNED_ON:
                if (pageNo == 1) {
                    if (meView != null) {
                        meView.setMicEnabled(true);
                    }
                    return true;
                }
                return false;
            case WEvents.EVENT_ME_MIC_TURNED_OFF:
                if (pageNo == 1) {
                    if (meView != null) {
                        meView.setMicEnabled(false);
                    }
                    return true;
                }
                return false;
            case WEvents.EVENT_ME_HAND_RAISED:
                if (pageNo == 1) {
                    if (meView != null) {
                        meView.setHandRaisedState(true);
                    }
                    return true;
                }
                return false;
            case WEvents.EVENT_ME_HAND_LOWERED:
                if (pageNo == 1) {
                    if (meView != null) {
                        meView.setHandRaisedState(false);
                    }
                    return true;
                }
                return false;
            case WEvents.EVENT_TYPE_PEER_DISCONNECTED:
                JSONObject p = (JSONObject) msg.obj;
                try {
                    String pId = p.getString("id");
                    removePeerItem(pId);
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            case WEvents.EVENT_PEER_HAND_LOWERED:
                if (msg.obj != null) {
                    JSONObject obj = (JSONObject) msg.obj;
                    try {
                        String socketId = obj.getString("socketId");
                        PeerView pv = getPeerView(socketId);
                        if (pv != null) {
                            pv.setHandRaisedState(false);
                        }
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            case WEvents.EVENT_PEER_HAND_RAISED:
                if (msg.obj != null) {
                    JSONObject obj = (JSONObject) msg.obj;
                    try {
                        String socketId = obj.getString("socketId");
                        PeerView pv = getPeerView(socketId);
                        if (pv != null) {
                            pv.setHandRaisedState(true);
                        }
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            case WEvents.EVENT_PEER_MIC_MUTED:
                if (msg.obj != null) {
                    JSONObject obj = (JSONObject) msg.obj;
                    try {
                        String socketId = obj.getString("socketId");
                        PeerView pv = getPeerView(socketId);
                        if (pv != null) {
                            pv.setMicState(false);
                        }
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            case WEvents.EVENT_PEER_MIC_UNMUTED:
                if (msg.obj != null) {
                    JSONObject obj = (JSONObject) msg.obj;
                    try {
                        String socketId = obj.getString("socketId");
                        PeerView pv = getPeerView(socketId);
                        if (pv != null) {
                            pv.setMicState(true);
                        }
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            case WEvents.EVENT_ON_NEW_ADMIN:
                if (mFrameLayout != null) {
                    for (int i = 0; i < mFrameLayout.getChildCount(); i++) {
                        if (mFrameLayout.getChildAt(i).getTag() != null) {
                            if (!mFrameLayout.getChildAt(i).getTag().equals("me")) {
                                PeerView pv = (PeerView) mFrameLayout.getChildAt(i);
                                if (pv != null) {
                                    pv.setAdminStatus();
                                }
                            }
                        }
                    }
                }
                return false;
            case WEvents.EVENT_DESTROY:
                dispose();
                return true;
            default:
                return false;
        }
    }
} /**
 * end class.
 */
