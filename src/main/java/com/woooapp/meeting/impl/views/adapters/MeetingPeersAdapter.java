package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.PagerAdapter;

import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.views.models.MeetingPage;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.lv.RoomStore;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eu.siacs.conversations.R;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 3:05 pm 04/10/2023
 * <code>class</code> MeetingPeersAdapter.java
 */
public final class MeetingPeersAdapter extends PagerAdapter {

    private static final String TAG = MeetingPeersAdapter.class.getSimpleName() + ".java";
    private final Context mContext;
    private final LifecycleOwner mLifecycleOwner;
    private final RoomStore mStore;
    private final MeetingClient mMeetingClient;
    private List<MeetingPage> mPages = new LinkedList<>();
    private ViewGroup mContainer;
    private final int mDeviceWidth;
    private final int mDeviceHeight;
    private int mBottomBarHeight = 90;
//    private View contentView1;
//    private PeerAdapter2 peerAdapter2;
    private Map<Integer, PeerAdapter2> peerAdapters = new LinkedHashMap<>();

    /**
     * @param mContext
     * @param lifecycleOwner
     * @param mStore
     * @param mMeetingClient
     */
    public MeetingPeersAdapter(
            @NonNull Context mContext,
            @NonNull LifecycleOwner lifecycleOwner,
            @NonNull RoomStore mStore,
            @NonNull MeetingClient mMeetingClient) {
        this.mContext = mContext;
        this.mLifecycleOwner = lifecycleOwner;
        this.mStore = mStore;
        this.mMeetingClient = mMeetingClient;

        this.mDeviceWidth = Display.getDisplayWidth(mContext) - 40;
        this.mDeviceHeight = Display.getDisplayHeight(mContext) - mBottomBarHeight;

        peerAdapters.clear();
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.v(TAG, "instantiateItem(" + position + ")");
        View contentView1 = LayoutInflater.from(mContext).inflate(R.layout.custom_peer_view_page, null);
        ListView peersListView = contentView1.findViewById(R.id.peersListView);
        peersListView.setEnabled(false);
        PeerAdapter2 peerAdapter2 = new PeerAdapter2(mContext, mPages.get(position).getPageNo(), mLifecycleOwner, mStore, mMeetingClient);
        peerAdapter2.setViewPagerHeight(mBottomBarHeight);
        peersListView.setAdapter(peerAdapter2);
        peerAdapters.put(mPages.get(position).getPageNo(), peerAdapter2);
        container.addView(contentView1);
        return contentView1;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_NONE;
//    }

    /**
     *
     * @param bottomBarHeight
     */
    public void setViewPagerHeight(int bottomBarHeight) {
        this.mBottomBarHeight = bottomBarHeight;
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

    @Override
    public void notifyDataSetChanged() {
//        if (mContainer != null) {
//            View contentView = mContainer.findViewWithTag(TAG);
//            int position = 0;
//            if (contentView != null) {
//                for (int i = 0; i < mContainer.getChildCount(); i++) {
//                    if (mContainer.getChildAt(i).getTag().equals(TAG)) {
//                        position = i;
//                        break;
//                    }
//                }
//                Log.d(TAG, "<< Invalidating the ViewPager's  view for position [" + position +"] >>");
//                update(contentView, position);
//            }
//        }
        super.notifyDataSetChanged();
        Log.v(TAG, "<< PEER ADAPTER SIZE >>>> " + peerAdapters.size());
        Log.v(TAG, "<< COUNT >>>> " + getCount());
//        if (mContainer != null) {
//            if (peerAdapters.size() == getCount()) {
//                Log.v(TAG, "<< UPDATING ADAPTERS >>");
//                for (int i = 0; i < getCount(); i++) {
//                    PeerAdapter2 adapter2;
//                    if (peerAdapters.size() == 0) {
//                        adapter2 = peerAdapters.get(0);
//                    } else {
//                        adapter2 = peerAdapters.get(mPages.get(i - 1).getPageNo());
//                    }
//                    if (adapter2 != null) {
//                        adapter2.replaceList(mPages.get(i).getmPeers());
//                    } else {
//                        Log.w(TAG, "<< Adapter 2 IS NULL >>");
//                    }
//                }
//            } else {
//                Log.w(TAG, "<< FAILED TO UPDATE ADAPTERS >>");
//            }
//        } else {
//            Log.w(TAG, "<< CONTAINER IS NULL >>>");
//        }
    }

    /**
     * @param pages
     */
    public void replacePages(@NonNull List<MeetingPage> pages) {
        this.mPages = pages;
//        if (peerAdapter2 != null) {
//            Log.d(TAG, "Replacing Peers >> ");
//            peerAdapter2.replaceList(mPages.get(0).getmPeers());
//        } else {
//            Log.d(TAG, "PeerAdapter2 is NULL >>>");
//        }
        notifyDataSetChanged();
    }

    public void dispose() {
        this.peerAdapters.clear();
    }

} /**
 * end class.
 */
