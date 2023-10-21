package com.woooapp.meeting.impl.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.views.MeView;
import com.woooapp.meeting.impl.views.PeerView;
import com.woooapp.meeting.impl.views.models.ListGridPeer;
import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.lv.RoomStore;

import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;
import pk.muneebahmad.lib.graphics.SP;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 4:44 pm 19/10/2023
 * <code>class</code> MeetingPageFragment2.java
 */
public class MeetingPageFragment2 extends Fragment {

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
    private List<ListGridPeer> peerList = new LinkedList<>();
    private MeView meView;
    private PeerView peerView1;
    private PeerView peerView2;
    private PeerView peerView3;
    private PeerView peerView4;
    private PeerView peerView5;
    private PeerView peerView6;
    private TableLayout tableLayout;
    private TableRow row1;
    private TableRow row2;
    private TableRow row3;
    boolean loaded = false;

    /**
     *
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

        this.mDeviceWidth = Display.getDisplayWidth(mContext) - 40;
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
     *
     * @param view
     */
    private void initComponents(@NonNull View view) {
        this.tableLayout = view.findViewById(R.id.tableLayout);

        this.row1 = view.findViewById(R.id.row1);
        this.row2 = view.findViewById(R.id.row2);
        this.row3 = view.findViewById(R.id.row3);

        this.meView = view.findViewById(R.id.meView);
        this.peerView1 = view.findViewById(R.id.peerView1);
        this.peerView2 = view.findViewById(R.id.peerView2);
        this.peerView3 = view.findViewById(R.id.peerView3);
        this.peerView4 = view.findViewById(R.id.peerView4);
        this.peerView5 = view.findViewById(R.id.peerView5);
        this.peerView6 = view.findViewById(R.id.peerView6);

        if (pageNo == 1) {
            this.peerView1.setVisibility(View.GONE);
            this.meView.setVisibility(View.VISIBLE);
        } else {
            this.meView.setVisibility(View.GONE);
            this.peerView1.setVisibility(View.VISIBLE);
        }
        loaded = true;
    }

    private void updateDimensions() {
        if (meView != null && peerView1 != null && peerView2 != null && peerView3 != null && peerView4 != null && peerView5 != null && peerView6 != null) {
            if (meView.getLayoutParams() != null) meView.getLayoutParams().height = getRowHeight();
            if (peerView1.getLayoutParams() != null)
                peerView1.getLayoutParams().height = getRowHeight();
            if (peerView2.getLayoutParams() != null)
                peerView2.getLayoutParams().height = getRowHeight();
            if (peerView3.getLayoutParams() != null)
                peerView3.getLayoutParams().height = getRowHeight();
            if (peerView4.getLayoutParams() != null)
                peerView4.getLayoutParams().height = getRowHeight();
            if (peerView5.getLayoutParams() != null)
                peerView5.getLayoutParams().height = getRowHeight();
            if (peerView6.getLayoutParams() != null)
                peerView6.getLayoutParams().height = getRowHeight();
        }
    }

