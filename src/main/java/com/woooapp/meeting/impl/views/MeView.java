package com.woooapp.meeting.impl.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.PeerConnectionUtils;
import com.woooapp.meeting.lib.RoomClient;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.WooViewMeBinding;

/**
 * @author Muneeb Ahmad
 * <p>
 * File MeView.java
 * Class [MeView]
 * on 08/09/2023 at 7:50 pm
 */
public class MeView extends RelativeLayout {

    public MeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MeView(
            @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    WooViewMeBinding mBinding;

    private void init(Context context) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.woo_view_me, this, true);
        mBinding.wooPeerView.wooVideoRenderer.init(PeerConnectionUtils.getEglContext(), null);
    }

    public void setProps(MeProps props, final MeetingClient roomClient) {

        // set view model.
        mBinding.wooPeerView.setWooPeerViewProps(props);

        // register click listener.
//        mBinding.peerView.info.setOnClickListener(
//                view -> {
//                    Boolean showInfo = props.getShowInfo().get();
//                    props.getShowInfo().set(showInfo != null && showInfo ? Boolean.FALSE : Boolean.TRUE);
//                });
//
//        mBinding.peerView.meDisplayName.setOnEditorActionListener(
//                (textView, actionId, keyEvent) -> {
//                    if (actionId == EditorInfo.IME_ACTION_DONE) {
//                        roomClient.changeDisplayName(textView.getText().toString().trim());
//                        return true;
//                    }
//                    return false;
//                });
//        mBinding.peerView.stats.setOnClickListener(
//                view -> {
//                    // TODO Handle inner click event;
//                });

        mBinding.wooPeerView.wooVideoRenderer.setZOrderMediaOverlay(true);

        // set view model.
        mBinding.setWooMeProps(props);

        // register click listener.
//        mBinding.mic.setOnClickListener(
//                view -> {
//                    if (MeProps.DeviceState.ON.equals(props.getMicState().get())) {
//                        roomClient.muteMic();
//                    } else {
//                        roomClient.unmuteMic();
//                    }
//                });
//        mBinding.cam.setOnClickListener(
//                view -> {
//                    if (MeProps.DeviceState.ON.equals(props.getCamState().get())) {
//                        roomClient.disableCam();
//                    } else {
//                        roomClient.enableCam();
//                    }
//                });
//        mBinding.changeCam.setOnClickListener(view -> roomClient.changeCam());
//        mBinding.share.setOnClickListener(
//                view -> {
//                    if (MeProps.DeviceState.ON.equals(props.getShareState().get())) {
//                        roomClient.disableShare();
//                    } else {
//                        roomClient.enableShare();
//                    }
//                });
    }

    public void dispose() {
        mBinding.setWooMeProps(null);
        mBinding.wooPeerView.wooVideoRenderer.release();
    }

} /**
 * end class [MeView]
 */
