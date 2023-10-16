package com.woooapp.meeting.impl.views.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.views.MeView;
import com.woooapp.meeting.impl.views.PeerView;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.views.models.GridPeer;
import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.model.Peer;

import java.util.LinkedList;
import java.util.List;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 9:50 am 10/10/2023
 * <code>class</code> CustomPeerViewPage.java
 */
public class CustomPeerViewPage extends LinearLayout {

    private static final String TAG = CustomPeerViewPage.class.getSimpleName() + ".java";
    private Activity mActivity;
    private LifecycleOwner mLifecycleOwner;
    private RoomStore mStore;
    private MeetingClient mMeetingClient;
    private List<GridPeer> peerList = new LinkedList<>();

    private LayoutParams rowParams0;
    private LinearLayout row1;
    private LayoutParams rowParams1;
    private LinearLayout row2;
    private LayoutParams rowParams2;
    private LinearLayout row3;
    private LayoutParams rowParams3;
    private int mDeviceWidth;
    private int mDeviceHeight;
    private int mBottomBarHeight = 90;
    private MeView meView;
    private PeerView peerView0;
    private PeerView peerView1;
    private PeerView peerView2;
    private PeerView peerView3;
    private PeerView peerView4;
    private PeerView peerView5;
    private LayoutParams singleItemParams;
    private LayoutParams twoItemParams;

    public CustomPeerViewPage(Context context) {
        super(context);
        initComponents();
    }

