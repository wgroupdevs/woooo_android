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
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.PeerConnectionUtils;

import org.json.JSONException;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.WooViewPeerBinding;
import pk.muneebahmad.lib.graphics.CircleDrawable;
import pk.muneebahmad.lib.net.Http;
import pk.muneebahmad.lib.net.HttpImageAdapter;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 10:02 am 18/11/2023
 * <code>class</code> ScreenSharePeerView.java
 */
public class ScreenSharePeerView extends RelativeLayout {

    private static final String TAG = PeerView.class.getSimpleName() + ".java";
    private boolean handRaised = false;
    private boolean peerVisibilityUpdateRequired = false;
    private PeerProps props;
    private MeetingClient meetingClient;
    private String peerId;

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

    public ScreenSharePeerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ScreenSharePeerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScreenSharePeerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ScreenSharePeerView(
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
     * @param props
     * @param meetingClient
     * @param peerId
     */
    public void setProps(PeerProps props, MeetingClient meetingClient, @NonNull String peerId) {
        this.props = props;
        this.meetingClient = meetingClient;
        this.peerId = peerId;

        mBinding.peerInfoMore.setVisibility(View.GONE);
        mBinding.peerInfoHand.setVisibility(View.GONE);
        mBinding.peerInfoMic.setVisibility(View.GONE);
        mBinding.audioPeerLayout.setVisibility(View.GONE);
        mBinding.tvPeerName.setVisibility(View.GONE);
        mBinding.wooPeerView.wooPeerThumbIv.setImageResource(R.drawable.ic_screen_share_34);

        // set view model into included layout
        mBinding.wooPeerView.setWooPeerViewProps(props);

        // set view model
        mBinding.setWooPeerProps(props);
    }

    /**
     * @return
     */
    @Nullable
    public PeerProps getProps() {
        return this.props;
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

} /** end class. */
