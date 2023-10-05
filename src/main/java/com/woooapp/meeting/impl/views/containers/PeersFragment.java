package com.woooapp.meeting.impl.views.containers;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.impl.views.MeView;
import com.woooapp.meeting.impl.views.PeerView;
import com.woooapp.meeting.impl.views.models.GridPeer;
import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.model.Peer;

import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;
import pk.muneebahmad.lib.views.containers.NavFragment;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 10:53 am 05/10/2023
 * <code>class</code> PeersFragment.java
 */
public class PeersFragment extends NavFragment {

    private static final String TAG = PeersFragment.class.getSimpleName() + ".java";
    private final LifecycleOwner mLifecycleOwner;
    private final RoomStore mStore;
    private final MeetingClient mMeetingClient;
    private List<GridPeer> mPeers = new LinkedList<>();
    private final LinearLayout.LayoutParams rowSingleItemParams;
    private final LinearLayout.LayoutParams rowTwoItemsParams;
    private final LinearLayout.LayoutParams onePeerParams;
    private final LinearLayout.LayoutParams twoPeersParams;
    private final LinearLayout.LayoutParams fivePeersParams;
    private final LinearLayout.LayoutParams zeroWeightParams;
    private View contentView;
    private LinearLayout row1;
    private LinearLayout row2;
    private LinearLayout row3;
    private MeView meView;
    private PeerView peer1;
    private PeerView peer2;
    private PeerView peer3;
    private PeerView peer4;
    private PeerView peer5;

    /**
     * @param activity
     * @param lifecycleOwner
     * @param roomStore
     * @param meetingClient
     */
    public PeersFragment(@NonNull Activity activity,
                         @NonNull LifecycleOwner lifecycleOwner,
                         @NonNull RoomStore roomStore,
                         @NonNull MeetingClient meetingClient) {
        super(activity);
        this.mLifecycleOwner = lifecycleOwner;
        this.mStore = roomStore;
        this.mMeetingClient = meetingClient;

        this.rowSingleItemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2);
        this.rowTwoItemsParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

        this.onePeerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 3);
        this.twoPeersParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2);
        this.fivePeersParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

        this.zeroWeightParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 0);
    }

    @Override
    public View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup root) {
        return inflater.inflate(R.layout.layout_meeting_peers_view, null);
    }

    @Override
    public <T extends View> void viewCreated(@NonNull T view) {
        this.contentView = view;

        row1 = contentView.findViewById(R.id.layoutPeerRow1);
        row2 = contentView.findViewById(R.id.layoutPeerRow2);
        row3 = contentView.findViewById(R.id.layoutPeerRow3);

        meView = contentView.findViewById(R.id.meNewView);
        peer1 = contentView.findViewById(R.id.peerView1);
        peer2 = contentView.findViewById(R.id.peerView2);
        peer3 = contentView.findViewById(R.id.peerView3);
        peer4 = contentView.findViewById(R.id.peerView4);
        peer5 = contentView.findViewById(R.id.peerView5);

        this.update();
    }

    private void update() {
        if (mPeers.size() == 1) {
            row2.setVisibility(View.GONE);
            row3.setVisibility(View.GONE);

            peer1.setVisibility(View.GONE);
            peer2.setVisibility(View.GONE);
            peer3.setVisibility(View.GONE);
            peer4.setVisibility(View.GONE);
            peer5.setVisibility(View.GONE);
        } else if (mPeers.size() > 1 && mPeers.size() < 5) {
            row2.setVisibility(View.VISIBLE);
            row3.setVisibility(View.GONE);
            // TODO Row Items
            if (mPeers.size() == 2) {
                peer1.setVisibility(View.VISIBLE);
                peer2.setVisibility(View.GONE);
                peer3.setVisibility(View.GONE);
                peer4.setVisibility(View.GONE);
                peer5.setVisibility(View.GONE);
            } else if (mPeers.size() == 3) {
                peer1.setVisibility(View.VISIBLE);
                peer2.setVisibility(View.VISIBLE);
                peer3.setVisibility(View.GONE);
                peer4.setVisibility(View.GONE);
                peer5.setVisibility(View.GONE);
            } else if (mPeers.size() == 4) {
                peer1.setVisibility(View.VISIBLE);
                peer2.setVisibility(View.VISIBLE);
                peer3.setVisibility(View.VISIBLE);
                peer4.setVisibility(View.GONE);
                peer5.setVisibility(View.GONE);
            }
        } else if (mPeers.size() > 4 && mPeers.size() < 7) {
            row2.setVisibility(View.VISIBLE);
            row3.setVisibility(View.VISIBLE);
            // TODO Row Items
            if (mPeers.size() == 5) {
                peer1.setVisibility(View.VISIBLE);
                peer2.setVisibility(View.VISIBLE);
                peer3.setVisibility(View.VISIBLE);
                peer4.setVisibility(View.VISIBLE);
                peer5.setVisibility(View.GONE);
            } else if (mPeers.size() == 6) {
                peer1.setVisibility(View.VISIBLE);
                peer2.setVisibility(View.VISIBLE);
                peer3.setVisibility(View.VISIBLE);
                peer4.setVisibility(View.VISIBLE);
                peer5.setVisibility(View.VISIBLE);
            }
        }

        for (int i = 0; i < mPeers.size(); i++) {
            GridPeer peer = mPeers.get(i);
            if (peer.getViewType() == GridPeer.VIEW_TYPE_ME) {
                MeProps meProps = new MeProps(getActivity().getApplication(), mStore);
                meProps.connect(mLifecycleOwner);
                meView.setProps(meProps, mMeetingClient);
                Log.d(TAG, "<< Added Me Props at index [" + i + "]");
            } else if (peer.getViewType() == GridPeer.VIEW_TYPE_PEER) {
                Peer p = peer.getPeer();
                PeerProps props = new PeerProps(getActivity().getApplication(), mStore);
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
    }

    public void replacePeers(@NonNull List<GridPeer> peers) {
        this.mPeers = peers;
        this.update();
    }

} /**
 * end class.
 */
