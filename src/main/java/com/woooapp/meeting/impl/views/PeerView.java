package com.woooapp.meeting.impl.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import com.bumptech.glide.Glide;
import com.woooapp.meeting.impl.utils.WDirector;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.PeerConnectionUtils;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.model.Info;
import com.woooapp.meeting.lib.model.Peer;
import com.woooapp.meeting.net.models.RoomData;

import org.json.JSONException;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

import java.net.MalformedURLException;
import java.net.URL;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.WooViewPeerBinding;
import pk.muneebahmad.lib.graphics.CircleDrawable;
import pk.muneebahmad.lib.net.Http;
import pk.muneebahmad.lib.net.HttpImageAdapter;
import pk.muneebahmad.lib.views.containers.FlowLayout;

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
    private boolean peerVisibilityUpdateRequired = false;
    private PeerProps props;
    private MeetingClient meetingClient;
    private String peerId;
    private boolean isScreenShare = false;

//    private ImageView peerInfoHand;
//    private ImageView peerInfoMic;
//    private ImageView peerInfoMore;
//    private LinearLayout controls;
//    private TextView tvPeerName;
//    private LinearLayout audioPeerLayout;
//    private ImageView audioPeerGif;
//    private LinearLayout videoHidden;
//    private ImageView wooPeerThumb;
//    private SurfaceViewRenderer surfaceViewRenderer;

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

