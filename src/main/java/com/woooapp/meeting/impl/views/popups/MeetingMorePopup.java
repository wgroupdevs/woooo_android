package com.woooapp.meeting.impl.views.popups;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.toolbox.ImageRequest;
import com.woooapp.meeting.impl.utils.ClipboardCopy;
import com.woooapp.meeting.impl.utils.WEvents;
import com.woooapp.meeting.impl.views.UIManager;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.lib.MeetingClient;

import eu.siacs.conversations.R;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 3:49 pm 02/10/2023
 * <code>class</code> MeetingMorePopup.java
 */
public final class MeetingMorePopup extends RelativeLayout {

    private static final String TAG = MeetingMorePopup.class.getSimpleName() + ".java";
    private final View mContentView;
    private final Context mContext;
    private final RelativeLayout mParent;
    private LinearLayout mMainLayout;
    private final UIManager uiManager;
    private final int mBottomBarHeight;
    private final MeetingClient mClient;
    private Bitmap blurredBg;
    private ImageRequest mImageRequest;
    private DismissCallback callback;

    /**
     * @param context
     * @param parent
     * @param bottomBarHeight
     * @param client
     * @param callback
     */
    public MeetingMorePopup(
            @NonNull final Context context,
            @NonNull RelativeLayout parent,
            int bottomBarHeight,
            @NonNull MeetingClient client,
            @Nullable DismissCallback callback) {
        super(context);
        this.mContext = context;
        this.mParent = parent;
        this.mBottomBarHeight = bottomBarHeight;
        this.mClient = client;
        this.callback = callback;
        this.uiManager = UIManager.getUIManager(mContext);
        this.mContentView = LayoutInflater.from(context).inflate(R.layout.meeting_more_popup, null);
        this.setContentView();
    }