    public CustomPeerViewPage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponents();
    }

    public CustomPeerViewPage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponents();
    }

    public CustomPeerViewPage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initComponents();
    }

    private void initComponents() {
        this.mDeviceWidth = Display.getDisplayWidth(getContext()) - 40;
        this.mDeviceHeight = Display.getDisplayHeight(getContext()) - mBottomBarHeight;

        LayoutParams mainParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mainParams.setMargins(0, 0, 0, 0);
        setLayoutParams(mainParams);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        setPadding(2, 2, 2, 2);

        rowParams0 = new LayoutParams(0, 0);
        rowParams1 = new LayoutParams(LayoutParams.MATCH_PARENT, getRowHeight(1));
        rowParams2 = new LayoutParams(LayoutParams.MATCH_PARENT, getRowHeight(2));
        rowParams3 = new LayoutParams(LayoutParams.MATCH_PARENT, getRowHeight(3));

        singleItemParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        singleItemParams.setMargins(0, 0, 0, 0);
        twoItemParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        twoItemParams.setMargins(0, 0, 0, 0);
    }

    public int getCount() {
        return peerList.size();
    }

    /**
     * @param position
     * @return
     */
    public int getItemViewType(int position) {
        return peerList.get(position).getViewType();
    }

    /**
     * @return
     */
    public int getViewTypeCount() {
        return 2;
    }

    /**
     *
     * @param position
     * @return
     */
    public GridPeer getItem(int position) {
        return peerList.get(position);
    }

    private void createRows(int rowCount) {
        Log.d(TAG, "Row Count >> " + rowCount);
        switch (rowCount) {
            case 2:
                if (row2 == null) {
                    row1.setLayoutParams(rowParams2);
                    row2 = new LinearLayout(getContext());
                    row2.setOrientation(LinearLayout.HORIZONTAL);
                    row2.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    addView(row2, rowParams2);
                }
                if (row3 != null) {
                    removeView(row3);
                    row3 = null;
                }
                break;
            case 3:
                if (row3 == null) {
                    row1.setLayoutParams(rowParams3);
                    row2.setLayoutParams(rowParams3);
                    row3 = new LinearLayout(getContext());
                    row3.setLayoutParams(rowParams3);
                    row3.setOrientation(LinearLayout.HORIZONTAL);
                    row3.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    addView(row3, rowParams3);
                }
                break;
            default:
                if (row1 == null) {
                    row1 = new LinearLayout(getContext());
                    row1.setLayoutParams(rowParams1);
                    row1.setOrientation(LinearLayout.HORIZONTAL);
                    row1.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    addView(row1, rowParams1);
                }
                if (row2 != null) {
//                    row2.setLayoutParams(rowParams0);
                    removeView(row2);
                    row2 = null;
                }
                if (row3 != null) {
//                    row3.setLayoutParams(rowParams0);
                    removeView(row3);
                    row3 = null;
                }
                break;
        }
    }

    public View createItem(int position) {
        if (getItemViewType(position) == GridPeer.VIEW_TYPE_ME) {
            if (meView == null) {
                meView = new MeView(getContext());
                meView.setLayoutParams(singleItemParams);
                meView.setPadding(0, 0, 0, 0);
            }
            return meView;
        }
        switch (position) {
            case 1:
                if (peerView1 == null) {
                    peerView1 = new PeerView(getContext());
                    peerView1.setLayoutParams(singleItemParams);
                    peerView1.setPadding(0, 0, 0, 0);
                }
                if (meView != null) {
                    meView.setLayoutParams(singleItemParams);
                }
                return peerView1;
            case 2:
                if (peerView2 == null) {
                    peerView2 = new PeerView(getContext());
                    peerView2.setLayoutParams(twoItemParams);
                    peerView2.setPadding(0, 0, 0, 0);
                }
                if (meView != null) {
                    meView.setLayoutParams(twoItemParams);
                }
                if (peerView1 != null) {
                    peerView1.setLayoutParams(singleItemParams);
                }
                return peerView2;
            case 3:
                if (peerView3 == null) {
                    peerView3 = new PeerView(getContext());
                    peerView3.setLayoutParams(twoItemParams);
                    peerView3.setPadding(0, 0, 0, 0);
                }
                if (peerView1 != null) {
                    peerView1.setLayoutParams(twoItemParams);
                }
                return peerView3;
            case 4:
                if (peerView4 == null) {
                    peerView4 = new PeerView(getContext());
                    peerView4.setLayoutParams(singleItemParams);
                    peerView4.setPadding(0, 0, 0, 0);
                }
                return peerView4;
            case 5:
                if (peerView5 == null) {
                    peerView5 = new PeerView(getContext());
                    peerView5.setLayoutParams(twoItemParams);
                    peerView5.setPadding(0, 0, 0, 0);
                }
                if (peerView4 != null) {
                    peerView4.setLayoutParams(twoItemParams);
                }
                return peerView5;
        }
        return null;
    }

    public View getPage() {
        update();
        this.invalidate();
        return this;
    }

    private void update() {
        switch (getCount()) {
            case 2:
            case 3:
            case 4:
                createRows(2);
                break;
            case 5:
            case 6:
                createRows(3);
                break;
            default:
                createRows(1);
                break;
        }

        for (int i = 0; i < peerList.size(); i++) {
            View item = createItem(i);
            if (i == 0) {
                if (meView == null) {
                    meView = (MeView) item;
                    row1.addView(meView, singleItemParams);
                    WooAnimationUtil.showView(meView, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            MeProps meProps = new MeProps(((AppCompatActivity) getContext()).getApplication(), mStore);
                            meProps.connect(mLifecycleOwner);
                            meView.setProps(meProps, mMeetingClient);
                        }
                    });
                }
                if (peerView5 != null) {
                    removePeer(peerView5, row3, 5);
                }
                if (peerView4 != null) {
                    removePeer(peerView4, row3, 4);
                }
                if (peerView3 != null) {
                    removePeer(peerView3, row2, 3);
                }
                if (peerView2 != null) {
                    removePeer(peerView2, row1, 2);
                }
                if (peerView1 != null) {
                    removePeer(peerView1, row2, 1);
                }
            } else if (i == 1) {
                if (peerView1 == null) {
                    peerView1 = (PeerView) item;
                    row2.addView(peerView1, singleItemParams);
                    int finalI4 = i;
                    WooAnimationUtil.showView(peerView1, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            Peer p1 = getItem(finalI4).getPeer();
                            PeerProps props1 = new PeerProps(((AppCompatActivity) getContext()).getApplication(), mStore);
                            props1.connect(mLifecycleOwner, p1.getId());
                            peerView1.setProps(props1, mMeetingClient);
                            peerView1.setName(p1.getDisplayName());
                        }
                    });
                }
                if (peerView5 != null) {
                    removePeer(peerView5, row3, 5);
                }
                if (peerView4 != null) {
                    removePeer(peerView4, row3, 4);
                }
                if (peerView3 != null) {
                    removePeer(peerView3, row2, 3);
                }
                if (peerView2 != null) {
                    removePeer(peerView2, row1, 2);
                }
            } else if (i == 2) {
                if (peerView2 == null) {
                    peerView2 = (PeerView) item;
                    row1.addView(peerView2, twoItemParams);
                    int finalI3 = i;
                    WooAnimationUtil.showView(peerView2, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            Peer p2 = getItem(finalI3).getPeer();
                            PeerProps props2 = new PeerProps(((AppCompatActivity) getContext()).getApplication(), mStore);
                            props2.connect(mLifecycleOwner, p2.getId());
                            peerView2.setProps(props2, mMeetingClient);
                            peerView2.setName(p2.getDisplayName());
                        }
                    });
                }
                if (peerView5 != null) {
                    removePeer(peerView5, row3, 5);
                }
                if (peerView4 != null) {
                    removePeer(peerView4, row3, 4);
                }
                if (peerView3 != null) {
                    removePeer(peerView3, row2, 3);
                }
            } else if (i == 3) {
                if (peerView3 == null) {
                    peerView3 = (PeerView) item;
                    row2.addView(peerView3, twoItemParams);
                    int finalI1 = i;
                    WooAnimationUtil.showView(peerView3, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            Peer p3 = getItem(finalI1).getPeer();
                            PeerProps props3 = new PeerProps(((AppCompatActivity) getContext()).getApplication(), mStore);
                            props3.connect(mLifecycleOwner, p3.getId());
                            peerView3.setProps(props3, mMeetingClient);
                            peerView3.setName(p3.getDisplayName());
                        }
                    });
                }
                if (peerView5 != null) {
                    removePeer(peerView5, row3, 5);
                }
                if (peerView4 != null) {
                    removePeer(peerView4, row3, 4);
                }
            } else if (i == 4) {
                if (peerView4 == null) {
                    peerView4 = (PeerView) item;
                    row3.addView(peerView4, singleItemParams);
                    int finalI = i;
                    WooAnimationUtil.showView(peerView4, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            Peer p4 = getItem(finalI).getPeer();
                            PeerProps props4 = new PeerProps(((AppCompatActivity) getContext()).getApplication(), mStore);
                            props4.connect(mLifecycleOwner, p4.getId());
                            peerView4.setProps(props4, mMeetingClient);
                            peerView4.setName(p4.getDisplayName());
                        }
                    });
                }
                if (peerView5 != null) {
                    removePeer(peerView5, row3, 5);
                }
            } else if (i == 5) {
                if (peerView5 == null) {
                    peerView5 = (PeerView) item;
                    row3.addView(peerView5, twoItemParams);
                    int finalI2 = i;
                    WooAnimationUtil.showView(peerView5, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            Peer p5 = getItem(finalI2).getPeer();
                            PeerProps props5 = new PeerProps(((AppCompatActivity) getContext()).getApplication(), mStore);
                            props5.connect(mLifecycleOwner, p5.getId());
                            peerView5.setProps(props5, mMeetingClient);
                            peerView5.setName(p5.getDisplayName());
                        }
                    });
                }
            }
        }
        if (row1 != null) row1.invalidate();
        if (row2 != null) row2.invalidate();
        if (row3 != null) row3.invalidate();
    }

    /**
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
     * @param activity
     */
    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * @param lifecycleOwner
     */
    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.mLifecycleOwner = lifecycleOwner;
    }

    /**
     * @param store
     */
    public void setRoomStore(RoomStore store) {
        this.mStore = store;
    }

    /**
     * @param meetingClient
     */
    public void setMeetingClient(MeetingClient meetingClient) {
        this.mMeetingClient = meetingClient;
    }

    /**
     * @param bottomBarHeight
     */
    public void setBottomBarHeight(int bottomBarHeight) {
        this.mBottomBarHeight = bottomBarHeight;
    }

    /**
     * @param peerList
     * @return
     */
    public boolean updateRequired(List<GridPeer> peerList) {
        return getCount() != peerList.size();
    }

    /**
     * @param peerList
     */
    public void setPeerList(List<GridPeer> peerList) {
        if (updateRequired(peerList)) {
            Log.d(TAG, "Update required. Updating Views ...");
            this.peerList = peerList;
            this.notifyDataSetChanged();
        } else {
            Log.d(TAG, "Update not required. Skipping view update ...");
        }
    }

    public void notifyDataSetChanged() {
        update();
    }

    /**
     *
     * @param peerView
     * @param row
     * @param peerNum
     */
    private void removePeer(@NonNull final PeerView peerView, @NonNull final LinearLayout row, int peerNum) {
       WooAnimationUtil.hideView(peerView, new AnimatorListenerAdapter() {
           @Override
           public void onAnimationEnd(Animator animation) {
               super.onAnimationEnd(animation);
               row.removeView(peerView);
               peerView.dispose();
               switch (peerNum) {
                   case 1:
                       peerView1 = null;
                       break;
                   case 2:
                       peerView2 = null;
                       break;
                   case 3:
                       peerView3 = null;
                       break;
                   case 4:
                       peerView4 = null;
                       break;
                   case 5:
                       peerView5 = null;
                       break;
               }
           }
       });
    }

    public void dispose() {
        if (meView != null) {
            meView.dispose();
        }
    }

} /**
 * end class.
 */