//        View contentView = LayoutInflater.from(context).inflate(R.layout.woo_view_peer, null);
//        this.peerInfoHand = contentView.findViewById(R.id.peerInfoHand);
//        this.peerInfoMic = contentView.findViewById(R.id.peerInfoMic);
//        this.peerInfoMore = contentView.findViewById(R.id.peerInfoMore);
//        this.controls = contentView.findViewById(R.id.controls);
//        this.tvPeerName = contentView.findViewById(R.id.tvPeerName);
//        this.audioPeerLayout = contentView.findViewById(R.id.audio_peer_layout);
//        this.audioPeerGif = contentView.findViewById(R.id.audio_peer_gif);
//
//        this.surfaceViewRenderer = contentView.findViewById(R.id.wooVideoRenderer);
//        this.videoHidden = contentView.findViewById(R.id.videoHidden);
//        this.wooPeerThumb = contentView.findViewById(R.id.wooPeerThumbIv);
//
//        RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        contentView.setLayoutParams(params);
//        addView(contentView, params);
    }

    public void setScreenShare(boolean on) {
        this.isScreenShare = on;
        if (on) {
            mBinding.peerInfoMore.setVisibility(View.GONE);
            mBinding.peerInfoHand.setVisibility(View.GONE);
            mBinding.peerInfoMic.setVisibility(View.GONE);
            mBinding.audioPeerLayout.setVisibility(View.GONE);
        }
    }

    /**
     * @param name
     */
    public void setName(@Nullable String name) {
        if (name != null) {
            mBinding.tvPeerName.setText(name);
            peerVisibilityUpdateRequired = false;
        }
    }

    public void setVideoState(boolean on) {
        if (on) {
//            mBinding.peerInfoCam.setImageResource(R.drawable.ic_video_camera_white);
//            mBinding.wooPeerView.videoHidden.setVisibility(View.GONE);
//            mBinding.wooPeerView.wooVideoRenderer.setVisibility(View.VISIBLE);
        } else {
//            mBinding.peerInfoCam.setImageResource(R.drawable.ic_camera_off_gray);
//            mBinding.wooPeerView.videoHidden.setVisibility(View.VISIBLE);
//            mBinding.wooPeerView.wooVideoRenderer.setVisibility(View.GONE);
        }
    }

    public void setVideoState2(boolean on) {
//        if (on) {
//            mBinding.videoHidden1.setVisibility(View.GONE);
//        } else {
//            mBinding.videoHidden1.setVisibility(View.VISIBLE);
//        }
    }

    public void setMicState(boolean on) {
        if (on) {
            mBinding.peerInfoMic.setImageResource(R.drawable.ic_mic_white_48dp);
            playAudioEffects();
        } else {
            mBinding.peerInfoMic.setImageResource(R.drawable.icon_mic_white_off);
            stopAudioEffects();
        }
        peerVisibilityUpdateRequired = false;
    }

    public void setAdminStatus() {
        if (meetingClient != null && peerId != null && !isScreenShare) {
            if (meetingClient.getRole() == MeetingClient.Role.ADMIN) {
                mBinding.peerInfoMore.setVisibility(View.VISIBLE);
                mBinding.peerInfoMore.setOnClickListener(view -> {
                    PopupMenu menu = new PopupMenu(getContext(), view);
                    menu.getMenuInflater().inflate(R.menu.meeting_admin_peer_popup, menu.getMenu());
                    menu.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.menuItemMute:
                                meetingClient.muteMember(peerId);
                                return true;
                            case R.id.menuItemCam:
                                meetingClient.turnMemberCamOff(peerId);
                                return true;
                            case R.id.menuItemKickout:
                                meetingClient.kickoutMember(peerId);
                                return true;
                            case R.id.menuItemMakeAdmin:
                                try {
                                    meetingClient.makeNewAdmin(peerId);
                                    return true;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return false;
                        }
                        return false;
                    });
                    menu.show();
                });
            } else {
                mBinding.peerInfoMore.setVisibility(View.GONE);
            }
        }
    }

    /**
     * @param props
     */
    public void render(PeerProps props) {
//        if (surfaceViewRenderer != null && props != null) {
//            if (props.getVideoTrack() != null) {
//                props.getVideoTrack().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//                    @Override
//                    public void onPropertyChanged(Observable sender, int propertyId) {
//                        Log.d(TAG, "<< Video Track property changed");
//                        VideoTrack track = props.getVideoTrack().get();
//                        if (track != null) {
//                            track.addSink(surfaceViewRenderer);
//                            surfaceViewRenderer.setVisibility(View.VISIBLE);
//                            videoHidden.setVisibility(View.GONE);
//                        } else {
//                            surfaceViewRenderer.setVisibility(View.GONE);
//                            videoHidden.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//            }
//        }
    }

    /**
     * @param props
     * @param meetingClient
     * @param peerId
     */
    public void setProps(PeerProps props, MeetingClient meetingClient, @NonNull String peerId) {
        this.props = props;
        this.meetingClient = meetingClient;
        this.peerId = peerId;
        // set view model into included layout
        mBinding.wooPeerView.setWooPeerViewProps(props);

        setAdminStatus();

        // set view model
        mBinding.setWooPeerProps(props);
    }

    /**
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
        peerVisibilityUpdateRequired = false;
    }

    private void playAudioEffects() {
        mBinding.audioPeerLayout.setVisibility(View.VISIBLE);
        WooAnimationUtil.showView(mBinding.audioPeerLayout, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                try {
                    Glide.with(PeerView.this).asGif().load(R.drawable.ic_audio_giphy).into(mBinding.audioPeerGif);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
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
     * @param pId
     */
    public void setImageForPeer(@NonNull String pId) {
        Log.d(TAG, "<< 1 Fetching image for peer " + pId);
//        if (WDirector.getInstance().getRoomData() != null) {
//            if (WDirector.getInstance().getRoomData().getMembers() != null) {
//                for (RoomData.Member m : WDirector.getInstance().getRoomData().getMembers()) {
//                    if (m != null) {
//                        if (m.getSocketId().equals(pId)) {
                            String displayPic = "https://picsum.photos/200";
//                            if (displayPic == null) {
//                                displayPic = "https://picsum.photos/200";
//                            }
//                            if (displayPic.isEmpty()) {
//                                displayPic = "https://picsum.photos/200";
//                            }
//                            try {
//                                URL u = new URL(displayPic);
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                                displayPic = "https://picsum.photos/200";
//                            }
                            Log.d(TAG, "<< 2 Fetching image for peer " + displayPic);
                            Http.build().getImage(getContext(), false, displayPic, new HttpImageAdapter() {
                                @Override
                                public void connected(String resource) {
                                }

                                @Override
                                public void failed(String resource, String reasonPhrase) {
                                }

                                @Override
                                public void done(String resource, Bitmap bitmap) {
                                    if (bitmap != null) {
                                        try {
                                            ((Activity) getContext()).runOnUiThread(() -> {
                                                CircleDrawable cd = new CircleDrawable(bitmap);
                                                mBinding.wooPeerView.wooPeerThumbIv.setImageDrawable(cd);
                                            });
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            });
//                        }
//                    }
//                }
//            }
//        }
    }

    /**
     * @return
     */
    @Nullable
    public PeerProps getProps() {
        return this.props;
    }

    /**
     * @param resId
     */
    public void setTitleBgDrawable(int resId) {
        mBinding.controls.setBackgroundResource(resId);
    }

    @Override
    protected void onDetachedFromWindow() {
        dispose();
        super.onDetachedFromWindow();
    }

    public void dispose() {
        mBinding.setWooPeerProps(null);
        mBinding.wooPeerView.setWooPeerViewProps(null);
        mBinding.wooPeerView.wooVideoRenderer.release();
        mBinding.unbind();
    }

} /**
 * end class [PeerView]
 */
