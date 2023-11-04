package com.woooapp.meeting.impl.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import com.bumptech.glide.Glide;
import com.woooapp.meeting.impl.utils.WooDirector;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.PeerConnectionUtils;
import com.woooapp.meeting.lib.model.Peer;
import com.woooapp.meeting.net.models.RoomData;

import org.json.JSONException;
import org.webrtc.MediaStreamTrack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.WooViewPeerBinding;
import pk.muneebahmad.lib.graphics.CircleDrawable;
import pk.muneebahmad.lib.net.Http;
import pk.muneebahmad.lib.net.HttpImageAdapter;

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
     * @param name
     */
    public void setName(@Nullable String name) {
        if (name != null) {
            mBinding.tvPeerName.setText(name);
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
        if (on) {
            mBinding.videoHidden1.setVisibility(View.GONE);
        } else {
            mBinding.videoHidden1.setVisibility(View.VISIBLE);
        }
    }

    public void setMicState(boolean on) {
        if (on) {
            mBinding.peerInfoMic.setImageResource(R.drawable.ic_mic_white_48dp);
            playAudioEffects();
        } else {
            mBinding.peerInfoMic.setImageResource(R.drawable.icon_mic_white_off);
            stopAudioEffects();
        }
    }

    public void setProps(PeerProps props, MeetingClient meetingClient) {
        this.props = props;
        // set view model into included layout
        mBinding.wooPeerView.setWooPeerViewProps(props);

        if (meetingClient != null) {
            if (meetingClient.getRole() == MeetingClient.Role.ADMIN) {
                if (props.getPeer() != null) {
                    Peer p = (Peer) props.getPeer().get();
                    if (p != null) {
                        mBinding.peerInfoMore.setVisibility(View.VISIBLE);
                        mBinding.peerInfoMore.setOnClickListener(view -> {
                            PopupMenu menu = new PopupMenu(getContext(), view);
                            menu.getMenuInflater().inflate(R.menu.meeting_admin_peer_popup, menu.getMenu());
                            menu.setOnMenuItemClickListener(item -> {
                                switch (item.getItemId()) {
                                    case R.id.menuItemMute:
                                        meetingClient.muteMember(p.getId());
                                        return true;
                                    case R.id.menuItemCam:
                                        meetingClient.turnMemberCamOff(p.getId());
                                        return true;
                                    case R.id.menuItemKickout:
                                        meetingClient.kickoutMember(p.getId());
                                        return true;
                                    case R.id.menuItemMakeAdmin:
                                        try {
                                            meetingClient.makeNewAdmin(p);
                                            return true;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        return false;
                                }
                                return false;
                            });
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                menu.setForceShowIcon(true);
                            }
                            menu.show();
                        });
                    }
                }
            } else {
                mBinding.peerInfoMore.setVisibility(View.GONE);
            }
        }

        if (props.getPeer() != null) {
            Peer p = (Peer) props.getPeer().get();
            if (p != null) {
                setMicState(p.isMicOn());
                setHandRaisedState(p.isHandRaised());
//                setVideoState(WooDirector.getInstance().isCameraOn(p.getId()));
//
//                if (props.getVideoTrack() == null) {
//                    setVideoState(false);
//                } else {
//                    setVideoState(true);
//                }
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
        mBinding.audioPeerLayout.setVisibility(View.VISIBLE);
        WooAnimationUtil.showView(mBinding.audioPeerLayout, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Glide.with(PeerView.this).asGif().load(R.drawable.ic_audio_giphy).into(mBinding.audioPeerGif);
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
     *
     * @param pId
     */
    public void setImageForPeer(@NonNull String pId) {
        if (WooDirector.getInstance().getRoomData() != null) {
            if (WooDirector.getInstance().getRoomData().getMembers() != null) {
                for (RoomData.Member m : WooDirector.getInstance().getRoomData().getMembers()) {
                    if (m != null) {
                        if (m.getSocketId().equals(pId)) {
                            if (m.getPicture() != null) {
                                String displayPic = m.getPicture();
                                if (displayPic.isEmpty()) {
                                    displayPic = "https://picsum.photos/200";
                                }
                                try {
                                    URL u = new URL(displayPic);
                                    Http.build().getImage(getContext(), true, displayPic, new HttpImageAdapter() {
                                        @Override
                                        public void connected(String resource) {}

                                        @Override
                                        public void failed(String resource, String reasonPhrase) {}

                                        @Override
                                        public void done(String resource, Bitmap bitmap) {
                                            if (bitmap != null) {
                                                try {
                                                    ((Activity) getContext()).runOnUiThread(() -> {
                                                        CircleDrawable cd = new CircleDrawable(bitmap);
                                                        mBinding.wooPeerThumbIv1.setImageDrawable(cd);
                                                    });
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
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

} /**
 * end class [PeerView]
 */
