package com.woooapp.meeting.impl.views.adapters;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.views.MeView;
import com.woooapp.meeting.impl.views.PeerView;
import com.woooapp.meeting.impl.views.models.GridPeer;
import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.lv.RoomStore;

import java.util.LinkedList;
import java.util.List;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 3:16 pm 10/10/2023
 * <code>class</code> PeerTableViewPage.java
 */
public class PeerTableViewPage extends TableLayout {

    private static final String TAG = PeerTableViewPage.class.getSimpleName() + ".java";
    private final LifecycleOwner mLifecycleOwner;
    private final RoomStore mStore;
    private final MeetingClient mClient;
    private List<GridPeer> peerList = new LinkedList<>();
    private int mDeviceWidth;
    private int mDeviceHeight;
    private int mBottomBarHeight = 90;
    private TableRow row1;
    private TableRow row2;
    private TableRow row3;
    private MeView meView;
    private PeerView peerView0;
    private PeerView peerView1;
    private PeerView peerView2;
    private PeerView peerView3;
    private PeerView peerView4;
    private PeerView peerView5;
    private LayoutParams singleRowParams;
    private LayoutParams twoRowParams;
    private LayoutParams threeRowParams;
    private TableRow.LayoutParams singleItemParams;
    private TableRow.LayoutParams twoItemParams;

    /**
     *
     * @param context
     */
    public PeerTableViewPage(Context context,
                             @NonNull LifecycleOwner lifecycleOwner,
                             @NonNull RoomStore roomStore,
                             @NonNull MeetingClient meetingClient) {
        super(context);
        this.mLifecycleOwner = lifecycleOwner;
        this.mStore = roomStore;
        this.mClient = meetingClient;

        this.initComponents();
    }

    private void initComponents() {
        this.mDeviceWidth = Display.getDisplayWidth(getContext()) - 40;
        this.mDeviceHeight = Display.getDisplayHeight(getContext()) - mBottomBarHeight;

        this.singleRowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.twoRowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.threeRowParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        this.singleItemParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        this.twoItemParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        reload();
    }

    private void reload() {
        this.mDeviceWidth = Display.getDisplayWidth(getContext()) - 40;
        this.mDeviceHeight = Display.getDisplayHeight(getContext()) - mBottomBarHeight;

        if (peerList.size() == 1) {
            if (row1 == null) {
                row1 = new TableRow(getContext());
                row1.setPadding(0, 0, 0, 0);
                addView(row1, singleRowParams);
            }
            if (meView == null) {
                meView = new MeView(getContext());
                MeProps meProps = new MeProps(((AppCompatActivity) getContext()).getApplication(), mStore);
                meProps.connect(mLifecycleOwner);
                meView.setProps(meProps, mClient);
                row1.addView(meView, singleItemParams);
            }
        }
    }

    /**
     *
     * @param rowCount
     * @return
     */
    private int getRowHeight(int rowCount) {
        switch (rowCount) {
            case 2:
                return (mDeviceHeight / 2) - 50;
            case 3:
                return mDeviceHeight / 3 - 50;
            default:
                return mDeviceHeight - 50;
        }
    }

    /**
     *
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
     * @param peerList
     * @return
     */
    private boolean shouldReload(List<GridPeer> peerList) {
        return this.peerList.size() == peerList.size();
    }

    /**
     *
     * @param peerList
     */
    public void replaceList(@NonNull List<GridPeer> peerList) {
        if (shouldReload(peerList)) {
            Log.v(TAG, "Reload is required! Reloading view ...");
            this.peerList = peerList;
            this.reload();
        }
    }

} /** end class. */
