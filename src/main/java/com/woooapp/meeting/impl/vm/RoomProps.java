package com.woooapp.meeting.impl.vm;

import android.app.Application;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LifecycleOwner;

import com.woooapp.meeting.lib.RoomClient;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.model.RoomInfo;

import eu.siacs.conversations.R;

/**
 * @author Muneeb Ahmad
 * <p>
 * File RoomProps.java
 * Class [RoomProps]
 * on 08/09/2023 at 7:43 pm
 */
public class RoomProps extends EdiasProps {
    private final Animation mConnectingAnimation;
    private ObservableField<String> mInvitationLink;
    private ObservableField<RoomClient.ConnectionState> mConnectionState;
    private ObservableField<Boolean> mAudioOnly;
    private ObservableField<Boolean> mAudioOnlyInProgress;
    private ObservableField<Boolean> mAudioMuted;
    private ObservableField<Boolean> mRestartIceInProgress;
    private final Animation mRestartIceAnimation;

    public RoomProps(@NonNull Application application, @NonNull RoomStore roomStore) {
        super(application, roomStore);
        mConnectingAnimation = AnimationUtils.loadAnimation(getApplication(), R.anim.ani_connecting);
        mInvitationLink = new ObservableField<>();
        mConnectionState = new ObservableField<>();
        mAudioOnly = new ObservableField<>();
        mAudioOnlyInProgress = new ObservableField<>();
        mAudioMuted = new ObservableField<>();
        mRestartIceInProgress = new ObservableField<>();
        mRestartIceAnimation = AnimationUtils.loadAnimation(getApplication(), R.anim.ani_restart_ice);
    }

    public Animation getConnectingAnimation() {
        return mConnectingAnimation;
    }

    public ObservableField<String> getInvitationLink() {
        return mInvitationLink;
    }

    public ObservableField<RoomClient.ConnectionState> getConnectionState() {
        return mConnectionState;
    }

    public ObservableField<Boolean> getAudioOnly() {
        return mAudioOnly;
    }

    public ObservableField<Boolean> getAudioOnlyInProgress() {
        return mAudioOnlyInProgress;
    }

    public ObservableField<Boolean> getAudioMuted() {
        return mAudioMuted;
    }

    public ObservableField<Boolean> getRestartIceInProgress() {
        return mRestartIceInProgress;
    }

    public Animation getRestartIceAnimation() {
        return mRestartIceAnimation;
    }

    private void receiveState(RoomInfo roomInfo) {
        mConnectionState.set(roomInfo.getConnectionState());
        mInvitationLink.set(roomInfo.getUrl());
    }

    @Override
    public void connect(LifecycleOwner owner) {
        RoomStore roomStore = getRoomStore();
        roomStore.getRoomInfo().observe(owner, this::receiveState);
        roomStore
                .getMe()
                .observe(
                        owner,
                        me -> {
                            mAudioOnly.set(me.isAudioOnly());
                            mAudioOnlyInProgress.set(me.isAudioOnlyInProgress());
                            mAudioMuted.set(me.isAudioMuted());
                            mRestartIceInProgress.set(me.isRestartIceInProgress());
                        });
    }
} /**
 * end class [RoomProps]
 */
