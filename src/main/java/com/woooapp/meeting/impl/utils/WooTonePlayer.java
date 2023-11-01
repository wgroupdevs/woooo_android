package com.woooapp.meeting.impl.utils;

import android.content.res.AssetFileDescriptor;

import androidx.annotation.NonNull;

import java.io.FileDescriptor;
import java.io.IOException;

import eu.siacs.conversations.services.MediaPlayer;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 3:57 pm 16/10/2023
 * <code>class</code> WooTonePlayer.java
 */
public class WooTonePlayer {

    private final AssetFileDescriptor afd;
    private final FileDescriptor fd;
    private final MediaPlayer mediaPlayer;

    /**
     *
     * @param afd
     */
    public WooTonePlayer(@NonNull AssetFileDescriptor afd) {
        this.afd = afd;
        fd = afd.getFileDescriptor();
        this.mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(fd, afd.getStartOffset(), afd.getLength());
            mediaPlayer.setLooping(false);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    public void play() throws Exception {
        mediaPlayer.start();
    }

} /** end class. */
