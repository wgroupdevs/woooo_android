package com.woooapp.meeting.impl.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.PeerConnectionUtils;
import com.woooapp.meeting.lib.RoomClient;
import com.woooapp.meeting.lib.model.Info;
import com.woooapp.meeting.lib.model.Peer;

import org.webrtc.MediaStreamTrack;

import java.util.Objects;
import java.util.Random;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.WooViewPeerBinding;

/**
 * @author Muneeb Ahmad
 * <p>
 * File PeerView.java
 * Class [PeerView]
 * on 08/09/2023 at 8:01 pm
 */
public class PeerView extends RelativeLayout {

    private static final String TAG = PeerView.class.getSimpleName() + ".java";
    private boolean handRaised = false;
    private PeerProps props;

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

    /**
     *
     * @param name
     */
    public void setName(@Nullable String name) {
        if (name != null) {
            mBinding.tvPeerName.setText(name);
        }
    }

    public void setCameraState(boolean on) {
        if (on) {
            mBinding.peerInfoCam.setImageResource(R.drawable.ic_video_camera_white);
        } else {
            mBinding.peerInfoCam.setImageResource(R.drawable.ic_camera_off_gray);
        }
    }

    public void setMicState(boolean on) {
        if (on) {
            mBinding.peerInfoMic.setImageResource(R.drawable.ic_mic_white_48dp);
            playAudioEffects();
        } else {
            mBinding.peerInfoMic.setImageResource(R.drawable.ic_mic_off_gray);
            stopAudioEffects();
        }
    }

    public void setProps(PeerProps props, MeetingClient meetingClient) {
        this.props = props;
        // set view model into included layout
        mBinding.wooPeerView.setWooPeerViewProps(props);

        if (props.getPeer() != null) {
            Peer p = (Peer) props.getPeer().get();
            if (p != null) {
                setMicState(p.isMicOn());
                setCameraState(p.isCamOn());
                setHandRaisedState(p.isHandRaised());
            }
        }

//        if (props.getPeer() != null) {
//            Peer p = (Peer) props.getPeer().get();
//            if (p != null) {
//                setHandRaisedState(p.isHandRaised());
//                setCameraState(p.isCamOn());
//                setMicState(p.isMicOn());
//            }
//        }
//        Info info = props.getPeer().get();
//        if (info != null) {
//            String name = info.getDisplayName();
//            if (name != null) {
//                if (!name.isEmpty()) {
//                    mBinding.tvPeerName.setText(name);
//                } else {
//                    mBinding.tvPeerName.setText("Empty");
//                }
//            } else {
//                mBinding.tvPeerName.setText("Null");
//            }
//        }

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

    /**
     *
     * @param raised
     */
    public void setHandRaisedState(boolean raised) {
        Log.d(TAG, "<< RAISING HAND FOR PEER >>> " + raised);
        if (raised) {
            mBinding.peerInfoHand.clearAnimation();
            mBinding.peerInfoHand.setImageResource(R.drawable.ic_front_hand_red_34);
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.hand_shake);
            mBinding.peerInfoHand.startAnimation(anim);
        } else {
            mBinding.peerInfoHand.clearAnimation();
            mBinding.peerInfoHand.setImageResource(R.drawable.ic_front_hand_white_34);
        }
    }

    private void playAudioEffects() {
        if (props != null) {
            if (mBinding.audioPeerLayout.getVisibility() == View.GONE) {
                if (props.getAudioTrack() != null) {
                    if (props.getAudioTrack().get() != null) {
                        if (Objects.requireNonNull(props.getAudioTrack().get()).state() == MediaStreamTrack.State.LIVE) {
                            mBinding.audioPeerLayout.setVisibility(View.VISIBLE);
                            WooAnimationUtil.showView(mBinding.audioPeerLayout, new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    Glide.with(PeerView.this).asGif().load(R.drawable.ic_audio_giphy).into(mBinding.audioPeerGif);
                                }
                            });
                        } else {
                            stopAudioEffects();
                        }
                    }
                }
            }
        }
    }

    private void stopAudioEffects() {
        WooAnimationUtil.hideView(mBinding.audioPeerLayout, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.audioPeerLayout.setVisibility(View.GONE);
            }
        });
    }

    /**
     *
     * @param resId
     */
    public void setTitleBgDrawable(int resId) {
        mBinding.controls.setBackgroundResource(resId);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dispose();
    }

    public void dispose() {
        mBinding.setWooPeerProps(null);
        mBinding.wooPeerView.setWooPeerViewProps(null);
        mBinding.wooPeerView.wooVideoRenderer.release();
    }

} /** end class [PeerView] */
