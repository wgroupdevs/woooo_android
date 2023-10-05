package com.woooapp.meeting.impl.views.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.PagerAdapter;

import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.views.MeView;
import com.woooapp.meeting.impl.views.PeerView;
import com.woooapp.meeting.impl.views.models.GridPeer;
import com.woooapp.meeting.impl.views.models.MeetingPage;
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

    /**
     * @param mContext
     * @param mLifecycleOwner
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
        this.mDeviceHeight = Display.getDisplayHeight(mContext) - 90;
    }

    @Override
    public int getCount() {
        Log.d(TAG, "PAGES SIZE >> " + mPages.size());
        return mPages.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d(TAG, "Instantiate Item");
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_meeting_peers_view, null);
        contentView.setTag(TAG);
        update(contentView, position);
        container.addView(contentView);
        this.mContainer = container;
        return contentView;
    }

    /**
     *
     * @param contentView
     * @param position
     */
    private void update(@NonNull View contentView, int position) {
        LinearLayout row1 = contentView.findViewById(R.id.layoutPeerRow1);
        row1.setWeightSum(3);
        LinearLayout row2 = contentView.findViewById(R.id.layoutPeerRow2);
        row2.setWeightSum(3);
        LinearLayout row3 = contentView.findViewById(R.id.layoutPeerRow3);
        row3.setWeightSum(3);

        MeView meView = contentView.findViewById(R.id.meNewView);
        PeerView peer1 = contentView.findViewById(R.id.peerView1);
        PeerView peer2 = contentView.findViewById(R.id.peerView2);
        PeerView peer3 = contentView.findViewById(R.id.peerView3);
        PeerView peer4 = contentView.findViewById(R.id.peerView4);
        PeerView peer5 = contentView.findViewById(R.id.peerView5);

        List<GridPeer> peers = mPages.get(position).getmPeers();
        if (peers.size() == 1) {
            if (row1.getLayoutParams() != null) {
                row1.getLayoutParams().height = getRowHeight(1);
            }
            row2.setVisibility(View.GONE);
            row3.setVisibility(View.GONE);

            if (meView.getLayoutParams() != null) {
                meView.getLayoutParams().width = getColumnWidth(1);
            }
            peer1.setVisibility(View.GONE);
            peer2.setVisibility(View.GONE);
            peer3.setVisibility(View.GONE);
            peer4.setVisibility(View.GONE);
            peer5.setVisibility(View.GONE);
        } else if (peers.size() > 1 && peers.size() < 5) {
            if (row1.getLayoutParams() != null) {
                row1.getLayoutParams().height = getRowHeight(2);
            }
            row2.setVisibility(View.VISIBLE);
            if (row2.getLayoutParams() != null) {
                row2.getLayoutParams().height = getRowHeight(2);
            }
            row3.setVisibility(View.GONE);

            if (peers.size() == 2) {
                peer1.setVisibility(View.VISIBLE);
                if (meView.getLayoutParams() != null) {
                    meView.getLayoutParams().width = getColumnWidth(1);
                }
                if (peer1.getLayoutParams() != null) {
                    peer1.getLayoutParams().width = getColumnWidth(1);
                }
                peer2.setVisibility(View.GONE);
                peer3.setVisibility(View.GONE);
                peer4.setVisibility(View.GONE);
                peer5.setVisibility(View.GONE);
            } else if (peers.size() == 3) {
                if (meView.getLayoutParams() != null) {
                    meView.getLayoutParams().width = getColumnWidth(2);
                }
                peer1.setVisibility(View.VISIBLE);
                if (peer1.getLayoutParams() != null) {
                    peer1.getLayoutParams().width = getColumnWidth(1);
                }
                peer2.setVisibility(View.VISIBLE);
                if (peer2.getLayoutParams() != null) {
                    peer2.getLayoutParams().width = getColumnWidth(2);
                }
                peer3.setVisibility(View.GONE);
                peer4.setVisibility(View.GONE);
                peer5.setVisibility(View.GONE);
            } else if (peers.size() == 4) {
                peer1.setVisibility(View.VISIBLE);
                if (peer1.getLayoutParams() != null) {
                    peer1.getLayoutParams().width = getColumnWidth(2);
                }
                peer2.setVisibility(View.VISIBLE);
                if (peer2.getLayoutParams() != null) {
                    peer2.getLayoutParams().width = getColumnWidth(2);
                }
                peer3.setVisibility(View.VISIBLE);
                if (peer3.getLayoutParams() != null) {
                    peer3.getLayoutParams().width = getColumnWidth(2);
                }
                peer4.setVisibility(View.GONE);
                peer5.setVisibility(View.GONE);
            }
        } else if (peers.size() > 4 && peers.size() < 7) {
            if (row1.getLayoutParams() != null) {
                row1.getLayoutParams().height = getRowHeight(3);
            }
            row2.setVisibility(View.VISIBLE);
            if (row2.getLayoutParams() != null) {
                row2.getLayoutParams().height = getRowHeight(3);
            }
            row3.setVisibility(View.VISIBLE);
            if (row3.getLayoutParams() != null) {
                row3.getLayoutParams().height = getRowHeight(3);
            }

            if (peers.size() == 5) {
                peer1.setVisibility(View.VISIBLE);
                if (peer1.getLayoutParams() != null) {
                    peer1.getLayoutParams().width = getColumnWidth(2);
                }
                peer2.setVisibility(View.VISIBLE);
                if (peer2.getLayoutParams() != null) {
                    peer2.getLayoutParams().width = getColumnWidth(2);
                }
                peer3.setVisibility(View.VISIBLE);
                if (peer3.getLayoutParams() != null) {
                    peer3.getLayoutParams().width = getColumnWidth(2);
                }
                peer4.setVisibility(View.VISIBLE);
                if (peer4.getLayoutParams() != null) {
                    peer4.getLayoutParams().width = getColumnWidth(1);
                }
                peer5.setVisibility(View.GONE);
            } else if (peers.size() == 6) {
                peer1.setVisibility(View.VISIBLE);
                if (peer1.getLayoutParams() != null) {
                    peer1.getLayoutParams().width = getColumnWidth(2);
                }
                peer2.setVisibility(View.VISIBLE);
                if (peer2.getLayoutParams() != null) {
                    peer2.getLayoutParams().width = getColumnWidth(2);
                }
                peer3.setVisibility(View.VISIBLE);
                if (peer3.getLayoutParams() != null) {
                    peer3.getLayoutParams().width = getColumnWidth(2);
                }
                peer4.setVisibility(View.VISIBLE);
                if (peer4.getLayoutParams() != null) {
                    peer4.getLayoutParams().width = getColumnWidth(2);
                }
                peer5.setVisibility(View.VISIBLE);
                if (peer5.getLayoutParams() != null) {
                    peer5.getLayoutParams().width = getColumnWidth(2);
                }
            }
        }

        for (int i = 0; i < peers.size(); i++) {
            GridPeer peer = peers.get(i);
            if (peer.getViewType() == GridPeer.VIEW_TYPE_ME) {
                MeProps meProps = new MeProps(((AppCompatActivity) mContext).getApplication(), mStore);
                meProps.connect(mLifecycleOwner);
                meView.setProps(meProps, mMeetingClient);
                Log.d(TAG, "<< Added Me Props at index [" + i + "]");
            } else if (peer.getViewType() == GridPeer.VIEW_TYPE_PEER) {
                Peer p = peer.getPeer();
                PeerProps props = new PeerProps(((AppCompatActivity) mContext).getApplication(), mStore);
                props.connect(mLifecycleOwner, p.getId());
                if (i == 1) {
                    peer1.setProps(props, mMeetingClient);
                } else if (i == 2) {
                    peer2.setProps(props, mMeetingClient);
                } else if (i == 3) {
                    peer3.setProps(props, mMeetingClient);
                } else if (i == 4) {
                    peer4.setProps(props, mMeetingClient);
                } else if (i == 5) {
                    peer5.setProps(props, mMeetingClient);
                }
                Log.d(TAG, "<< Added Peer Props at index [" + i + "]");
            }
        }

        row1.invalidate();
        row2.invalidate();
        row3.invalidate();
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
     * @param rowCount
     * @return
     */
    private int getRowHeight(int rowCount) {
        switch (rowCount) {
            case 2:
                return mDeviceHeight / 2;
            case 3:
                return mDeviceHeight / 3;
            default:
                return mDeviceHeight;
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
        super.notifyDataSetChanged();
        if (mContainer != null) {
            View contentView = mContainer.findViewWithTag(TAG);
            int position = 0;
            if (contentView != null) {
                for (int i = 0; i < mContainer.getChildCount(); i++) {
                    if (mContainer.getChildAt(i).getTag().equals(TAG)) {
                        position = i;
                        break;
                    }
                }
                Log.d(TAG, "<< Invalidating the ViewPager's  view for position [" + position +"] >>");
                update(contentView, position);
            }
        }
    }

    /**
     * @param pages
     */
    public void replacePages(@NonNull List<MeetingPage> pages) {
        this.mPages = pages;
        notifyDataSetChanged();
    }

} /**
 * end class.
 */
