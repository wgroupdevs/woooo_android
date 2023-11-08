package com.woooapp.meeting.impl.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import com.bumptech.glide.Glide;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.PeerConnectionUtils;
import com.woooapp.meeting.lib.RoomClient;
import com.woooapp.meeting.lib.model.Me;

import org.webrtc.MediaStreamTrack;
import org.webrtc.VideoTrack;

import java.util.Objects;

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

    private static final String TAG = MeView.class.getSimpleName() + ".java";
    private boolean handRaised = false;
    private MeProps props;

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

    public void setProps(MeProps props, final MeetingClient meetingClient) {
        this.props = props;
        // set view model.
        mBinding.wooPeerView.setWooPeerViewProps(props);
        mBinding.tvMeName.setText("Me");

        if (props.getMe() != null) {
            Me me = props.getMe().get();
            if (me != null) {
                setMicEnabled(me.isMicOn());
                setCamEnabled(me.isCamOn());
                setHandRaisedState(me.isHandRaised());
            }
        }

        if (props.getVideoTrack() != null) {
            props.getVideoTrack().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    VideoTrack vt = props.getVideoTrack().get();
                    if (vt != null) {
                        mBinding.wooPeerView.videoHidden.setVisibility(View.GONE);
                    } else {
                        mBinding.wooPeerView.videoHidden.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

//        if (MeProps.DeviceState.ON.equals(props.getCamState().get())) {
//            Log.d(TAG, "<< Turning Camera ON >>");
//            mBinding.meInfoCam.setImageResource(R.drawable.ic_video_camera_white);
//        } else {
//            Log.d(TAG, "<< Turning Camera OFF >>");
//            mBinding.meInfoCam.setImageResource(R.drawable.ic_camera_off_gray);
//        }

//        if (props.getMe() != null) {
//            Me me = props.getMe().get();
//            if (me != null) {
//                if (me.isAudioMuted()) {
//                    mBinding.meInfoMic.setImageResource(R.drawable.ic_mic_off_gray);
//                } else {
//                    mBinding.meInfoMic.setImageResource(R.drawable.ic_mic_white_48dp);
//                }
//
//                if (me.isCamInProgress()) {
//                    mBinding.meInfoCam.setImageResource(R.drawable.ic_video_camera_white);
//                } else {
//                    mBinding.meInfoCam.setImageResource(R.drawable.ic_camera_off_gray);
//                }
//
//                setHandRaisedState(me.isHandShaking(), props);
//            }
//        }

        mBinding.meInfoHand.setOnClickListener(view -> {
            setHandRaisedState(!handRaised);
            meetingClient.setMeHandRaised(!handRaised);
        });
//        if (MeProps.DeviceState.ON.equals(props.getMicState().get())) {
//
//        } else {
//
//        }
//        if (meetingClient.getUsername() != null) {
//            mBinding.tvMeName.setText(meetingClient.getUsername().isEmpty() ? meetingClient.getEmail() : meetingClient.getUsername());
//        } else {
//            mBinding.tvMeName.setText(meetingClient.getEmail());
//        }

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

    /**
     * @param raisedState
     */
    public void setHandRaisedState(boolean raisedState) {
        if (raisedState) {
            mBinding.meInfoHand.clearAnimation();
            mBinding.meInfoHand.setImageResource(R.drawable.ic_front_hand_red_34);
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.hand_shake);
            mBinding.meInfoHand.startAnimation(anim);
        } else {
            mBinding.meInfoHand.clearAnimation();
            mBinding.meInfoHand.setImageResource(R.drawable.ic_front_hand_white_34);
        }
        handRaised = raisedState;
    }

    public void setCamEnabled(boolean enabled) {
        if (enabled) {
            mBinding.meInfoCam.setImageResource(R.drawable.ic_video_camera_white);
        } else {
            mBinding.meInfoCam.setImageResource(R.drawable.ic_camera_off_gray);
        }
    }

    public void setMicEnabled(boolean enabled) {
        if (enabled) {
            mBinding.meInfoMic.setImageResource(R.drawable.baseline_mic_34);
            playAudioEffects();
        } else {
            mBinding.meInfoMic.setImageResource(R.drawable.icon_mic_white_off);
            stopAudioEffect();
        }
    }

    private void playAudioEffects() {
        if (props != null) {
            if (mBinding.audioMeLayout.getVisibility() == View.GONE) {
                if (props.getAudioTrack() != null) {
                    if (props.getAudioTrack().get() != null) {
                        if (Objects.requireNonNull(props.getAudioTrack().get()).state() == MediaStreamTrack.State.LIVE) {
                            mBinding.audioMeLayout.setVisibility(View.VISIBLE);
                            WooAnimationUtil.showView(mBinding.audioMeLayout, new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    Glide.with(MeView.this).asGif().load(R.drawable.ic_audio_giphy).into(mBinding.audioMeGif);
                                }
                            });
                        } else {
                            stopAudioEffect();
                        }
                    }
                }
            }
        }
    }

    private void stopAudioEffect() {
        WooAnimationUtil.hideView(mBinding.audioMeLayout, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.audioMeLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dispose();
    }

    public void dispose() {
        props = null;
        mBinding.setWooMeProps(null);
        mBinding.wooPeerView.wooVideoRenderer.release();
        Log.d(TAG, "Disposed EGL Surface Renderer ...");
    }

} /**
 * end class [MeView]
 */