    private void setContentView() {
        // Blurred bg
        blurredBg = uiManager.blur(mContext, uiManager.createBitmap(mParent));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(params);

        this.mMainLayout = mContentView.findViewById(R.id.morePopupContainer);

        TextView tvMeetingId = mContentView.findViewById(R.id.morePopupTvMeetingId);
        tvMeetingId.setText(mClient.getMeetingId());

        View buttonMeetingId = mContentView.findViewById(R.id.morePopupButtonMeetingId);
        buttonMeetingId.setOnClickListener(view -> {
            ClipboardCopy.clipboardCopy(mContext, mClient.getMeetingId());
            Toast.makeText(mContext, "Meeting id " + mClient.getMeetingId() + " copied to clipboard", Toast.LENGTH_LONG).show();
            dismiss();
        });

        View buttonBackground = mContentView.findViewById(R.id.morePopupButtonSelectBackground);
        buttonBackground.setOnClickListener(view -> {
//            WooEvents.getInstance().notify(WooEvents.EVENT_SELECT_BACKGROUND, null);
            dismiss();
        });

        View buttonLanguage = mContentView.findViewById(R.id.morePopupButtonSelectLang);
        buttonLanguage.setOnClickListener(v -> {
            WEvents.getInstance().notify(WEvents.EVENT_CLICKED_LANGUAGE_SELECT, null);
            dismiss();
        });

        View buttonMembers = mContentView.findViewById(R.id.morePopupButtonParticipants);
        buttonMembers.setOnClickListener(view -> {
            WEvents.getInstance().notify(WEvents.EVENT_SHOW_MEMBERS, null);
            dismiss();
        });

        View buttonSpeaker = mContentView.findViewById(R.id.morePopupButtonSpeakerPhone);
        final ImageView ivSpeaker = mContentView.findViewById(R.id.morePopupIvSpeaker);
        buttonSpeaker.setOnClickListener(v -> {

        });

        ImageView ivHeadset = mContentView.findViewById(R.id.morePopupIvHeadset);
        TextView tvHeadset = mContentView.findViewById(R.id.morePopupTvHeadset);

        View buttonScreenShare = mContentView.findViewById(R.id.morePopupScreenShare);
        TextView tvScreenShare = mContentView.findViewById(R.id.tvScreenShare);
        buttonScreenShare.setOnClickListener(view -> {
            WEvents.getInstance().notify(WEvents.EVENT_ENABLE_SCREEN_SHARE, null);
            dismiss();
        });

        // ADMIN ROLES
        View buttonMuteEveryone = mContentView.findViewById(R.id.morePopupButtonMuteEveryone);
        if (mClient.getRole() == MeetingClient.Role.ADMIN) {
            ImageView ivMute = mContentView.findViewById(R.id.morePopupIvMute);
            TextView tvMute = mContentView.findViewById(R.id.morePopupTvMute);
            if (mClient.isAudioMuted()) {
                ivMute.setImageResource(R.drawable.ic_speaker_white_34);
                tvMute.setText("Un Mute Everyone");
            } else {
                ivMute.setImageResource(R.drawable.ic_speaker_off);
                tvMute.setText("Mute Everyone");
            }
            buttonMuteEveryone.setOnClickListener(view -> {
                mClient.setEveryonesAudioMuted(!mClient.isAudioMuted());
                dismiss();
            });
        } else {
            buttonMuteEveryone.setVisibility(View.GONE);
        }

//        View buttonDisableCam = mContentView.findViewById(R.id.morePopupButtonDisableCam);
//        if (mClient.getRole() == MeetingClient.Role.ADMIN) {
//            ImageView ivCam = mContentView.findViewById(R.id.morePopupIvCam);
//            TextView tvCam = mContentView.findViewById(R.id.morePopupTvCam);
//            if (mClient.isEveryoneCamOn()) {
//                ivCam.setImageResource(R.drawable.ic_camera_off_gray);
//                tvCam.setText("Disable Everyone's Camera");
//            } else {
//                ivCam.setImageResource(R.drawable.ic_video_camera_white);
//                tvCam.setText("Enable Everyone's Camera");
//            }
//            buttonDisableCam.setOnClickListener(view -> {
//                mClient.setEveryoneCamOn(!mClient.isEveryoneCamOn());
//                dismiss();
//            });
//        } else {
//            buttonDisableCam.setVisibility(View.GONE);
//        }

//        View buttonCloseMemberVideo = mContentView.findViewById(R.id.morePopupButtonCloseMemberVideo);
//        if (mClient.getRole() == MeetingClient.Role.ADMIN) {
//            // TODO
//        } else {
//            buttonCloseMemberVideo.setVisibility(View.GONE);
//        }

//        View buttonMuteMember = mContentView.findViewById(R.id.morePopupButtonMuteMember);
//        if (mClient.getRole() == MeetingClient.Role.ADMIN) {
//            // TODO
//        } else {
//            buttonMuteMember.setVisibility(View.GONE);
//        }

//        View buttonKickOut = mContentView.findViewById(R.id.morePopupButtonKickMember);
//        if (mClient.getRole() == MeetingClient.Role.ADMIN) {
//            // TODO
//        } else {
//            buttonKickOut.setVisibility(View.GONE);
//        }

        View buttonPassword = mContentView.findViewById(R.id.morePopupPassword);
        TextView tvPassword = mContentView.findViewById(R.id.tvPassword);
        if (mClient.getRole() == MeetingClient.Role.ADMIN) {
            if (mClient.isPasswordSet()) {
                tvPassword.setText("Change Password");
            }
            buttonPassword.setOnClickListener(view -> {
                WEvents.getInstance().notify(WEvents.EVENT_ADD_PASSWORD, true);
                dismiss();
            });

        } else {
            buttonPassword.setVisibility(View.GONE);
        }

//        final ImageView ivThumb = mContentView.findViewById(R.id.morePopupIvThumb);
//        TextView tvName = mContentView.findViewById(R.id.morePopupTvName);
//
//        if (mClient.getPicture() != null) {
//            Log.d(TAG, "Loading image >> " + mClient.getPicture());
//            Http.build().getImage(mContext, true, mClient.getPicture(), new HttpImageAdapter() {
//                @Override
//                public void connected(String resource) {}
//
//                @Override
//                public void failed(String resource, String reasonPhrase) {}
//
//                @Override
//                public void done(String resource, final Bitmap bitmap) {
//                    if (bitmap != null) {
//                        new Handler(Looper.getMainLooper()).post(() -> {
//                            CircleDrawable cd = new CircleDrawable(bitmap);
//                            ivThumb.setImageDrawable(cd);
//                        });
//                    }
//                }
//            });
//        }
//        tvName.setText(mClient.getUsername());
    }

    public void show() {
        final ImageView ivBg = new ImageView(mContext);
        ivBg.setImageBitmap(blurredBg);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ivBg.setOnTouchListener((view, motionEvent) -> true);

        // Add to parent
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.setMargins(10, 0, 10, mBottomBarHeight + 10);
        this.addView(this.mMainLayout, params);
        mParent.addView(this, getLayoutParams());

//        WooAnimationUtil.showView(this.mMainLayout, 100, 300,
//                new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                addView(ivBg, 0, params1);
//            }
//        });

        WooAnimationUtil.slideFromBottom(this.mMainLayout, 800, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                addView(ivBg, 0, params1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void dismiss() {
        WooAnimationUtil.slideToBottom(this.mMainLayout, 800, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (getChildAt(0) != null) {
                    removeViewAt(0);
                }
                mParent.removeView(MeetingMorePopup.this);
                if (blurredBg != null) {
                    blurredBg.recycle();
                }
                if (callback != null) {
                    callback.onDismiss();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getY() < mMainLayout.getY() ||
                    event.getY() > mMainLayout.getY() + mMainLayout.getHeight()) {
                Log.d(TAG, "<< Popup Clicked outside bounds >>");
                dismiss();
                return true;
            }
        }
        return false;
    }

    public interface DismissCallback {
        void onDismiss();
    }

} /**
 * end class.
 */