    private void updateVisibility() {
        if (meView != null && peerView1 != null && peerView2 != null && peerView3 != null && peerView4 != null && peerView5 != null && peerView6 != null) {
            switch (peerList.size()) {
                case 1:
                    if (peerList.get(0).getPeer2() != null) {
                        peerView3.setVisibility(View.VISIBLE);
                    } else {
                        peerView3.setVisibility(View.GONE);
                    }
                    peerView2.setVisibility(View.GONE);
                case 2:
                    peerView2.setVisibility(View.VISIBLE);
                    if (peerList.get(1).getPeer2() != null) {
                        peerView4.setVisibility(View.VISIBLE);
                    } else {
                        peerView4.setVisibility(View.GONE);
                    }
                    peerView5.setVisibility(View.GONE);
                    peerView6.setVisibility(View.GONE);
                    break;
                case 3:
                    peerView5.setVisibility(View.VISIBLE);
                    if (peerList.get(2).getPeer2() != null) {
                        peerView6.setVisibility(View.VISIBLE);
                    } else {
                        peerView6.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void updatePeers() {
        for (int i = 0; i < peerList.size(); i++) {
            if (i == 0) {
                if (pageNo == 1) {
                    MeProps meProps = new MeProps(getActivity().getApplication(), mStore);
                    meProps.connect(lifecycleOwner);
                    meView.setProps(meProps, mMeetingClient);
                } else {
                    if (peerList.get(i).getPeer1() != null) {
                        PeerProps p1 = new PeerProps(getActivity().getApplication(), mStore);
                        p1.connect(lifecycleOwner, peerList.get(i).getPeer1().getId());
                        peerView1.setProps(p1, mMeetingClient);
                        peerView1.setName(peerList.get(i).getPeer1().getDisplayName());
                    }
                }
                if (peerList.get(i).getPeer2() != null) {
                    PeerProps p2 = new PeerProps(getActivity().getApplication(), mStore);
                    p2.connect(lifecycleOwner, peerList.get(i).getPeer2().getId());
                    peerView2.setProps(p2, mMeetingClient);
                    peerView2.setName(peerList.get(i).getPeer2().getDisplayName());
                }
            } else if (i == 1) {
                if (peerList.get(i).getPeer1() != null) {
                    PeerProps p3 = new PeerProps(getActivity().getApplication(), mStore);
                    p3.connect(lifecycleOwner, peerList.get(i).getPeer1().getId());
                    peerView3.setProps(p3, mMeetingClient);
                    peerView3.setName(peerList.get(i).getPeer1().getDisplayName());
                }
                if (peerList.get(i).getPeer2() != null) {
                    PeerProps p4 = new PeerProps(getActivity().getApplication(), mStore);
                    p4.connect(lifecycleOwner, peerList.get(i).getPeer2().getId());
                    peerView4.setProps(p4, mMeetingClient);
                    peerView4.setName(peerList.get(i).getPeer2().getDisplayName());
                }
            } else if (i == 3) {
                if (peerList.get(i).getPeer1() != null) {
                    PeerProps p5 = new PeerProps(getActivity().getApplication(), mStore);
                    p5.connect(lifecycleOwner, peerList.get(i).getPeer1().getId());
                    peerView5.setProps(p5, mMeetingClient);
                    peerView5.setName(peerList.get(i).getPeer1().getDisplayName());
                }
                if (peerList.get(i).getPeer2() != null) {
                    PeerProps p6 = new PeerProps(getActivity().getApplication(), mStore);
                    p6.connect(lifecycleOwner, peerList.get(i).getPeer2().getId());
                    peerView6.setProps(p6, mMeetingClient);
                    peerView6.setName(peerList.get(i).getPeer2().getDisplayName());
                }
            }
        }
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
                if (translationEnabled) return (mDeviceHeight / 2) - (int) SP.valueOf(mContext, 150);
                return (mDeviceHeight / 2) - 40;
            case 3:
                if (translationEnabled) return (mDeviceHeight / 3) - (int) SP.valueOf(mContext, 100);
                return (mDeviceHeight / 3) - 40;
            default:
                if (translationEnabled) return mDeviceHeight - (int) SP.valueOf(mContext, 250);
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
     *
     * @param newCount
     * @return
     */
    private boolean shouldUpdateView(int newCount) {
        return peerList.size() != newCount;
    }

    /**
     * @param peerList
     */
    public void replaceList(final @NonNull List<ListGridPeer> peerList) {
        this.peerList = peerList;
        Log.v(TAG, "Reloading peers anyway ... for size >> " + peerList.size());
        notifyDataSetChanged();
        if (shouldUpdateView(peerList.size())) {
            updatePeers();
        }
    }

    public void notifyDataSetChanged() {
        updateDimensions();
        updateVisibility();
    }

    public void enableTranslation() {
        this.translationEnabled = true;
        if (loaded) {
            updateDimensions();
        }
    }

    public void disableTranslation() {
        this.translationEnabled = false;
        if (loaded) {
            updateDimensions();
        }
    }

} /** end class. */
