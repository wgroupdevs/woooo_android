package com.woooapp.meeting.impl.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.PeerConnectionUtils;
import com.woooapp.meeting.lib.RoomClient;

import java.util.Objects;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.ViewPeerBinding;
import eu.siacs.conversations.databinding.WooViewPeerBinding;

/**
 * @author Muneeb Ahmad
 * <p>
 * File PeerView.java
 * Class [PeerView]
 * on 08/09/2023 at 8:01 pm
 */
public class PeerView extends RelativeLayout {

    public PeerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PeerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PeerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PeerView(
            @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    WooViewPeerBinding mBinding;

    private void init(Context context) {
        mBinding =
                DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.woo_view_peer, this, true);
        mBinding.wooPeerView.wooVideoRenderer.init(PeerConnectionUtils.getEglContext(), null);
    }

    public void setProps(PeerProps props, MeetingClient meetingClient) {
        // set view model into included layout
        mBinding.wooPeerView.setWooPeerViewProps(props);
//        mBinding.tvPeerName.setText(Objects.requireNonNull(props.getPeer().get()).getDisplayName());

//        // register click listener.
//        mBinding.peerView.info.setOnClickListener(
//                view -> {
//                    Boolean showInfo = props.getShowInfo().get();
//                    props.getShowInfo().set(showInfo != null && showInfo ? Boolean.FALSE : Boolean.TRUE);
//                });
//
//        mBinding.peerView.stats.setOnClickListener(
//                view -> {
//                    // TODO Handle inner click event;
//                });

        // set view model
        mBinding.setWooPeerProps(props);
    }

    public void dispose() {
        mBinding.setWooPeerProps(null);
        mBinding.wooPeerView.setWooPeerViewProps(null);
        mBinding.wooPeerView.wooVideoRenderer.release();
    }

} /**
 * end class [PeerView]
 */
