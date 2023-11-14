package com.woooapp.meeting.impl.utils;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 11:16 am 31/10/2023
 * <code>class</code> WVoicePlayback.java
 */
public class WVoicePlayback {

    private static final String TAG = WVoicePlayback.class.getSimpleName() + ".java";
    private final VoiceDataSource dataSource;
    private final MediaPlayer mediaPlayer;

    /**
     *
     * @param dataSource
     */
    public WVoicePlayback(@NonNull VoiceDataSource dataSource) {
        this.dataSource = dataSource;
        this.mediaPlayer = new MediaPlayer();
        try {
            this.mediaPlayer.setDataSource(this.dataSource);
            this.mediaPlayer.setLooping(false);
            this.mediaPlayer.prepare();
            this.mediaPlayer.setOnPreparedListener(mp -> {
                Log.d(TAG, "Voice Translation Mediaplayer onPrepared(); ...");
                mp.start();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @throws Exception
     */
    public void play() throws Exception {
        this.mediaPlayer.start();
    }

    /**
     *
     * @param listener
     */
    public void setCompletionListener(@NonNull MediaPlayer.OnCompletionListener listener) {
        this.mediaPlayer.setOnCompletionListener(listener);
    }

    /**
     *
     * @param volume
     */
    public void setVolume(float volume) {
        this.mediaPlayer.setVolume(volume, volume);
    }

    public void release() {
        this.mediaPlayer.stop();
        this.mediaPlayer.release();
    }

} /** end class. */
